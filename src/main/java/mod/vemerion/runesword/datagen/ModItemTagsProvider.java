package mod.vemerion.runesword.datagen;

import java.util.concurrent.CompletableFuture;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			TagsProvider<Block> blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTagProvider, Main.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		var runes = ItemTags.create(new ResourceLocation(Main.MODID, "runes"));
		for (Item rune : RuneItem.getRunes())
			tag(runes).add(rune);

		var runeableSwords = Main.RUNEABLE_SWORDS;
		var runeableAxes = Main.RUNEABLE_AXES;

		addRuneable(Items.STONE_SWORD, runeableSwords, Main.RUNE_TIER_1);
		addRuneable(Items.IRON_SWORD, runeableSwords, Main.RUNE_TIER_2);
		addRuneable(Items.DIAMOND_SWORD, runeableSwords, Main.RUNE_TIER_3);
		addRuneable(Items.NETHERITE_SWORD, runeableSwords, Main.RUNE_TIER_4);

		addRuneable(Items.STONE_AXE, runeableAxes, Main.RUNE_TIER_1);
		addRuneable(Items.IRON_AXE, runeableAxes, Main.RUNE_TIER_2);
		addRuneable(Items.DIAMOND_AXE, runeableAxes, Main.RUNE_TIER_3);
		addRuneable(Items.NETHERITE_AXE, runeableAxes, Main.RUNE_TIER_4);
	}

	private void addRuneable(Item item, TagKey<Item> runeable, TagKey<Item> tier) {
		tag(runeable).add(item);
		tag(tier).add(item);
	}
}
