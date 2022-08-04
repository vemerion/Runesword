package mod.vemerion.runesword;

import mod.vemerion.runesword.capability.EntityRuneData;
import mod.vemerion.runesword.capability.GuideData;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.init.ModEntities;
import mod.vemerion.runesword.network.AxeMagicPowersMessage;
import mod.vemerion.runesword.network.GuideMessage;
import mod.vemerion.runesword.network.Network;
import mod.vemerion.runesword.network.SyncBleedingMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class ModEventSubscriber {

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		Network.INSTANCE.registerMessage(0, AxeMagicPowersMessage.class, AxeMagicPowersMessage::encode,
				AxeMagicPowersMessage::decode, AxeMagicPowersMessage::handle);
		Network.INSTANCE.registerMessage(1, SyncBleedingMessage.class, SyncBleedingMessage::encode,
				SyncBleedingMessage::decode, SyncBleedingMessage::handle);
		Network.INSTANCE.registerMessage(2, GuideMessage.class, GuideMessage::encode,
				GuideMessage::decode, GuideMessage::handle);
	}

	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntities.FROST_GOLEM.get(), SnowGolem.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(Runes.class);
		event.register(EntityRuneData.class);
		event.register(GuideData.class);
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(Main.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}
}
