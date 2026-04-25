package net.crossface22.sulfurcavesplus.block;

import net.crossface22.sulfurcavesplus.registry.ScpParticleTypes;
import net.crossface22.sulfurcavesplus.registry.ScpSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.PotentSulfurEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GeyserAirBlock extends Block {

    public static final int MAX_HEIGHT = 6;

    /** 1 = directly above the water source, 6 = topmost block. */
    public static final IntegerProperty DISTANCE =
            IntegerProperty.create("distance", 1, MAX_HEIGHT);

    // Upward push applied to non-player entities each tick (players handled client-side).
    public static final double PUSH_PER_TICK   = 0.3;
    public static final double MAX_UP_VELOCITY = 1.5;

    // Particle + validation tick interval (2 ticks ≈ 10 times/second).
    private static final int TICK_INTERVAL = 2;

    /**
     * Tracks the last server tick at which each entity (by runtime ID) was seen
     * inside any geyser_air block.  Used to detect "just entered" and play the
     * entry sound exactly once per entry event.
     */
    private static final Map<Integer, Long> ENTITY_LAST_SEEN = new HashMap<>();

    /**
     * If an entity has not been inside a geyser block for this many ticks it is
     * considered to have left, so the next contact triggers the entry sound again.
     */
    private static final long ENTRY_EXPIRY_TICKS = 8;

    // ── Per-player ambient sound ──────────────────────────────────────────────

    /**
     * How many of the nearest geyser columns each player hears simultaneously.
     * Keeps audio manageable even in dense geyser fields.
     */
    private static final int MAX_AUDIBLE_GEYSERS = 5;

    /** Horizontal / vertical scan radius when searching for nearby geysers. */
    private static final int SOUND_SCAN_H = 32;
    private static final int SOUND_SCAN_V = 8;

    /**
     * Per-player de-duplication: stores the last game-tick at which a player's
     * ambient-sound batch was already dispatched.  Whichever distance=1 geyser
     * fires its per-second check first handles ALL players; later geysers skip
     * already-processed players for that tick.
     */
    private static final Map<UUID, Long> PLAYER_SOUND_TICK = new HashMap<>();

    public GeyserAirBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(DISTANCE, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE);
    }

    // ── Shape ────────────────────────────────────────────────────────────────

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos,
                        BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, TICK_INTERVAL);
        }
    }

    /**
     * Every 2 ticks:
     *  1. Validate — if invalid, cascade-remove self + everything above.
     *  2. Ambient sound (distance=1 only, every second): each player hears their
     *     5 nearest geyser columns via per-player packets.
     *  3. Spawn upward particles.
     *  4. Push non-player entities; play entry sound once when a player first enters.
     *  5. Self-propagate column upward into air.
     */
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int distance = state.getValue(DISTANCE);

        // ── Validation ────────────────────────────────────────────────────────
        if (!isPositionValid(level, pos, distance)) {
            BlockPos current = pos;
            for (int i = 0; i <= MAX_HEIGHT - distance; i++) {
                if (level.getBlockState(current).getBlock() instanceof GeyserAirBlock) {
                    level.setBlock(current, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                } else break;
                current = current.above();
            }
            return;
        }

        long time = level.getGameTime();

        // ── Per-second events (ambient sound + stale-entry cleanup) ───────────
        // Using < TICK_INTERVAL avoids the parity bug: since ticks fire every
        // TICK_INTERVAL game-ticks, 'time' has a fixed parity set at placement.
        // A single == target could be permanently missed; < TICK_INTERVAL always
        // fires once per 20-tick window regardless of parity.
        if (distance == 1 && time % 20 < TICK_INTERVAL) {
            for (ServerPlayer player : level.players()) {
                UUID uid = player.getUUID();
                // Skip this player if another geyser already handled them this tick.
                if (PLAYER_SOUND_TICK.getOrDefault(uid, -1L) == time) continue;
                PLAYER_SOUND_TICK.put(uid, time);
                sendNearestGeyserSounds(level, player);
            }

            // Housekeeping: purge stale entries from both maps
            PLAYER_SOUND_TICK.entrySet().removeIf(e -> time - e.getValue() > 40);
            ENTITY_LAST_SEEN.entrySet().removeIf(e -> time - e.getValue() > ENTRY_EXPIRY_TICKS + 2);
        }

        // ── Particles ─────────────────────────────────────────────────────────
        spawnUpwardParticles(level, pos, random);

        // ── Entity push + entry sound ─────────────────────────────────────────
        AABB box = new AABB(
                pos.getX(),     pos.getY(),     pos.getZ(),
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, box);

        for (Entity entity : entities) {
            int  id       = entity.getId();
            Long lastSeen = ENTITY_LAST_SEEN.get(id);

            // Play entry sound once, only for players, when they weren't in a
            // geyser block recently.  Anchored to the block so it feels directional.
            if (entity instanceof Player
                    && (lastSeen == null || time - lastSeen > ENTRY_EXPIRY_TICKS)) {
                level.playSound(null,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE,
                        SoundSource.BLOCKS, 0.5F, 0.75F);
            }
            ENTITY_LAST_SEEN.put(id, time);

            // Server-side velocity push for non-players.
            // Players are pushed by GeyserPlayerMixin on the client.
            if (!(entity instanceof Player)) {
                Vec3 vel = entity.getDeltaMovement();
                entity.setDeltaMovement(vel.x,
                        Math.min(vel.y + PUSH_PER_TICK, MAX_UP_VELOCITY),
                        vel.z);
                entity.resetFallDistance();
            }
        }

        // ── Self-propagation ──────────────────────────────────────────────────
        if (distance < MAX_HEIGHT && level.getBlockState(pos.above()).isAir()) {
            level.setBlock(pos.above(),
                    defaultBlockState().setValue(DISTANCE, distance + 1),
                    Block.UPDATE_ALL);
        }

        level.scheduleTick(pos, this, TICK_INTERVAL);
    }

    // ── Ambient sound ─────────────────────────────────────────────────────────

    /**
     * Finds the {@link #MAX_AUDIBLE_GEYSERS} geyser bases (distance=1) nearest
     * to {@code player} and sends a targeted {@link ClientboundSoundPacket} for
     * each one directly to that player's connection.
     *
     * <p>Using per-player packets means every player independently hears the
     * columns closest to them — correct in multiplayer without any shared state.</p>
     */
    private static void sendNearestGeyserSounds(ServerLevel level, ServerPlayer player) {
        if (player.connection == null) return;

        BlockPos pPos = player.blockPosition();

        // Collect all distance=1 geyser bases within the scan cylinder.
        List<BlockPos> candidates = new ArrayList<>();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int dx = -SOUND_SCAN_H; dx <= SOUND_SCAN_H; dx++) {
            for (int dz = -SOUND_SCAN_H; dz <= SOUND_SCAN_H; dz++) {
                if (dx * dx + dz * dz > SOUND_SCAN_H * SOUND_SCAN_H) continue;
                for (int dy = -SOUND_SCAN_V; dy <= SOUND_SCAN_V; dy++) {
                    cursor.set(pPos.getX() + dx, pPos.getY() + dy, pPos.getZ() + dz);
                    BlockState st = level.getBlockState(cursor);
                    if (st.getBlock() instanceof GeyserAirBlock
                            && st.getValue(DISTANCE) == 1) {
                        candidates.add(cursor.immutable());
                    }
                }
            }
        }
        if (candidates.isEmpty()) return;

        // Sort by squared distance to player, keep the nearest MAX_AUDIBLE_GEYSERS.
        candidates.sort(Comparator.comparingDouble(p -> {
            int ddx = p.getX() - pPos.getX();
            int ddy = p.getY() - pPos.getY();
            int ddz = p.getZ() - pPos.getZ();
            return ddx * ddx + ddy * ddy + ddz * ddz;
        }));

        Holder<SoundEvent> holder =
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ScpSounds.GEYSER_AMBIENT);
        long baseSeed = level.getRandom().nextLong();
        int  limit    = Math.min(candidates.size(), MAX_AUDIBLE_GEYSERS);

        for (int i = 0; i < limit; i++) {
            BlockPos gp = candidates.get(i);
            player.connection.send(new ClientboundSoundPacket(
                    holder, SoundSource.BLOCKS,
                    gp.getX() + 0.5, gp.getY() + 0.5, gp.getZ() + 0.5,
                    0.3F, 0.4F, baseSeed + i));
        }
    }

    // ── Particle emission ─────────────────────────────────────────────────────

    private static void spawnUpwardParticles(ServerLevel level, BlockPos pos, RandomSource rng) {
        double cx   = pos.getX() + 0.5;
        double cz   = pos.getZ() + 0.5;
        double base = pos.getY();

        // Scale particles down when many geysers are close together so a lone
        // geyser looks full while a dense cluster doesn't overwhelm.
        // Flat scan at the same Y level: cheap (≈50 block reads) and correct at
        // every height because column blocks reflect source density.
        float density = computeNearbyDensity(level, pos);

        // ── Straight-up water jets ────────────────────────────────────────────
        // count=0 mode: (dx, dy, dz) is the velocity direction, speed is magnitude.
        for (int i = 0; i < 8; i++) {
            if (rng.nextFloat() >= density) continue;
            double ox    = (rng.nextDouble() - 0.5) * 0.5;
            double oz    = (rng.nextDouble() - 0.5) * 0.5;
            double speed = 0.7 + rng.nextDouble() * 0.8;
            sendForced(level, rng, ScpParticleTypes.GEYSER_WATER,
                    cx + ox, base + 0.1, cz + oz,
                    0, ox * 0.2, 1.0, oz * 0.2, speed);
        }

        // ── Lateral spray — shoots outward to the sides ───────────────────────
        // Random angle around the column; mostly horizontal, slight upward arc.
        for (int i = 0; i < 6; i++) {
            if (rng.nextFloat() >= density) continue;
            double angle  = rng.nextDouble() * Math.PI * 2;
            double hx     = Math.cos(angle);
            double hz     = Math.sin(angle);
            double hSpeed = 1.9 + rng.nextDouble() * 0.5;
            sendForced(level, rng, ScpParticleTypes.GEYSER_WATER,
                    cx + hx * 0.15, base + 0.2, cz + hz * 0.15,
                    0, hx, 0.35, hz, hSpeed);
        }

        // ── Vapor wisps ───────────────────────────────────────────────────────
        for (int i = 0; i < 3; i++) {
            if (rng.nextFloat() >= density) continue;
            double ox    = (rng.nextDouble() - 0.5) * 0.7;
            double oz    = (rng.nextDouble() - 0.5) * 0.7;
            double speed = 0.08 + rng.nextDouble() * 0.18;
            sendForced(level, rng, ParticleTypes.CLOUD, MAX_CLOUD_PARTICLE_DIST,
                    cx + ox, base + 0.2, cz + oz,
                    0, ox * 0.05, 1.0, oz * 0.05, speed);
        }
        if (rng.nextFloat() < 0.5f * density) {
            double ox    = (rng.nextDouble() - 0.5) * 0.6;
            double oz    = (rng.nextDouble() - 0.5) * 0.6;
            double speed = 0.05 + rng.nextDouble() * 0.12;
            sendForced(level, rng, ParticleTypes.CLOUD, MAX_CLOUD_PARTICLE_DIST,
                    cx + ox, base + 0.5, cz + oz,
                    0, ox * 0.03, 1.0, oz * 0.03, speed);
        }

        // ── SPLASH ────────────────────────────────────────────────────────────
        for (int i = 0; i < 3; i++) {
            if (rng.nextFloat() >= density) continue;
            double ox    = (rng.nextDouble() - 0.5) * 3.9;
            double oz    = (rng.nextDouble() - 0.5) * 3.9;
            double speed = 0.5 + rng.nextDouble() * 0.6;
            sendForced(level, rng, ParticleTypes.SPLASH,
                    cx + ox, base + 0.1, cz + oz,
                    0, ox * 0.15, 1.0, oz * 0.15, speed);
        }

        // ── Steam puff at the topmost block ──────────────────────────────────
        if (!(level.getBlockState(pos.above()).getBlock() instanceof GeyserAirBlock)) {
            int puffCount = Math.max(1, Math.round(10 * density));
            sendForced(level, rng, ParticleTypes.CLOUD, MAX_CLOUD_PARTICLE_DIST,
                    cx, base + 0.9, cz,
                    puffCount, 0.5, 0.06, 0.5, 0.02);
        }
    }

    /**
     * Counts {@link GeyserAirBlock} instances at the same Y level within radius 4.
     * Returns {@code 1.0 / count}, so a lone geyser gets density 1.0 and each block
     * in a cluster of N gets 1/N — their combined output stays roughly constant.
     */
    private static float computeNearbyDensity(ServerLevel level, BlockPos pos) {
        final int R = 4;
        int count = 0;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -R; dx <= R; dx++) {
            for (int dz = -R; dz <= R; dz++) {
                if (dx * dx + dz * dz > R * R) continue;
                cursor.set(pos.getX() + dx, pos.getY(), pos.getZ() + dz);
                if (level.getBlockState(cursor).getBlock() instanceof GeyserAirBlock) {
                    count++;
                }
            }
        }
        return 1.0f / Math.max(1, count);
    }

    /** Max send distance for water / splash particles. */
    private static final double MAX_PARTICLE_DIST       = 24.0;
    /** Max send distance for cloud / steam particles — larger so vapor is seen from afar. */
    private static final double MAX_CLOUD_PARTICLE_DIST = 42.0;
    // (MAX_PARTICLE_DIST_SQ removed — distance² is computed per-call inside sendForced)

    /**
     * Sends a particle to each nearby player individually, applying a
     * quadratic distance-based LOD so players farther away receive
     * proportionally fewer particles.  Beyond {@code maxDist} no particles
     * are sent at all.
     *
     * <p>{@code count = 0} uses velocity mode: (dx, dy, dz) is the direction
     * and {@code speed} is the magnitude.</p>
     */
    private static <T extends ParticleOptions> void sendForced(
            ServerLevel level, RandomSource rng, T type,
            double x, double y, double z,
            int count, double dx, double dy, double dz, double speed) {
        sendForced(level, rng, type, MAX_PARTICLE_DIST, x, y, z, count, dx, dy, dz, speed);
    }

    private static <T extends ParticleOptions> void sendForced(
            ServerLevel level, RandomSource rng, T type, double maxDist,
            double x, double y, double z,
            int count, double dx, double dy, double dz, double speed) {
        double maxDistSq = maxDist * maxDist;
        for (ServerPlayer player : level.players()) {
            double distSq = player.distanceToSqr(x, y, z);
            if (distSq > maxDistSq) continue;

            // Quadratic falloff: probability = t² where t = 1 − (dist / maxDist).
            double t = 1.0 - Math.sqrt(distSq) / maxDist;
            if (rng.nextDouble() > t * t) continue;

            // overrideLimiter=true bypasses the client's "Particles" setting entirely.
            // We honour "Minimal" ourselves: drop ~half the particles so the effect
            // is lighter than All/Decreased but not invisible (vanilla would show none).
            var clientInfo = player.clientInformation();
            if ("MINIMAL".equals(clientInfo.particleStatus().name())
                    && rng.nextFloat() < 0.5f) continue;

            level.sendParticles(player, type, true, false, x, y, z, count, dx, dy, dz, speed);
        }
    }

    // ── Validity rules ────────────────────────────────────────────────────────

    /**
     * distance=1 : below is water AND 2 below has a PotentSulfurEntity.
     * distance>1 : below is geyser_air with distance − 1.
     */
    public static boolean isPositionValid(Level level, BlockPos pos, int distance) {
        BlockState below = level.getBlockState(pos.below());
        if (distance == 1) {
            return below.is(Blocks.WATER)
                    && level.getBlockEntity(pos.below(2)) instanceof PotentSulfurEntity
                    && level.getBlockState(pos.below(3)).is(Blocks.MAGMA_BLOCK);
        }
        return below.getBlock() instanceof GeyserAirBlock
                && below.getValue(DISTANCE) == distance - 1;
    }
}
