package mod.vemerion.runesword.guide;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.api.IGuideChapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class GuideChapter implements IGuideChapter {

	public static final int ICON_SIZE = 16;
	private static final int COMPONENT_PADDING = 6;

	private static final Component SEPARATOR = new TranslatableComponent("gui." + Main.MODID + ".separator");

	private ItemLike itemIcon;
	private ResourceLocation rlIcon;
	private HeaderComponent title;
	private GuideChapter parent;
	private int id;
	private List<GuideChapter> children = new ArrayList<>();
	private List<ChapterComponent> components = new ArrayList<>();

	public GuideChapter(ItemLike icon, Component title) {
		this.itemIcon = icon;
		this.title = new HeaderComponent(title);
	}

	public GuideChapter(ResourceLocation icon, Component title) {
		this.rlIcon = icon;
		this.title = new HeaderComponent(title);
	}

	public static void throwIfInvalidChapter(IGuideChapter chapter) {
		if (!(chapter instanceof GuideChapter))
			throw new IllegalArgumentException("openGuide() parameter is not of type GuideChapter");
	}

	@Override
	public GuideChapter addChild(IGuideChapter child) {
		throwIfInvalidChapter(child);
		var validChild = (GuideChapter) child;
		children.add(validChild);
		validChild.addParent(this);
		return this;
	}

	@Override
	public GuideChapter addText(String translationKey) {
		components.add(new ParagraphComponent(new TranslatableComponent(translationKey)));
		return this;
	}

	@Override
	public GuideChapter addHeader(String translationKey) {
		components.add(new HeaderComponent(new TranslatableComponent(translationKey)));
		return this;
	}

	@Override
	public GuideChapter addImage(ResourceLocation image, int imgWidth, int imgHeight) {
		components.add(new ImageComponent(image, imgWidth, imgHeight));
		return this;
	}

	private void addParent(GuideChapter parent) {
		if (this.parent != null)
			throw new IllegalArgumentException("GuidebookChapter is only allowed to have one parent!");
		this.parent = parent;
	}

	public GuideChapter getParent() {
		return parent;
	}

	public List<GuideChapter> getChildren() {
		return children;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Component getTitle() {
		return title.text;
	}

	public Component getPath() {
		if (parent == null)
			return getTitle();

		var chapters = new LinkedList<GuideChapter>();
		chapters.push(this);
		while (chapters.peek().parent != null)
			chapters.push(chapters.peek().parent);

		var path = chapters.pop().getTitle().copy();
		while (!chapters.isEmpty()) {
			path.append(SEPARATOR);
			path.append(chapters.pop().getTitle());
		}

		return path;
	}

	public void renderTitle(PoseStack poseStack, Minecraft mc, int x, int y, int width, int height) {
		title.render(poseStack, mc, x, y, y, width, height);
	}

	public boolean mouseClicked(int x, int y, int top, int width, int height, double mouseX, double mouseY, int button,
			Consumer<GuideChapter> changeChapter) {
		int left = x;
		for (int i = 0; i < children.size(); i++) {
			if (isInside(x, y, ICON_SIZE, mouseX, mouseY) && button == InputConstants.MOUSE_BUTTON_LEFT
					&& isInsideScreen(y, ICON_SIZE, top, height)) {
				changeChapter.accept(children.get(i));
				return true;
			}
			x += ICON_SIZE;
			if (x + ICON_SIZE > left + width && i + 1 != children.size()) {
				x = left;
				y += ICON_SIZE;
			}
		}
		return false;
	}

	public int renderComponents(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height,
			int mouseX, int mouseY) {
		int left = x;
		for (int i = 0; i < children.size(); i++) {
			GuideChapter c = children.get(i);
			if (isInsideScreen(y, ICON_SIZE, top, height))
				c.renderIcon(poseStack, mc, x, y, width, height, mouseX, mouseY, true);
			x += ICON_SIZE;
			if (x + ICON_SIZE > left + width && i + 1 != children.size()) {
				x = left;
				y += ICON_SIZE;
			}
		}

		x = left;
		y += children.isEmpty() ? COMPONENT_PADDING : ICON_SIZE + COMPONENT_PADDING;
		for (ChapterComponent comp : components) {
			y = comp.render(poseStack, mc, x, y, top, width, height) + COMPONENT_PADDING;
		}
		return y;
	}

	public void renderIcon(PoseStack poseStack, Minecraft mc, int x, int y, int width, int height, int mouseX,
			int mouseY, boolean hoverText) {
		if (hoverText && isInside(x, y, ICON_SIZE, mouseX, mouseY)) {
			mc.screen.renderTooltip(poseStack, title.text, mouseX, mouseY);
		}

		if (itemIcon != null) {
			mc.getItemRenderer().renderAndDecorateItem(new ItemStack(itemIcon), x, y);
		} else {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShaderTexture(0, rlIcon);
			Screen.blit(poseStack, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
		}
	}

	private boolean isInside(int left, int top, int size, double x, double y) {
		return x > left && x < left + size && y > top && y < top + size;
	}

	private static boolean isInsideScreen(int y, int size, int top, int height) {
		return y >= top && y + size < top + height;
	}

	public JsonObject write() {
		JsonObject json = new JsonObject();

		if (itemIcon != null)
			json.addProperty("item_icon", itemIcon.asItem().getRegistryName().toString());
		else {
			json.addProperty("image_icon", rlIcon.toString());
		}

		json.add("title", title.write());

		JsonArray compontentArray = new JsonArray();

		for (var component : components)
			compontentArray.add(component.write());

		json.add("components", compontentArray);

		JsonArray childArray = new JsonArray();
		for (var child : children)
			childArray.add(child.write());

		json.add("children", childArray);

		return json;
	}

	public static GuideChapter read(JsonObject json) {
		GuideChapter result = null;
		Component titleComp = null;
		if (ChapterComponent.read(GsonHelper.getAsJsonObject(json, "title")) instanceof HeaderComponent header)
			titleComp = header.text;
		else
			throw new JsonParseException("Guide chapter has invalid title type");

		if (json.has("item_icon"))
			result = new GuideChapter(GsonHelper.getAsItem(json, "item_icon"), titleComp);
		else if (json.has("image_icon"))
			result = new GuideChapter(new ResourceLocation(GsonHelper.getAsString(json, "image_icon")), titleComp);
		else
			throw new JsonParseException("Guide chapter must have an icon");

		for (var jsonComp : GsonHelper.getAsJsonArray(json, "components")) {
			var comp = ChapterComponent.read(GsonHelper.convertToJsonObject(jsonComp, "component"));
			if (comp == null)
				throw new JsonParseException("Guide chapter has an unknown component type");
			result.components.add(comp);
		}

		for (var child : GsonHelper.getAsJsonArray(json, "children"))
			result.addChild(read(GsonHelper.convertToJsonObject(child, "child")));

		return result;
	}

	private static interface ChapterComponent {
		int render(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height);

		JsonObject write();

		static ChapterComponent read(JsonObject json) {
			switch (GsonHelper.getAsString(json, "type")) {
			case "paragraph":
				return new ParagraphComponent(new TranslatableComponent(GsonHelper.getAsString(json, "key")));
			case "header":
				return new HeaderComponent(
						json.has("key") ? new TranslatableComponent(GsonHelper.getAsString(json, "key"))
								: new TextComponent(GsonHelper.getAsString(json, "string")));
			case "image":
				return new ImageComponent(new ResourceLocation(GsonHelper.getAsString(json, "key")),
						GsonHelper.getAsInt(json, "width"), GsonHelper.getAsInt(json, "height"));
			}
			return null;
		}
	}

	private static class ParagraphComponent implements ChapterComponent {

		private static final int COLOR = Color.BLACK.getRGB();

		private TranslatableComponent text;

		private ParagraphComponent(TranslatableComponent text) {
			this.text = text;
		}

		@Override
		public int render(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height) {
			var font = mc.font;
			var lines = font.split(text, width);
			for (var line : lines) {
				if (isInsideScreen(y, font.lineHeight, top, height))
					font.draw(poseStack, line, x, y, COLOR);
				y += font.lineHeight;
			}
			return y;
		}

		@Override
		public JsonObject write() {
			JsonObject json = new JsonObject();
			json.addProperty("type", "paragraph");
			json.addProperty("key", text.getKey());

			return json;
		}

	}

	private static class HeaderComponent implements ChapterComponent {

		private static final int COLOR = Color.BLACK.getRGB();
		private static final float SCALE = 1.3f;

		private Component text;

		private HeaderComponent(Component text) {
			this.text = text;
		}

		@Override
		public int render(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height) {
			var font = mc.font;

			var lines = font.split(text, (int) (width / SCALE));
			for (var line : lines) {
				float textWidth = font.width(line) * SCALE;
				poseStack.pushPose();
				poseStack.scale(SCALE, SCALE, 1);
				if (isInsideScreen(y, (int) (font.lineHeight * SCALE), top, height))
					font.draw(poseStack, line, (x + width / 2 - textWidth / 2) / SCALE, y / SCALE, COLOR);
				y += font.lineHeight * SCALE;
				poseStack.popPose();
			}
			return y + 2;
		}

		@Override
		public JsonObject write() {
			JsonObject json = new JsonObject();
			json.addProperty("type", "header");

			if (text instanceof TranslatableComponent TranslatableComp)
				json.addProperty("key", TranslatableComp.getKey());
			else
				json.addProperty("string", text.getContents());

			return json;
		}

	}

	private static class ImageComponent implements ChapterComponent {

		private static final float SCALE = 0.6f;

		private ResourceLocation image;
		private int imgWidth;
		private int imgHeight;

		private ImageComponent(ResourceLocation image, int imgWidth, int imgHeight) {
			this.image = image;
			this.imgWidth = imgWidth;
			this.imgHeight = imgHeight;
		}

		@Override
		public int render(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShaderTexture(0, image);
			float scale = (float) width / imgWidth * SCALE;
			int drawWidth = (int) (imgWidth * scale);
			int drawHeight = (int) (imgHeight * scale);
			int offset = drawHeight + 2;

			// Do not draw if outside guide screen
			if (y + drawHeight <= top || y >= top + height)
				return y + offset;

			int vHeight = imgHeight;
			int v = 0;
			int drawY = y;

			// Cut of image top
			if (y < top && y + drawHeight > top) {
				float ratio = (y + drawHeight - top) / (float) drawHeight;
				v += imgHeight * (1 - ratio);
				drawY += drawHeight * (1 - ratio);
				vHeight = (int) (ratio * vHeight);
				drawHeight = (int) (ratio * drawHeight);
			}

			// Cut of image bottom
			if (y + drawHeight > top + height) {
				float ratio = 1 - ((y + drawHeight - (top + height)) / (float) drawHeight);
				drawHeight = (int) (drawHeight * ratio);
				vHeight = (int) (vHeight * ratio);
			}
			Screen.blit(poseStack, x + width / 2 - drawWidth / 2, drawY, drawWidth, drawHeight, 0, v, imgWidth, vHeight,
					imgWidth, imgHeight);
			return y + offset;
		}

		@Override
		public JsonObject write() {
			JsonObject json = new JsonObject();
			json.addProperty("type", "image");
			json.addProperty("width", imgWidth);
			json.addProperty("height", imgHeight);
			json.addProperty("key", image.toString());

			return json;
		}

	}
}