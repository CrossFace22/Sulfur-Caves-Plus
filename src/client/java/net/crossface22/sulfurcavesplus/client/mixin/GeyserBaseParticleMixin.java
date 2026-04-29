package net.crossface22.sulfurcavesplus.client.mixin;

import net.crossface22.sulfurcavesplus.client.LavaGeyserParticleColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.GeyserBaseParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GeyserBaseParticle.class)
public class GeyserBaseParticleMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void scp$applyLavaColor(ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, int waterBlocks, float burstImpulseBase, SpriteSet sprites, CallbackInfo ci) {
        GeyserBaseParticle self = (GeyserBaseParticle)(Object)this;
        BlockPos pos = BlockPos.containing(x, y, z);

        for (int i = -2; i <= 3; i++) {
            if (level.getFluidState(pos.offset(0, i, 0)).is(Fluids.LAVA)) {
                float[] color = LavaGeyserParticleColors.pick(level.getRandom());

                self.setColor(color[0], color[1], color[2]);
                break;
            }
        }
    }
}
