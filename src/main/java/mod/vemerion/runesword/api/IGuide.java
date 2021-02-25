package mod.vemerion.runesword.api;

import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public interface IGuide {

	void openGuide(IGuideChapter startChapter);

	IGuideChapter createGuideChapter(IItemProvider icon, ITextComponent title);

	IGuideChapter createGuideChapter(ResourceLocation icon, ITextComponent title);
}
