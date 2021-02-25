package mod.vemerion.runesword.guide;

import mod.vemerion.runesword.api.IGuide;
import mod.vemerion.runesword.api.IGuideChapter;
import mod.vemerion.runesword.screen.GuideScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class Guide implements IGuide {

	@Override
	public void openGuide(IGuideChapter startChapter) {
		GuideChapter.throwIfInvalidChapter(startChapter);

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OpenGui.open((GuideChapter) startChapter));
	}

	@Override
	public IGuideChapter createGuideChapter(IItemProvider icon, ITextComponent title) {
		return new GuideChapter(icon, title);
	}

	@Override
	public IGuideChapter createGuideChapter(ResourceLocation icon, ITextComponent title) {
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
						Minecraft.getInstance().displayGuiScreen(new GuideScreen(startChapter));
				}
			};
		}
	}

}
