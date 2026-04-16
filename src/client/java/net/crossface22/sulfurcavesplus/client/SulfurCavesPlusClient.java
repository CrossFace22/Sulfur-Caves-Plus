package net.crossface22.sulfurcavesplus.client;

import net.crossface22.sulfurcavesplus.client.renderer.SulfurSpiderRenderer;
import net.crossface22.sulfurcavesplus.registry.ScpEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class SulfurCavesPlusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ScpEntities.SULFUR_SPIDER, SulfurSpiderRenderer::new);
    }
}
