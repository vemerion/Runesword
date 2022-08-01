package mod.vemerion.runesword.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public interface IGuide {

	void openGuide(IGuideChapter startChapter);

	IGuideChapter createGuideChapter(ItemLike icon, Component title);

	IGuideChapter createGuideChapter(ResourceLocation icon, Component title);
}
