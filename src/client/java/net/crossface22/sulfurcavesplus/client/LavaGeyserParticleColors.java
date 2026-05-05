package net.crossface22.sulfurcavesplus.client;

import net.minecraft.util.RandomSource;

public final class LavaGeyserParticleColors {
    private static final float EMBER_CHANCE = 0.08F;

    private LavaGeyserParticleColors() {
    }

    public static float[] pick(RandomSource random) {
        if (random.nextFloat() < EMBER_CHANCE) {
            return new float[] {
                    0.34F + random.nextFloat() * 0.08F,
                    0.28F + random.nextFloat() * 0.05F,
                    0.22F + random.nextFloat() * 0.04F
            };
        }

        float smoke = 0.12F + random.nextFloat() * 0.18F;
        return new float[] { smoke, smoke, smoke };
    }
}
