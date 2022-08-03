package mod.vemerion.runesword.guide;

import java.util.HashMap;
import java.util.Map;

import mod.vemerion.runesword.api.IGuide;
import mod.vemerion.runesword.api.IGuideChapter;
import mod.vemerion.runesword.capability.GuideData;
import mod.vemerion.runesword.screen.GuideScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class Guide implements IGuide {
	
	private Map<String, IGuideChapter> guides = new HashMap<>();

	@Override
	public void openGuide(IGuideChapter startChapter) {
		GuideChapter.throwIfInvalidChapter(startChapter);

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OpenGui.open((GuideChapter) startChapter));
	}
	
	@Override
	public void registerGuide(String id, IGuideChapter startChapter) {
		GuideChapter.throwIfInvalidChapter(startChapter);
		guides.put(id, startChapter);
	}
	
	@Override
	public void openGuide(Player player, String id) {
		GuideData.openGuide(player, id);
	}
	
	@Override
	public IGuideChapter getGuide(String id) {
		return guides.get(id);
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
					var mc = Minecraft.getInstance();
					if (mc != null)
						Minecraft.getInstance().setScreen(new GuideScreen(startChapter));
				}
			};
		}
	}

}
