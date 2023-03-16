package mod.vemerion.runesword.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import mod.vemerion.runesword.blockentity.RuneforgeBlockEntity;
import mod.vemerion.runesword.capability.Runes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TransformationHelper;

public class RuneforgeBlockEntityRenderer implements BlockEntityRenderer<RuneforgeBlockEntity> {

	private static final int LIGHT = 15728880;

	public RuneforgeBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {

	}

	@Override
	public void render(RuneforgeBlockEntity tileEntityIn, float partialTicks, PoseStack poseStack,
			MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		ItemStack runeable = tileEntityIn.getRuneable();
		if (runeable.isEmpty())
			return;
		float size = 0.4f;
		poseStack.pushPose();
		poseStack.translate(0.5D, 0.97D, 0.5D);
		poseStack.mulPose(TransformationHelper.quatFromXYZ(90, 0, 0, true));
		poseStack.scale(size, size, size);
		Minecraft.getInstance().getItemRenderer().renderStatic(runeable, TransformType.GUI, LIGHT, combinedOverlayIn,
				poseStack, bufferIn, 0);

		poseStack.scale(0.8f, 0.8f, 0.8f);
		Runes.getRunes(runeable).ifPresent(runes -> {
			for (int i = 0; i < Runes.RUNES_COUNT; i++) {
				ItemStack rune = runes.getStackInSlot(i);
				if (rune.isEmpty())
					continue;
				poseStack.pushPose();
				poseStack.mulPose(TransformationHelper.quatFromXYZ(0, 0, 45 + i * 90, true));
				poseStack.translate(1.4, 0, 0);

				Minecraft.getInstance().getItemRenderer().renderStatic(rune, TransformType.GUI, LIGHT,
						combinedOverlayIn, poseStack, bufferIn, 0);
				poseStack.popPose();

			}
		});
		poseStack.popPose();
	}

}
