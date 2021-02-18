package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

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
		}
	}
}
