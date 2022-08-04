package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.api.RuneswordAPI;
import mod.vemerion.runesword.guide.GuideChapters;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(bus = Bus.MOD, modid = Main.MODID)
public class EventSubscriber {

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();

		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			BlockTagsProvider blockTagsProvider = new BlockTagsProvider(generator, Main.MODID, existingFileHelper);
			generator.addProvider(new ModItemTagsProvider(generator, blockTagsProvider, existingFileHelper));
			generator.addProvider(new ModRecipeProvider(generator));
			generator.addProvider(new ModLootModifierProvider(generator));
		}
		if (event.includeClient()) {
			generator.addProvider(new ModLanguageProvider(generator));
			generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
			generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
			generator.addProvider(new ModSoundProvider(generator, existingFileHelper));
			generator.addProvider(
					RuneswordAPI.guide.guideProvider(generator, Main.MODID, "guide", GuideChapters.getStartChapter()));
		}
	}
}
