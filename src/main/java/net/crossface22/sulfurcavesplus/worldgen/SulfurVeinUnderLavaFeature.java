package net.crossface22.sulfurcavesplus.worldgen;

import com.mojang.serialization.Codec;
import net.crossface22.sulfurcavesplus.ScpConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class SulfurVeinUnderLavaFeature extends Feature<NoneFeatureConfiguration> {

    public SulfurVeinUnderLavaFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        if (!ScpConfig.INSTANCE.sulfurVeinsGenerateUnderBasaltDeltas) {
            return false;
        }

        BlockPos origin = ctx.origin();
        RandomSource random = ctx.random();
        boolean placed = false;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                if (random.nextInt(8) != 0) continue;

                for (int y = 120; y > 20; y--) {
                    BlockPos pos = new BlockPos(
                            origin.getX() + x,
                            y,
                            origin.getZ() + z
                    );

                    FluidState fluid = level.getFluidState(pos);
                    if (!fluid.isSource() || !fluid.is(Fluids.LAVA)) continue;

                    BlockPos lavaBottom = pos;
                    while (true) {
                        BlockPos candidate = lavaBottom.below();
                        FluidState candidateFluid = level.getFluidState(candidate);
                        if (candidateFluid.isSource() && candidateFluid.is(Fluids.LAVA)) {
                            lavaBottom = candidate;
                        } else {
                            break;
                        }
                    }

                    int lavaCount = 0;
                    BlockPos counter = lavaBottom;
                    while (true) {
                        FluidState counterFluid = level.getFluidState(counter);
                        if (counterFluid.isSource() && counterFluid.is(Fluids.LAVA)) {
                            lavaCount++;
                            counter = counter.above();
                        } else {
                            break;
                        }
                    }

                    BlockPos lavaTop = counter.below();

                    BlockState stateAbove = level.getBlockState(lavaTop.above());
                    if (!stateAbove.isAir()) break;

                    if (lavaCount > 4) break;

                    BlockPos potentPos = lavaBottom.below();
                    BlockPos magmaPos = lavaBottom.below(2);

                    BlockState statePotent = level.getBlockState(potentPos);
                    BlockState stateMagma = level.getBlockState(magmaPos);

                    boolean potentValid = !statePotent.isAir() && statePotent.getFluidState().isEmpty();
                    boolean magmaValid = !stateMagma.isAir() && stateMagma.getFluidState().isEmpty();

                    BlockState stateUnderMagma = level.getBlockState(magmaPos.below());
                    boolean groundValid = !stateUnderMagma.isAir() && stateUnderMagma.getFluidState().isEmpty();

                    if (potentValid && magmaValid && groundValid) {
                        level.setBlock(potentPos, Blocks.POTENT_SULFUR.defaultBlockState(), 3);
                        level.setBlock(magmaPos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
                        placed = true;
                    }

                    break;
                }
            }
        }

        return placed;
    }
}
