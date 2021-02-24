package mod.vemerion.runesword.datagen;

import java.util.function.Consumer;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(RuneItem.FROST_RUNE_ITEM).addIngredient(RuneItem.WATER_RUNE_ITEM)
				.addIngredient(RuneItem.AIR_RUNE_ITEM).addCriterion("has_water_rune", hasItem(RuneItem.WATER_RUNE_ITEM))
				.addCriterion("has_air_rune", hasItem(RuneItem.AIR_RUNE_ITEM)).build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(RuneItem.MAGIC_RUNE_ITEM).addIngredient(RuneItem.WATER_RUNE_ITEM)
				.addIngredient(RuneItem.AIR_RUNE_ITEM).addIngredient(RuneItem.EARTH_RUNE_ITEM)
				.addIngredient(RuneItem.FIRE_RUNE_ITEM).addIngredient(RuneItem.BLOOD_RUNE_ITEM)
				.addCriterion("has_blood_rune", hasItem(RuneItem.BLOOD_RUNE_ITEM)).build(consumer);

		ITag<Item> runes = ItemTags.createOptional(new ResourceLocation(Main.MODID, "runes"));
		ShapedRecipeBuilder.shapedRecipe(Main.RUNEFORGE_BLOCK).patternLine("sss").patternLine("srs").patternLine("sss")
				.key('s', ItemTags.STONE_CRAFTING_MATERIALS).key('r', runes).addCriterion("has_rune", hasItem(runes))
				.build(consumer);

		ShapelessRecipeBuilder.shapelessRecipe(Main.GUIDE_ITEM).addIngredient(runes)
				.addIngredient(Tags.Items.COBBLESTONE).addCriterion("has_rune", hasItem(runes)).build(consumer);
	}

}
