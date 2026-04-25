package net.crossface22.sulfurcavesplus.client;

import net.crossface22.sulfurcavesplus.client.particle.GeyserWaterParticle;
import net.crossface22.sulfurcavesplus.client.renderer.SulfurSpiderRenderer;
import net.crossface22.sulfurcavesplus.registry.ScpEntities;
import net.crossface22.sulfurcavesplus.registry.ScpParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class SulfurCavesPlusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ScpEntities.SULFUR_SPIDER, SulfurSpiderRenderer::new);

        ParticleProviderRegistry.getInstance().register(
                ScpParticleTypes.GEYSER_WATER,
                GeyserWaterParticle.Provider::new
        );
    }
}
