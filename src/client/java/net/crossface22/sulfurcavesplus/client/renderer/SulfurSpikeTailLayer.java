package net.crossface22.sulfurcavesplus.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class SulfurSpikeTailLayer extends RenderLayer<LivingEntityRenderState, SpiderModel> {

    private static final float TAIL_Z = 6.0f / 16.0f;

    private static final float SPIKE_SCALE = 0.5f;

    private final BlockModelRenderState spike;

    public SulfurSpikeTailLayer(
            RenderLayerParent<LivingEntityRenderState, SpiderModel> renderer,
            BlockModelRenderState spike) {
        super(renderer);
        this.spike = spike;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector,
                       int packedLight, LivingEntityRenderState renderState,
                       float yRot, float xRot) {
        if (spike.isEmpty()) return;
        poseStack.pushPose();
        this.getParentModel().root().getChild("body1").translateAndRotate(poseStack);

        poseStack.translate(0.0f, 0.0f, TAIL_Z);
        poseStack.scale(SPIKE_SCALE, SPIKE_SCALE, SPIKE_SCALE);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
        poseStack.translate(-0.5f, 0f, 0.5f);

        spike.submit(poseStack, collector, packedLight,
                OverlayTexture.NO_OVERLAY, renderState.outlineColor);

        poseStack.popPose();
    }
}
