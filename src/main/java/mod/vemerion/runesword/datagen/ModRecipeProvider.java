package mod.vemerion.runesword.datagen;

import java.util.function.Consumer;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModBlocks;
import mod.vemerion.runesword.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(PackOutput pOutput) {
		super(pOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FROST_RUNE.get())
				.requires(ModItems.WATER_RUNE.get()).requires(ModItems.AIR_RUNE.get())
				.unlockedBy("has_water_rune", has(ModItems.WATER_RUNE.get()))
				.unlockedBy("has_air_rune", has(ModItems.AIR_RUNE.get())).save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MAGIC_RUNE.get())
				.requires(ModItems.WATER_RUNE.get()).requires(ModItems.AIR_RUNE.get())
				.requires(ModItems.EARTH_RUNE.get()).requires(ModItems.FIRE_RUNE.get())
				.requires(ModItems.BLOOD_RUNE.get()).unlockedBy("has_blood_rune", has(ModItems.BLOOD_RUNE.get()))
				.save(consumer);

		var runes = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation(Main.MODID, "runes"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RUNEFORGE.get()).pattern("sss").pattern("srs")
				.pattern("sss").define('s', ItemTags.STONE_CRAFTING_MATERIALS).define('r', runes)
				.unlockedBy("has_rune", has(runes)).save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GUIDE.get()).requires(runes)
				.requires(Tags.Items.COBBLESTONE).unlockedBy("has_rune", has(runes)).save(consumer);
	}

}
