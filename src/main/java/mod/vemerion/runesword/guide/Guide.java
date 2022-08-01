package mod.vemerion.runesword.guide;

import mod.vemerion.runesword.api.IGuide;
import mod.vemerion.runesword.api.IGuideChapter;
import mod.vemerion.runesword.screen.GuideScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class Guide implements IGuide {

	@Override
	public void openGuide(IGuideChapter startChapter) {
		GuideChapter.throwIfInvalidChapter(startChapter);

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OpenGui.open((GuideChapter) startChapter));
	}

	@Override
	public IGuideChapter createGuideChapter(ItemLike icon, Component title) {
		return new GuideChapter(icon, title);
	}

	@Override
	public IGuideChapter createGuideChapter(ResourceLocation icon, Component title) {
		return new GuideChapter(icon, title);
	}

	private static class OpenGui {
		private static DistExecutor.SafeRunnable open(GuideChapter startChapter) {
			return new DistExecutor.SafeRunnable() {
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					Minecraft mc = Minecraft.getInstance();
					if (mc != null)
						Minecraft.getInstance().setScreen(new GuideScreen(startChapter));
				}
			};
		}
	}

}
