package net.crossface22.sulfurcavesplus.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * Upward water-jet particle for the geyser column.
 * Texture is driven by assets/sulfurcavesplus/particles/geyser_water.json.
 */
public class GeyserWaterParticle extends SingleQuadParticle {

    GeyserWaterParticle(ClientLevel level, double x, double y, double z,
                        double vx, double vy, double vz, TextureAtlasSprite sprite) {
        super(level, x, y, z, vx, vy, vz, sprite);
        this.quadSize = 0.12F + this.random.nextFloat() * 0.08F;
        this.gravity = 0.0F;   // upward momentum is set by sendParticles velocity
        this.lifetime = 20 + this.random.nextInt(12);
        this.alpha = 0.75F;

        // Random colour: blue, light blue/cyan, or white
        switch (this.random.nextInt(5)) {
            case 0 -> this.setColor(0.10f, 0.35f, 1.00f); // deep blue
            case 1 -> this.setColor(0.20f, 0.30f, 1.00f); // cyan / light blue
            default -> this.setColor(1.00f, 1.00f, 1.00f); // white
        }
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    // ── Factory ───────────────────────────────────────────────────────────────

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz,
                                       RandomSource random) {
            return new GeyserWaterParticle(level, x, y, z, vx, vy, vz,
                    this.sprites.get(random));
        }
    }
}
