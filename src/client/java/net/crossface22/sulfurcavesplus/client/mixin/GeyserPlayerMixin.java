package net.crossface22.sulfurcavesplus.client.mixin;

import net.crossface22.sulfurcavesplus.block.GeyserAirBlock;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Applies upward geyser push to the local player on the client side,
 * bypassing server-authoritative movement prediction issues.
 */
@Mixin(LocalPlayer.class)
public abstract class GeyserPlayerMixin {

    // Keep in sync with GeyserAirBlock constants.
    private static final double PUSH_PER_TICK = GeyserAirBlock.PUSH_PER_TICK;
    private static final double MAX_VELOCITY  = GeyserAirBlock.MAX_UP_VELOCITY;

    @Inject(method = "tick", at = @At("TAIL"))
    private void scp$geyserPush(CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer)(Object)this;
        if (self.getAbilities().flying) return;

        Level level = self.level();
        BlockPos feet  = self.blockPosition();
        BlockPos torso = feet.above();

        BlockState feetState  = level.getBlockState(feet);
        BlockState torsoState = level.getBlockState(torso);

        boolean inGeyser = feetState.getBlock()  instanceof GeyserAirBlock
                        || torsoState.getBlock() instanceof GeyserAirBlock;
        if (!inGeyser) return;

        Vec3 vel = self.getDeltaMovement();
        self.setDeltaMovement(vel.x, Math.min(vel.y + PUSH_PER_TICK, MAX_VELOCITY), vel.z);
        self.resetFallDistance();
    }
}
