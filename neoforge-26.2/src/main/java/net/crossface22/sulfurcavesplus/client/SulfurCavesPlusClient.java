package net.crossface22.sulfurcavesplus.client;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.client.renderer.SulfurSpiderRenderer;
import net.crossface22.sulfurcavesplus.registry.ScpEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = SulfurCavesPlus.MOD_ID, value = Dist.CLIENT)
public final class SulfurCavesPlusClient {

    private SulfurCavesPlusClient() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ScpEntities.SULFUR_SPIDER.get(), SulfurSpiderRenderer::new);
    }
}
