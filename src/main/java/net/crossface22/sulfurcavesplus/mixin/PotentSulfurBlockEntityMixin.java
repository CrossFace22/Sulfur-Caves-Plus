package net.crossface22.sulfurcavesplus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.crossface22.sulfurcavesplus.ScpConfig;
import net.crossface22.sulfurcavesplus.registry.ScpEffects;
import net.crossface22.sulfurcavesplus.registry.ScpGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PotentSulfurBlock;
import net.minecraft.world.level.block.entity.PotentSulfurBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PotentSulfurState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
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
    private boolean wasPowered = false;

    @Shadow
    private static BlockPos findNoxiousGasSourceBlock(Level level, BlockPos origin) {
        return null;
    }

    @Unique
    private static final ThreadLocal<Boolean> scp$blockEffects = ThreadLocal.withInitial(() -> false);

    @Unique
    private static boolean scp$isGeyser(Level level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK);
    }

    @Unique
    private static boolean scp$noxiousGasOnGeysers(Level level) {
        return level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(ScpGameRules.NOXIOUS_GAS_ON_GEYSERS);
    }

    @Inject(
            method = "lambda$static$0", // SERVER_NAUSEA_EFFECT_TICKER
            at = @At("HEAD")
    )
    private static void scp$markIfGeyser(Level level, BlockPos pos, BlockState state, PotentSulfurBlockEntity entity, CallbackInfo ci) {
        scp$blockEffects.set(scp$isGeyser(level, pos));
    }

    @Inject(
            method = "lambda$static$0", // SERVER_NAUSEA_EFFECT_TICKER
            at = @At("TAIL")
    )
    private static void scp$clearFlag(Level level, BlockPos pos, BlockState state, PotentSulfurBlockEntity entity, CallbackInfo ci) {
        scp$blockEffects.set(false);
    }

    @Inject(
            method = "findNoxiousGasSourceBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void scp$allowLava(Level level, BlockPos origin, CallbackInfoReturnable<BlockPos> cir) {

        if (!level.getBlockState(origin.below()).is(Blocks.MAGMA_BLOCK)) {
            return;
        }

        int maxY = origin.getY() + 5;
        BlockPos.MutableBlockPos pos = origin.above(2).mutable();

        boolean foundLava = false;

        while (pos.getY() <= maxY) {

            var fluid = level.getFluidState(pos);

            if (fluid.isSourceOfType(Fluids.LAVA)) {
                foundLava = true;
                pos.move(Direction.UP);
                continue;
            }

            if (foundLava && level.getBlockState(pos).isAir()) {
                cir.setReturnValue(pos.immutable());
                return;
            }

            break;
        }
    }

    @Inject(method = "applyNauseaEffect", at = @At("HEAD"), cancellable = true)
    private static void scp$cancelIfGeyser(LivingEntity entity, CallbackInfo ci) {
        if (scp$blockEffects.get() && !scp$noxiousGasOnGeysers(entity.level())) {
            ci.cancel();
        }
    }

    @Inject(method = "applyNauseaEffect", at = @At("TAIL"))
    private static void scp$applyCorrosionGas(LivingEntity entity, CallbackInfo ci) {
        int duration = ScpConfig.INSTANCE.potentSulfurEffectDuration;
        entity.addEffect(new MobEffectInstance(
                ScpEffects.CORROSION, duration, 0, true, true));
    }

    @Inject(
            method = "lambda$static$1", // CLIENT_NOXIOUS_GAS_TICKER
            at = @At("HEAD"),
            cancellable = true
    )
    private static void scp$noGasParticles(Level level, BlockPos pos, BlockState state, PotentSulfurBlockEntity entity, CallbackInfo ci) {
        if (scp$isGeyser(level, pos) && !scp$noxiousGasOnGeysers(level)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "lambda$static$3", // SERVER_WAITING_COUNTDOWN_TICKER
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onServerTick(Level level, BlockPos pos, BlockState state, PotentSulfurBlockEntity entity, CallbackInfo ci) {

        PotentSulfurBlockEntityMixin self = (PotentSulfurBlockEntityMixin)(Object) entity;

        boolean powered = level.hasNeighborSignal(pos);

        if (powered) {

            if (state.getValue(PotentSulfurBlock.STATE) != PotentSulfurState.ERUPTING) {
                level.setBlock(pos, state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.ERUPTING), 3);
            }

            entity.waitingCountdown = entity.geyserEruptionTime;

            self.wasPowered = true;
            ci.cancel();
            return;
        }

        if (!powered && self.wasPowered) {
            entity.waitingCountdown = entity.geyserEruptionTime;
            self.wasPowered = false;
        }
    }

    @Inject(
            method = "spawnGeyserParticle",
            at = @At("TAIL")
    )
    private static void scp$spawnFireParticles(Level level, Vec3 sulfurPos, Vec3 sourcePos, CallbackInfo ci) {
        BlockPos pos = BlockPos.containing(sourcePos);

        boolean isLavaGeyser = false;
        for (int i = -2; i <= 3; i++) {
            if (level.getFluidState(pos.offset(0, i, 0)).is(net.minecraft.world.level.material.Fluids.LAVA)) {
                isLavaGeyser = true;
                break;
            }
        }

        if (!isLavaGeyser) return;

        BlockPos sulfurBlockPos = BlockPos.containing(sulfurPos);
        int lavaSources = 0;
        for (int i = 1; i <= 4; i++) {
            if (level.getFluidState(sulfurBlockPos.above(i)).isSourceOfType(Fluids.LAVA)) {
                lavaSources++;
            }
        }
        lavaSources = Math.max(1, lavaSources);

        double height = Math.max(1.0, sourcePos.y - sulfurPos.y);
        double heightScale = lavaSources / 4.0;
        double plumeHeight = height + 2.5 + (heightScale * 5.5);

        int lavaCount = 5 + level.getRandom().nextInt(8);

        for (int i = 0; i < lavaCount; i++) {
            double rise = level.getRandom().nextDouble();
            double randomYOffset = Math.pow(rise, 0.65) * plumeHeight;

            double spread = 0.2 + (randomYOffset * 0.08);
            double rx = (level.getRandom().nextDouble() - 0.5) * spread;
            double rz = (level.getRandom().nextDouble() - 0.5) * spread;

            level.addParticle(
                    ParticleTypes.LAVA,
                    sourcePos.x + rx,
                    sourcePos.y + randomYOffset,
                    sourcePos.z + rz,
                    rx * 0.01,
                    0.06 + (heightScale * 0.18) + level.getRandom().nextDouble() * 0.10,
                    rz * 0.01
            );
        }

    }

    @Inject(
            method = "lambda$static$4", // SERVER_LAUNCH_ENTITY_TICKER
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

    @Redirect(
            method = "lambda$static$4", // SERVER_LAUNCH_ENTITY_TICKER
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V")
    )
    private static void scp$modifyGeyserSound(Level level, Entity entity, BlockPos pos, net.minecraft.sounds.SoundEvent sound, net.minecraft.sounds.SoundSource source, float volume, float pitch, @Local(argsOnly = true) BlockPos origin) {

        boolean isLava = level.getFluidState(origin.above()).is(net.minecraft.world.level.material.Fluids.LAVA);

        float finalPitch = pitch;
        if (isLava) {
            finalPitch = 0.4F;
        }

        level.playSound(entity, pos, sound, source, volume, finalPitch);
    }
}
