package net.crossface22.sulfurcavesplus.client.renderer;

import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.CaveSpiderRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;

public class SulfurSpiderRenderer extends CaveSpiderRenderer {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            "sulfurcavesplus", "textures/entity/spider/sulfur_spider.png");

    private static final RenderType EYES = RenderTypes.eyes(Identifier.fromNamespaceAndPath(
            "sulfurcavesplus", "textures/entity/spider/sulfur_spider_eyes.png"));

    private static final BlockDisplayContext SPIKE_CONTEXT = BlockDisplayContext.create();

    public SulfurSpiderRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.layers.removeIf(layer -> layer instanceof SpiderEyesLayer);
        this.addLayer(new EyesLayer<LivingEntityRenderState, SpiderModel>(this) {
            @Override
            public RenderType renderType() {
                return EYES;
            }
        });

        BlockModelRenderState spikeModel = new BlockModelRenderState();
        context.getBlockModelResolver().update(
                spikeModel, Blocks.SULFUR_SPIKE.defaultBlockState(), SPIKE_CONTEXT);

        this.addLayer(new SulfurSpikeTailLayer(this, spikeModel));
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
