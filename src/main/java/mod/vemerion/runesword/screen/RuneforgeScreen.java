package mod.vemerion.runesword.screen;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.container.RuneforgeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;

public class RuneforgeScreen extends ContainerScreen<RuneforgeContainer> {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/runeforge.png");

	public RuneforgeScreen(RuneforgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX,
			int mouseY) {
		Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		blit(matrixStack, x, y, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrix, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(matrix, mouseX, mouseY);
	}
	
	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		super.render(matrix, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(matrix, mouseX, mouseY);
		renderRuneTooltips(matrix, mouseX, mouseY);
	}

	private void renderRuneTooltips(MatrixStack matrix, int mouseX, int mouseY) {
		for (int i = 0; i < RuneforgeContainer.RUNE_SLOTS_X.length; i++) {
			int index = i;
			if (containsMouse(RuneforgeContainer.RUNE_SLOTS_X[i], RuneforgeContainer.RUNE_SLOTS_Y[i], 18, mouseX,
					mouseY)) {

				LazyOptional<Runes> maybeRunes = Runes.getRunes(container.getSword());
				if (!maybeRunes.isPresent()) {
					renderTooltip(matrix, new TranslationTextComponent("gui.runesword.no_sword"), mouseX, mouseY);
				} else {
					maybeRunes.ifPresent(runes -> {
						if (!runes.isSlotUnlocked(index)) {
							renderTooltip(matrix, new TranslationTextComponent("gui.runesword.slot_locked"), mouseX,
									mouseY);
						}
					});
				}
			}
		}
	}

	private boolean containsMouse(int left, int top, int length, int mouseX, int mouseY) {
		return mouseX > guiLeft + left && mouseX < guiLeft + left + length && mouseY > guiTop + top && mouseY < guiTop + top + length;
	}

}
