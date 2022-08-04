package mod.vemerion.runesword.guide;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mod.vemerion.runesword.api.IGuide;
import mod.vemerion.runesword.api.IGuideChapter;
import mod.vemerion.runesword.capability.GuideData;
import mod.vemerion.runesword.datagen.GuideProvider;
import mod.vemerion.runesword.screen.GuideScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class Guide implements IGuide {

	private static final Logger LOGGER = LogManager.getLogger();

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
		if (!guides.containsKey(id))
			LOGGER.warn("Unable to find guide '" + id + "'");

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

	@Override
	public DataProvider guideProvider(DataGenerator generator, String modid, String guideId,
			IGuideChapter startChapter) {
		GuideChapter.throwIfInvalidChapter(startChapter);
		return new GuideProvider(generator, modid, guideId, startChapter);
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
