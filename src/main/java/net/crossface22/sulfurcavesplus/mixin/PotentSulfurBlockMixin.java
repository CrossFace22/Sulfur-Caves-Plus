package net.crossface22.sulfurcavesplus.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PotentSulfurBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.PotentSulfurBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PotentSulfurState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotentSulfurBlock.class)
public class PotentSulfurBlockMixin {

    @Inject(
            method = "validBlockState",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void scp$allowLavaGeyser(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {

        boolean hasWater = level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER);
        boolean hasLava  = level.getFluidState(pos.above()).isSourceOfType(Fluids.LAVA);

        if (!hasWater && !hasLava) {
            cir.setReturnValue(state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.DRY));
            return;
        }

        BlockState belowState = level.getBlockState(pos.below());

        if (!belowState.is(Blocks.MAGMA_BLOCK)) {
            cir.setReturnValue(state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.WET));
            return;
        }

        boolean isGeyser =
                state.getValue(PotentSulfurBlock.STATE) == PotentSulfurState.ERUPTING ||
                        state.getValue(PotentSulfurBlock.STATE) == PotentSulfurState.DORMANT;

        if (!isGeyser) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PotentSulfurBlockEntity entity) {
                entity.resetCountdown();
            }
        }

        cir.setReturnValue(
                state.getValue(PotentSulfurBlock.STATE) == PotentSulfurState.ERUPTING
                        ? state
                        : state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.DORMANT)
        );
    }

    @Redirect(
            method = "onPlace",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V")
    )
    private void scp$modifyEruptionStartSound(net.minecraft.world.level.Level level, net.minecraft.world.entity.Entity entity, BlockPos pos, net.minecraft.sounds.SoundEvent sound, net.minecraft.sounds.SoundSource source, float volume, float pitch) {

        boolean isLava = level.getFluidState(pos.above()).isSourceOfType(net.minecraft.world.level.material.Fluids.LAVA);

        float finalPitch = pitch;
        if (isLava) {
            finalPitch = 0.5F;
        }

        level.playSound(entity, pos, sound, source, volume, finalPitch);
    }
}