package mod.vemerion.runesword.screen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.capability.GuideData;
import mod.vemerion.runesword.guide.GuideChapter;
import mod.vemerion.runesword.init.ModSounds;
import mod.vemerion.runesword.network.GuideMessage;
import mod.vemerion.runesword.network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GuideScreen extends Screen {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/table.png");
	private static final ResourceLocation UP_ARROW = new ResourceLocation(Main.MODID, "textures/gui/up_arrow.png");
	private static final ResourceLocation DOWN_ARROW = new ResourceLocation(Main.MODID, "textures/gui/down_arrow.png");
	private static final ResourceLocation HOME_BUTTON = new ResourceLocation(Main.MODID,
			"textures/gui/home_button.png");
	private static final ResourceLocation MUTE_BUTTON = new ResourceLocation(Main.MODID,
			"textures/gui/mute_button.png");
	private static final ResourceLocation BACK_BUTTON = new ResourceLocation(Main.MODID,
			"textures/gui/back_button.png");
	private static final ResourceLocation EMPTY_BUTTON = new ResourceLocation(Main.MODID,
			"textures/gui/empty_button.png");
	private static final int X_SIZE = 230;
	private static final int Y_SIZE = 230;
	private static final int TEX_SIZE = 256;
	private static final int Y_OFFSET = 25;
	private static final int X_OFFSET = 10;
	private static final int CHAPTER_WIDTH = X_SIZE - X_OFFSET * 2;
	private static final int CHAPTER_HEIGHT = Y_SIZE - Y_OFFSET * 2;
	private static final int SCROLL_LENGTH = (int) (CHAPTER_HEIGHT * 0.3);
	private static final int BUTTON_SIZE = 32;

	private GuideChapter current;
	private int page = 0;
	private boolean canPageDown;
	private final GuideChapter startChapter;
	private int left, top;
	private String guideId;
	private boolean mute;
	private int[] bookmarks;
	private Map<Integer, GuideChapter> chapters;

	public GuideScreen(GuideChapter startChapter) {
		this(startChapter, GuideMessage.DUMMY);
	}

	public GuideScreen(GuideChapter startChapter, GuideMessage message) {
		super(startChapter.getTitle());
		this.startChapter = startChapter;
		this.current = startChapter;
		this.guideId = message.getId();
		this.mute = message.isMute();
		this.bookmarks = message.getBookmarks();
		this.initChapters();
	}

	private void initChapters() {
		chapters = new HashMap<>();
		int i = 1;

		var list = new LinkedList<GuideChapter>();
		list.add(startChapter);
		while (!list.isEmpty()) {
			var chapter = list.pop();
			chapters.put(i, chapter);
			chapter.setId(i);
			i++;
			for (var child : chapter.getChildren())
				list.add(child);
		}
	}

	@Override
	public void onClose() {
		super.onClose();

		if (guideId != null) {
			bookmarks[GuideData.BOOKMARK_START] = current.getId();
			Network.INSTANCE.sendToServer(new GuideMessage(guideId, mute, bookmarks));
		}
	}

	private void gotoChapter(GuideChapter chapter) {
		current = chapter;
		page = 0;
	}

	private void goBack() {
		if (current != null && current.getParent() != null)
			gotoChapter(current.getParent());
	}

	private GuideChapter getBookmark(int i) {
		int id = bookmarks[i];
		if (!chapters.containsKey(id)) {
			bookmarks[i] = 0;
			return startChapter;
		}

		return chapters.get(id);
	}

	@Override
	protected void init() {
		left = (width - X_SIZE) / 2;
		top = (height - Y_SIZE) / 2;
		int x = left + X_SIZE;
		int y = top + Y_SIZE / 2;
		addRenderableWidget(new GuideButton(x, y - BUTTON_SIZE - BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32,
				UP_ARROW, b -> page = Math.max(0, page - 1)) {
		});
		addRenderableWidget(
				new GuideButton(x, y + BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32, DOWN_ARROW, b -> {
					if (canPageDown)
						page++;
				}));
		addRenderableWidget(new GuideButton(x + 2, y - Y_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32, HOME_BUTTON, 256,
				256, b -> gotoChapter(startChapter), (b, m, mouseX, mouseY) -> renderComponentTooltip(m,
						Arrays.asList(b.getMessage()), mouseX, mouseY, minecraft.font),
				new TranslatableComponent("gui." + Main.MODID + ".home")));

		var muteTooltip = new TranslatableComponent("gui." + Main.MODID + ".mute");
		var unmuteTooltip = new TranslatableComponent("gui." + Main.MODID + ".unmute");
		addRenderableWidget(new GuideButton(x + 2, top + Y_SIZE - BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32,
				MUTE_BUTTON, 256, 256, b -> {
					mute = !mute;
					b.setMessage(mute ? unmuteTooltip : muteTooltip);
				}, (b, m, mouseX, mouseY) -> renderComponentTooltip(m, Arrays.asList(b.getMessage()), mouseX, mouseY,
						minecraft.font),
				muteTooltip));

		addRenderableWidget(new GuideButton(left - 2 - BUTTON_SIZE, top + Y_SIZE - BUTTON_SIZE, BUTTON_SIZE,
				BUTTON_SIZE, 0, 0, 32, BACK_BUTTON, 256, 256, b -> {
					goBack();
				}, (b, m, mouseX, mouseY) -> renderComponentTooltip(m, Arrays.asList(b.getMessage()), mouseX, mouseY,
						minecraft.font),
				new TranslatableComponent("gui." + Main.MODID + ".back")));

		// Bookmark buttons
		for (int i = 1; i < bookmarks.length; i++) {
			addRenderableWidget(new BookmarkButton(i, left - 2 - BUTTON_SIZE, top + (BUTTON_SIZE + 2) * (i - 1),
					BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32, EMPTY_BUTTON, 256, 256));
		}

		gotoChapter(getBookmark(GuideData.BOOKMARK_START));
	}

	private Component bookmarkText(int index) {
		var chapter = getBookmark(index);
		if (chapter == startChapter) {
			return new TranslatableComponent("gui." + Main.MODID + ".empty_bookmark");
		} else {
			return chapter.getPath();
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		var mouseKey = InputConstants.getKey(keyCode, scanCode);
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
			onClose();
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) {
			return true;
		} else if (current.mouseClicked(left + X_OFFSET, top + Y_OFFSET - SCROLL_LENGTH * page, top + Y_OFFSET,
				CHAPTER_WIDTH, CHAPTER_HEIGHT, mouseX, mouseY, button, chapter -> {
					current = chapter;
					if (!mute)
						Minecraft.getInstance().getSoundManager()
								.play(SimpleSoundInstance.forUI(ModSounds.GUIDE_CLICK.get(), 1.0F));
				})) {
			return true;
		} else if (button == InputConstants.MOUSE_BUTTON_RIGHT) {
			goBack();
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
		if (super.mouseScrolled(pMouseX, pMouseY, pDelta)) {
			return true;
		}

		pDelta = Mth.clamp(pDelta, -1, 1);

		if (pDelta < 0 && canPageDown)
			page++;
		else if (pDelta > 0 && page != 0)
			page--;

		return true;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);

		blit(poseStack, left, top, X_SIZE, Y_SIZE, 0, 0, TEX_SIZE, TEX_SIZE, TEX_SIZE, TEX_SIZE);

		int x = left + X_OFFSET;
		int y = top + Y_OFFSET;

		current.renderTitle(poseStack, minecraft, x, top + 5, CHAPTER_WIDTH, CHAPTER_HEIGHT);
		y -= SCROLL_LENGTH * page;
		y = current.renderComponents(poseStack, minecraft, x, y, top + Y_OFFSET, CHAPTER_WIDTH, CHAPTER_HEIGHT, mouseX,
				mouseY);
		canPageDown = y > top + CHAPTER_HEIGHT;

		super.render(poseStack, mouseX, mouseY, partialTicks);
	}

	private class GuideButton extends ImageButton {

		public GuideButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yHoverOffset,
				ResourceLocation image, int imgWidth, int imgHeight, OnPress action, OnTooltip tooltip,
				Component message) {
			super(x, y, width, height, xTexStart, yTexStart, yHoverOffset, image, imgWidth, imgHeight, action, tooltip,
					message);
		}

		public GuideButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yHoverOffset,
				ResourceLocation image, OnPress action) {
			super(x, y, width, height, xTexStart, yTexStart, yHoverOffset, image, action);
		}

		@Override
		public void playDownSound(SoundManager handler) {
			if (!mute)
				handler.play(SimpleSoundInstance.forUI(ModSounds.GUIDE_CLICK.get(), 1.0F));
		}
	}

	private class BookmarkButton extends GuideButton {

		private final int bookmarkIndex;

		public BookmarkButton(int bookmarkIndex, int x, int y, int width, int height, int xTexStart, int yTexStart,
				int yHoverOffset, ResourceLocation image, int imgWidth, int imgHeight) {
			super(x, y, width, height, xTexStart, yTexStart, yHoverOffset, image, imgWidth, imgHeight,
					b -> ((BookmarkButton) b).clickAction(),
					(b, p, mouseX, mouseY) -> ((BookmarkButton) b).hoverAction(p, mouseX, mouseY),
					bookmarkText(bookmarkIndex));
			this.bookmarkIndex = bookmarkIndex;
		}

		private void clickAction() {
			var chapter = getBookmark(bookmarkIndex);
			if (chapter != startChapter) {
				gotoChapter(chapter);
			} else if (current != startChapter) {
				bookmarks[bookmarkIndex] = current.getId();
				setMessage(bookmarkText(bookmarkIndex));
			}
		}

		private void hoverAction(PoseStack poseStack, int mouseX, int mouseY) {
			renderComponentTooltip(poseStack, Arrays.asList(getMessage()), mouseX, mouseY, minecraft.font);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (clicked(mouseX, mouseY) && button == InputConstants.MOUSE_BUTTON_RIGHT) {
				bookmarks[bookmarkIndex] = 0;
				setMessage(bookmarkText(bookmarkIndex));
				playDownSound(minecraft.getSoundManager());
				return true;
			}
			return super.mouseClicked(mouseX, mouseY, button);
		}

		@Override
		public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
			super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
			var chapter = getBookmark(bookmarkIndex);

			if (chapter != startChapter) {
				chapter.renderIcon(pPoseStack, minecraft, x + (BUTTON_SIZE - GuideChapter.ICON_SIZE) / 2,
						y + (BUTTON_SIZE - GuideChapter.ICON_SIZE) / 2, 0, 0, pMouseX, pMouseY, false);
			}
		}

	}
}
