package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider,
			ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagProvider, Main.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags() {
		IOptionalNamedTag<Item> runes = ItemTags.createOptional(new ResourceLocation(Main.MODID, "runes"));
		for (Item rune : RuneItem.getRunes())
			getOrCreateBuilder(runes).add(rune);
	}

}
