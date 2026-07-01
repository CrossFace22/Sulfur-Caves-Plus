package net.crossface22.sulfurcavesplus.client.mixin;

import net.crossface22.sulfurcavesplus.client.LavaGeyserParticleColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.GeyserPlumeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.GeyserParticleOptions;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GeyserPlumeParticle.class)
public class GeyserPlumeParticleMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void scp$applyLavaColor(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, GeyserParticleOptions options, SpriteSet sprites, CallbackInfo ci) {
        GeyserPlumeParticle self = (GeyserPlumeParticle)(Object)this;
        BlockPos pos = BlockPos.containing(x, y, z);

        boolean isLavaGeyser = false;
        BlockPos check = pos;
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

        if (isLavaGeyser) {
            float[] color = LavaGeyserParticleColors.pick(level.getRandom());
            self.setColor(color[0], color[1], color[2]);
        }
    }
}
