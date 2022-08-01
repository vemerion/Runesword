package mod.vemerion.runesword.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.container.RuneforgeMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.util.LazyOptional;

public class RuneforgeScreen extends AbstractContainerScreen<RuneforgeMenu> {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/runeforge.png");

	public RuneforgeScreen(RuneforgeMenu menu, Inventory inv, Component titleIn) {
		super(menu, inv, titleIn);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		super.renderLabels(poseStack, mouseX, mouseY);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		super.render(poseStack, mouseX, mouseY, partialTicks);
		renderTooltip(poseStack, mouseX, mouseY);
		renderRuneTooltips(poseStack, mouseX, mouseY);
	}

	private void renderRuneTooltips(PoseStack poseStack, int mouseX, int mouseY) {
		for (int i = 0; i < RuneforgeMenu.RUNE_SLOTS_X.length; i++) {
			int index = i;
			if (containsMouse(RuneforgeMenu.RUNE_SLOTS_X[i], RuneforgeMenu.RUNE_SLOTS_Y[i], 18, mouseX, mouseY)) {

				LazyOptional<Runes> maybeRunes = Runes.getRunes(menu.getRuneable());
				if (!maybeRunes.isPresent()) {
					renderTooltip(poseStack, new TranslatableComponent("gui." + Main.MODID + ".no_sword"), mouseX,
							mouseY);
				} else {
					maybeRunes.ifPresent(runes -> {
						if (!runes.isSlotUnlocked(index)) {
							renderTooltip(poseStack, new TranslatableComponent("gui." + Main.MODID + ".slot_locked"),
									mouseX, mouseY);
						}
					});
				}
			}
		}
	}

	private boolean containsMouse(int left, int top, int length, int mouseX, int mouseY) {
		return mouseX > leftPos + left && mouseX < leftPos + left + length && mouseY > topPos + top
				&& mouseY < topPos + top + length;
	}

}
