package mod.vemerion.runesword.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.container.RuneforgeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class RuneforgeScreen extends ContainerScreen<RuneforgeContainer> {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/runeforge.png");

	public RuneforgeScreen(RuneforgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		blit(matrixStack, x, y, 0, 0, xSize, ySize);
	}

}
