package mod.vemerion.runesword.api;

import net.minecraft.util.ResourceLocation;

public interface IGuideChapter {

	IGuideChapter addChild(IGuideChapter child);

	IGuideChapter addText(String translationKey);

	IGuideChapter addHeader(String translationKey);

	IGuideChapter addImage(ResourceLocation image, int imgWidth, int imgHeight);

}
