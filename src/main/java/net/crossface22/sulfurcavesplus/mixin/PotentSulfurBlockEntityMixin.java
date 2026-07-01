package net.crossface22.sulfurcavesplus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.crossface22.sulfurcavesplus.ScpConfig;
import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.registry.ScpEffects;
import net.crossface22.sulfurcavesplus.registry.ScpGameRules;
import net.crossface22.sulfurcavesplus.util.ScpGeyserTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.PotentSulfurBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotentSulfurBlockEntity.class)
public class PotentSulfurBlockEntityMixin {

    @Unique
    private static final TagKey<Block> SCP_PASSABLE_BLOCKS = TagKey.create(
            Registries.BLOCK,
            Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "geyser_passable_blocks")
    );

    @Unique
    private static boolean scp$isGeyserParticlePassableBlock(BlockState state) {
        if (state.is(SCP_PASSABLE_BLOCKS)) {
            return true;
        }

        return state.getBlock() instanceof TrapDoorBlock
                && state.getValue(TrapDoorBlock.OPEN);
    }

    @Shadow
    private static BlockPos findNoxiousGasSourceBlock(Level level, BlockPos origin) {
        return null;
    }

    @Inject(
            method = "findNoxiousGasSourceBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void scp$allowLava(Level level, BlockPos origin, CallbackInfoReturnable<BlockPos> cir) {

        boolean hasMagmaBelow = level.getBlockState(origin.below()).is(Blocks.MAGMA_BLOCK);
        boolean hasLavaBelow = level.getFluidState(origin.below()).isSourceOfType(Fluids.LAVA);

        if (!hasMagmaBelow && !hasLavaBelow) {
            return;
        }

        int maxY = origin.getY() + 5;
        BlockPos.MutableBlockPos pos = origin.above().mutable();

        boolean foundLava = false;

        while (pos.getY() <= maxY) {

            var fluid = level.getFluidState(pos);

            if (fluid.isSourceOfType(Fluids.LAVA)) {
                foundLava = true;
                pos.move(Direction.UP);
                continue;
            }

            BlockState state = level.getBlockState(pos);

            if (foundLava && (state.isAir() || scp$isGeyserParticlePassableBlock(state))) {
                cir.setReturnValue(pos.immutable());
                return;
            }

            break;
        }
    }

    @Inject(method = "applyNauseaEffect", at = @At("TAIL"))
    private static void scp$applyCorrosionGas(LivingEntity entity, CallbackInfo ci) {
        int duration = ScpConfig.INSTANCE.potentSulfurEffectDuration;
        entity.addEffect(new MobEffectInstance(
                ScpEffects.CORROSION, duration, 0, true, true));
    }

    @Inject(
            method = "isGeyserPassableBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void scp$allowGeyserThroughCopperGrates(
            BlockState state,
            Level level,
            BlockPos pos,
            CollisionContext context,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (scp$isGeyserParticlePassableBlock(state)) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    private static double scp$getLavaParticlePlumeLimit(Level level, BlockPos sourcePos, double fallbackHeight) {
        double sourceY = Vec3.atCenterOf(sourcePos).y;

        int maxCheck = Math.max(1, (int) Math.ceil(fallbackHeight) + 2);
        CollisionContext context = CollisionContext.positionContext((double) sourcePos.below().getY());

        for (int i = 0; i <= maxCheck; i++) {
            BlockPos currentPos = sourcePos.above(i);
            BlockState state = level.getBlockState(currentPos);

            if (scp$isBlockPassableForLavaParticles(state, level, currentPos, context)) {
                continue;
            }

            double blockerBottomY = currentPos.getY();
            return Math.max(0.15D, blockerBottomY - sourceY - 0.05D);
        }

        return fallbackHeight;
    }

    @Unique
    private static boolean scp$isBlockPassableForLavaParticles(
            BlockState state,
            Level level,
            BlockPos pos,
            CollisionContext context
    ) {
        if (state.isAir()) {
            return true;
        }

        if (state.is(Blocks.WATER)) {
            return true;
        }

        if (level.getFluidState(pos).isSourceOfType(Fluids.LAVA)) {
            return true;
        }

        if (scp$isGeyserParticlePassableBlock(state)) {
            return true;
        }

        return state.getCollisionShape(level, pos, context).isEmpty();
    }

    @Inject(
            method = "spawnGeyserParticle",
            at = @At("TAIL")
    )
    private static void scp$spawnFireParticles(Level level, BlockPos sulfurPos, BlockPos sourcePos, CallbackInfo ci) {
        BlockPos pos = sourcePos;

        boolean isLavaGeyser = false;
        BlockPos check = sourcePos;
        for (int i = 0; i <= 16; i++) {
            var fluid = level.getFluidState(check);
            if (fluid.isSourceOfType(Fluids.WATER)) {
                break;
            }
            if (fluid.isSourceOfType(Fluids.LAVA)) {
                isLavaGeyser = true;
                break;
            }
            check = check.below();
        }

        if (!isLavaGeyser) return;

        int lavaSources = 0;
        for (int i = 1; i <= 4; i++) {
            if (level.getFluidState(sulfurPos.above(i)).isSourceOfType(Fluids.LAVA)) {
                lavaSources++;
            }
        }
        lavaSources = Math.max(1, lavaSources);

        Vec3 sulfurCenter = Vec3.atCenterOf(sulfurPos);
        Vec3 sourceCenter = Vec3.atCenterOf(sourcePos);

        double height = Math.max(1.0, sourceCenter.y - sulfurCenter.y);
        double heightScale = lavaSources / 4.0;
        double plumeHeight = height + 2.5 + (heightScale * 5.5);
        plumeHeight = Math.min(plumeHeight, scp$getLavaParticlePlumeLimit(level, sourcePos, plumeHeight));

        int lavaCount = 5 + level.getRandom().nextInt(8);

        for (int i = 0; i < lavaCount; i++) {
            double rise = level.getRandom().nextDouble();
            double randomYOffset = Math.pow(rise, 0.65) * plumeHeight;

            double spread = 0.2 + (randomYOffset * 0.08);
            double rx = (level.getRandom().nextDouble() - 0.5) * spread;
            double rz = (level.getRandom().nextDouble() - 0.5) * spread;

            level.addParticle(
                    ParticleTypes.LAVA,
                    sourceCenter.x + rx,
                    sourceCenter.y + randomYOffset,
                    sourceCenter.z + rz,
                    rx * 0.01,
                    0.06 + (heightScale * 0.18) + level.getRandom().nextDouble() * 0.10,
                    rz * 0.01
            );
        }

    }

    @Inject(
            method = "lambda$static$5",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", shift = At.Shift.AFTER)
    )
    private static void scp$applyLavaGeyserDamage(Level level, BlockPos pos, BlockState state, PotentSulfurBlockEntity entity, CallbackInfo ci, @Local Entity entityToBeLaunched) {
        BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
        if (sourceBlock == null) return;
        if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(ScpGameRules.LAVA_GEYSERS_DAMAGE)) return;

        boolean isLava = level.getFluidState(sourceBlock.below()).is(net.minecraft.world.level.material.Fluids.LAVA);

        if (isLava && entityToBeLaunched instanceof Entity living) {
            living.hurt(level.damageSources().hotFloor(), 0.5F);

            if (!living.fireImmune()) {
                living.igniteForSeconds(3);
            }
        }
    }

    @Inject(
            method = "lambda$static$5",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;checkFallDistanceAccumulation()V",
                    shift = At.Shift.AFTER
            )
    )
    private static void scp$markEntityInsideGeyser(
            Level level,
            BlockPos pos,
            BlockState state,
            PotentSulfurBlockEntity sulfurEntity,
            CallbackInfo ci,
            @Local Entity entityToBeLaunched
    ) {
        ScpGeyserTracker.markInsideGeyser(entityToBeLaunched);
    }

    @Redirect(
            method = "lambda$static$3",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V")
    )
    private static void scp$modifyGeyserSound(Level level, double x, double y, double z, net.minecraft.sounds.SoundEvent sound, net.minecraft.sounds.SoundSource source, float volume, float pitch, boolean delay, @Local(argsOnly = true) BlockPos origin) {

        boolean isLava = level.getFluidState(origin.above()).is(net.minecraft.world.level.material.Fluids.LAVA);

        float finalPitch = pitch;
        if (isLava) {
            finalPitch = 0.4F;
        }

        level.playLocalSound(x, y, z, sound, source, volume, finalPitch, delay);
    }
}
