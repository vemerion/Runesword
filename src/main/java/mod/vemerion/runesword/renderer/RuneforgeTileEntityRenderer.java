package mod.vemerion.runesword.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.tileentity.RuneforgeTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;

public class RuneforgeTileEntityRenderer extends TileEntityRenderer<RuneforgeTileEntity> {
	
	private static final int LIGHT = 15728880;

	public RuneforgeTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(RuneforgeTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		ItemStack runeable = tileEntityIn.getRuneable();
		if (runeable.isEmpty())
			return;
		float size = 0.4f;
		matrixStackIn.push();
		matrixStackIn.translate(0.5D, 0.97D, 0.5D);
		matrixStackIn.rotate(new Quaternion(90, 0, 0, true));
		matrixStackIn.scale(size, size, size);
		Minecraft.getInstance().getItemRenderer().renderItem(runeable, TransformType.GUI, LIGHT,
				combinedOverlayIn, matrixStackIn, bufferIn);
		
		matrixStackIn.scale(0.8f, 0.8f, 0.8f);
		Runes.getRunes(runeable).ifPresent(runes -> {
			for (int i = 0; i < Runes.RUNES_COUNT; i++) {
				ItemStack rune = runes.getStackInSlot(i);
				if (rune.isEmpty())
					continue;
				matrixStackIn.push();
				matrixStackIn.rotate(new Quaternion(0, 0, 45 + i * 90, true));
				matrixStackIn.translate(1.4, 0, 0);

				Minecraft.getInstance().getItemRenderer().renderItem(rune, TransformType.GUI, LIGHT,
						combinedOverlayIn, matrixStackIn, bufferIn);
				matrixStackIn.pop();

			}
		});
		matrixStackIn.pop();
	}

}
