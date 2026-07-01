package net.crossface22.sulfurcavesplus.client.mixin;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.GeyserBaseParticle;
import net.minecraft.client.particle.GeyserPlumeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public abstract class GeyserParticleCollisionMixin {

    @Shadow
    protected ClientLevel level;

    @Shadow
    protected boolean hasPhysics;

    @Shadow
    public abstract AABB getBoundingBox();

    @Unique
    private boolean scp$previousHasPhysics;

    @Unique
    private static final TagKey<Block> SCP_PASSABLE_BLOCKS = TagKey.create(
            Registries.BLOCK,
            Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "geyser_passable_blocks")
    );

    @Unique
    private boolean scp$isGeyserParticlePassableBlock(BlockState state) {
        if (state.is(SCP_PASSABLE_BLOCKS)) {
            return true;
        }

        return state.getBlock() instanceof TrapDoorBlock
                && state.getValue(TrapDoorBlock.OPEN);
    }

    @Inject(
            method = "move",
            at = @At("HEAD")
    )
    private void scp$ignorePassableBlocksForGeyserParticle(
            double dx,
            double dy,
            double dz,
            CallbackInfo ci
    ) {
        this.scp$previousHasPhysics = this.hasPhysics;

        if (!scp$isGeyserParticle()) {
            return;
        }

        if (!this.hasPhysics) {
            return;
        }

        if (scp$wouldOnlyCollideWithPassableBlocks(dx, dy, dz)) {
            this.hasPhysics = false;
        }
    }

    @Inject(
            method = "move",
            at = @At("TAIL")
    )
    private void scp$restoreGeyserParticlePhysics(
            double dx,
            double dy,
            double dz,
            CallbackInfo ci
    ) {
        if (scp$isGeyserParticle()) {
            this.hasPhysics = this.scp$previousHasPhysics;
        }
    }

    @Unique
    private boolean scp$isGeyserParticle() {
        Object self = this;
        return self instanceof GeyserBaseParticle || self instanceof GeyserPlumeParticle;
    }

    @Unique
    private boolean scp$wouldOnlyCollideWithPassableBlocks(double dx, double dy, double dz) {
        AABB currentBox = this.getBoundingBox();

        // Importante: expandTowards revisa todo el trayecto, no solo la posición final.
        AABB movementBox = currentBox.expandTowards(dx, dy, dz).inflate(0.05D);

        int minX = net.minecraft.util.Mth.floor(movementBox.minX);
        int minY = net.minecraft.util.Mth.floor(movementBox.minY);
        int minZ = net.minecraft.util.Mth.floor(movementBox.minZ);
        int maxX = net.minecraft.util.Mth.floor(movementBox.maxX);
        int maxY = net.minecraft.util.Mth.floor(movementBox.maxY);
        int maxZ = net.minecraft.util.Mth.floor(movementBox.maxZ);

        boolean foundPassableBlockCollision = false;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pos.set(x, y, z);

                    BlockState state = this.level.getBlockState(pos);

                    if (state.isAir()) {
                        continue;
                    }

                    boolean hasCollision = !state
                            .getCollisionShape(this.level, pos, CollisionContext.empty())
                            .isEmpty();

                    if (!hasCollision) {
                        continue;
                    }

                    if (scp$isGeyserParticlePassableBlock(state)) {
                        foundPassableBlockCollision = true;
                        continue;
                    }

                    // Si hay cualquier otro bloque sólido en el camino,
                    // la partícula NO debe atravesarlo.
                    return false;
                }
            }
        }

        return foundPassableBlockCollision;
    }
}