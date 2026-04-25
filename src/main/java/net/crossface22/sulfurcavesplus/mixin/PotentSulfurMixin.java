package net.crossface22.sulfurcavesplus.mixin;

import net.crossface22.sulfurcavesplus.ScpConfig;
import net.crossface22.sulfurcavesplus.block.GeyserAirBlock;
import net.crossface22.sulfurcavesplus.registry.ScpBlocks;
import net.crossface22.sulfurcavesplus.registry.ScpEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.PotentSulfurEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotentSulfurEntity.class)
public abstract class PotentSulfurMixin {

    // ── Corrosion gas ─────────────────────────────────────────────────────────

    @Inject(method = "applyNauseaEffect", at = @At("TAIL"))
    private static void scp$applyCorrosionGas(LivingEntity entity, CallbackInfo ci) {
        int duration = ScpConfig.INSTANCE.potentSulfurEffectDuration;
        entity.addEffect(new MobEffectInstance(
                ScpEffects.CORROSION, duration, 0, true, true));
    }

    // ── Geyser column management ───────────────────────────────────────────────
    // Particles are emitted by GeyserAirBlock.tick() independently.
    // This injection only handles building/tearing down the column structure
    // and playing the ambient sound.

    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void scp$geyserTick(Level level, BlockPos pos, BlockState state,
                                        PotentSulfurEntity be, CallbackInfo ci) {
        if (level.isClientSide()) return;

        boolean conditionsMet = level.getBlockState(pos.above()).is(Blocks.WATER)
                && level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK);

        // Rebuild / validate column every 5 ticks
        if (level.getGameTime() % 5 == 0) {
            if (conditionsMet) {
                scp$buildColumn(level, pos.above());
            } else {
                scp$tearDownColumn(level, pos.above().above(), GeyserAirBlock.MAX_HEIGHT);
            }
        }

        // Ambient sound is now emitted by GeyserAirBlock.tick() at distance=1.
    }

    // ── Column helpers ────────────────────────────────────────────────────────

    private static void scp$buildColumn(Level level, BlockPos waterPos) {
        // Only bootstrap the distance=1 block; the rest of the column propagates
        // automatically via GeyserAirBlock.tick() — each block places the one above it
        // when it sees air.  This also means obstructions that are later removed cause the
        // column to re-grow organically without any extra logic here.
        BlockPos d1 = waterPos.above();
        BlockState st = level.getBlockState(d1);
        if (st.isAir()) {
            level.setBlock(d1,
                    ScpBlocks.GEYSER_AIR.defaultBlockState()
                            .setValue(GeyserAirBlock.DISTANCE, 1),
                    Block.UPDATE_ALL);
        } else if (st.getBlock() instanceof GeyserAirBlock
                && st.getValue(GeyserAirBlock.DISTANCE) != 1) {
            // Correct a mismatched distance on the seed block
            level.setBlock(d1, st.setValue(GeyserAirBlock.DISTANCE, 1), Block.UPDATE_ALL);
        }
        // If d1 is occupied by a non-geyser solid block the column cannot grow;
        // the geyser_air blocks above (if any) will self-invalidate via their own tick cascade.
    }

    private static void scp$tearDownColumn(Level level, BlockPos start, int maxBlocks) {
        BlockPos current = start;
        for (int i = 0; i < maxBlocks; i++) {
            if (level.getBlockState(current).getBlock() instanceof GeyserAirBlock) {
                level.setBlock(current, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            } else break;
            current = current.above();
        }
    }
}
