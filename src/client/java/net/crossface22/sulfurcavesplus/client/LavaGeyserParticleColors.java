package net.crossface22.sulfurcavesplus.client;

import net.minecraft.util.RandomSource;

public final class LavaGeyserParticleColors {
    private static final float EMBER_CHANCE = 0.18F;

    private LavaGeyserParticleColors() {
    }

    public static float[] pick(RandomSource random) {
        if (random.nextFloat() < EMBER_CHANCE) {
            return new float[] {
                    0.42F + random.nextFloat() * 0.14F,
                    0.24F + random.nextFloat() * 0.08F,
                    0.14F + random.nextFloat() * 0.05F
            };
        }

        float smoke = 0.12F + random.nextFloat() * 0.18F;
        return new float[] { smoke, smoke, smoke };
    }
}
