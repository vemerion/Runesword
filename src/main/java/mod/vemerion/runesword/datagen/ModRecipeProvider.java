package mod.vemerion.runesword.datagen;

import java.util.function.Consumer;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RuneItem.FROST_RUNE_ITEM).requires(RuneItem.WATER_RUNE_ITEM)
				.requires(RuneItem.AIR_RUNE_ITEM).unlockedBy("has_water_rune", has(RuneItem.WATER_RUNE_ITEM))
				.unlockedBy("has_air_rune", has(RuneItem.AIR_RUNE_ITEM)).save(consumer);

		ShapelessRecipeBuilder.shapeless(RuneItem.MAGIC_RUNE_ITEM).requires(RuneItem.WATER_RUNE_ITEM)
				.requires(RuneItem.AIR_RUNE_ITEM).requires(RuneItem.EARTH_RUNE_ITEM)
				.requires(RuneItem.FIRE_RUNE_ITEM).requires(RuneItem.BLOOD_RUNE_ITEM)
				.unlockedBy("has_blood_rune", has(RuneItem.BLOOD_RUNE_ITEM)).save(consumer);

		var runes = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Main.MODID, "runes"));
		ShapedRecipeBuilder.shaped(Main.RUNEFORGE_BLOCK).pattern("sss").pattern("srs").pattern("sss")
				.define('s', ItemTags.STONE_CRAFTING_MATERIALS).define('r', runes).unlockedBy("has_rune", has(runes))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(Main.GUIDE_ITEM).requires(runes)
				.requires(Tags.Items.COBBLESTONE).unlockedBy("has_rune", has(runes)).save(consumer);
	}

}
