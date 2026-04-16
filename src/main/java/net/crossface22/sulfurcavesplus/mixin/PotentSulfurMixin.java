package net.crossface22.sulfurcavesplus.mixin;

import net.crossface22.sulfurcavesplus.ScpConfig;
import net.crossface22.sulfurcavesplus.registry.ScpEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.PotentSulfurEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotentSulfurEntity.class)
public abstract class PotentSulfurMixin {

    @Inject(method = "applyNauseaEffect", at = @At("TAIL"))
    private static void scp$applyCorrosionGas(LivingEntity entity, CallbackInfo ci) {
        int duration = ScpConfig.INSTANCE.potentSulfurEffectDuration;
        entity.addEffect(new MobEffectInstance(
                ScpEffects.CORROSION, duration, 0, true, true));
    }
}
