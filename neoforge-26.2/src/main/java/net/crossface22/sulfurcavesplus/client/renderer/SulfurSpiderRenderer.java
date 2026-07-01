package net.crossface22.sulfurcavesplus.client.renderer;

import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.renderer.entity.CaveSpiderRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class SulfurSpiderRenderer extends CaveSpiderRenderer {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            "sulfurcavesplus", "textures/entity/spider/sulfur_spider.png");

    private static final RenderType EYES = RenderTypes.eyes(Identifier.fromNamespaceAndPath(
            "sulfurcavesplus", "textures/entity/spider/sulfur_spider_eyes.png"));

    public SulfurSpiderRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.layers.removeIf(layer -> layer instanceof SpiderEyesLayer);
        this.addLayer(new EyesLayer<LivingEntityRenderState, SpiderModel>(this) {
            @Override
            public RenderType renderType() {
                return EYES;
            }
        });

        this.addLayer(new SulfurSpikeTailLayer(this, context));
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
