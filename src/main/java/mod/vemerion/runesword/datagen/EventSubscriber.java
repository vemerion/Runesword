package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.api.RuneswordAPI;
import mod.vemerion.runesword.guide.GuideChapters;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.MOD, modid = Main.MODID)
public class EventSubscriber {

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		var generator = event.getGenerator();

		var existingFileHelper = event.getExistingFileHelper();

		var packOutput = generator.getPackOutput();

		var blockTagsProvider = new BlockTagsProvider(packOutput, event.getLookupProvider(), Main.MODID,
				existingFileHelper) {
			@Override
			protected void addTags(Provider pProvider) {
			}
		};
		generator.addProvider(event.includeServer(),
				new ModItemTagsProvider(packOutput, event.getLookupProvider(), blockTagsProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
		generator.addProvider(event.includeServer(), new ModLootModifierProvider(packOutput));

		generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput));
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModSoundProvider(packOutput, existingFileHelper));
		generator.addProvider(event.includeClient(),
				RuneswordAPI.guide.guideProvider(generator, Main.MODID, "guide", GuideChapters.getStartChapter()));
	}
}
