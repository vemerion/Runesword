package mod.vemerion.runesword.init;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.AirRuneItem;
import mod.vemerion.runesword.item.BloodRuneItem;
import mod.vemerion.runesword.item.EarthRuneItem;
import mod.vemerion.runesword.item.FireRuneItem;
import mod.vemerion.runesword.item.FrostRuneItem;
import mod.vemerion.runesword.item.GuideItem;
import mod.vemerion.runesword.item.MagicRuneItem;
import mod.vemerion.runesword.item.WaterRuneItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(bus = Bus.MOD, modid = Main.MODID)
public class ModItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

	public static final RegistryObject<Item> RUNEFORGE = ITEMS.register("runeforge",
			() -> new BlockItem(ModBlocks.RUNEFORGE.get(), new Item.Properties()));

	public static final RegistryObject<Item> GUIDE = ITEMS.register("guide",
			() -> new GuideItem(new Item.Properties()));

	public static final RegistryObject<Item> FIRE_RUNE = ITEMS.register("fire_rune",
			() -> new FireRuneItem(new Item.Properties()));

	public static final RegistryObject<Item> WATER_RUNE = ITEMS.register("water_rune",
			() -> new WaterRuneItem(new Item.Properties()));

	public static final RegistryObject<Item> EARTH_RUNE = ITEMS.register("earth_rune",
			() -> new EarthRuneItem(new Item.Properties()));

	public static final RegistryObject<Item> AIR_RUNE = ITEMS.register("air_rune",
			() -> new AirRuneItem(new Item.Properties()));

	public static final RegistryObject<Item> BLOOD_RUNE = ITEMS.register("blood_rune",
			() -> new BloodRuneItem(new Item.Properties()));

	public static final RegistryObject<Item> FROST_RUNE = ITEMS.register("frost_rune",
			() -> new FrostRuneItem(new Item.Properties()));

	public static final RegistryObject<Item> MAGIC_RUNE = ITEMS.register("magic_rune",
			() -> new MagicRuneItem(new Item.Properties()));

	@SubscribeEvent
	public static void onRegisterTabs(CreativeModeTabEvent.Register event) {
		event.registerCreativeModeTab(new ResourceLocation(Main.MODID, "tab"),
				builder -> builder.title(Component.translatable("itemGroup." + Main.MODID))
						.icon(() -> ModItems.FIRE_RUNE.get().getDefaultInstance())
						.displayItems((features, output, operator) -> {
							for (var item : ITEMS.getEntries())
								output.accept(item.get().getDefaultInstance());
						}));
	}
}
