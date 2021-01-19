package mod.vemerion.runesword;

import mod.vemerion.runesword.block.RuneforgeBlock;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.container.RuneforgeContainer;
import mod.vemerion.runesword.datagen.ModItemTagsProvider;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import mod.vemerion.runesword.item.AirRuneItem;
import mod.vemerion.runesword.item.BloodRuneItem;
import mod.vemerion.runesword.item.EarthRuneItem;
import mod.vemerion.runesword.item.FireRuneItem;
import mod.vemerion.runesword.item.FrostRuneItem;
import mod.vemerion.runesword.item.RuneItem;
import mod.vemerion.runesword.item.WaterRuneItem;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import mod.vemerion.runesword.lootmodifier.lootcondition.LootConditions;
import mod.vemerion.runesword.network.Network;
import mod.vemerion.runesword.network.SyncRunesMessage;
import mod.vemerion.runesword.tileentity.RuneforgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class ModEventSubscriber {

	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		event.getRegistry()
				.register(setup(new BlockItem(Main.RUNEFORGE_BLOCK, new Item.Properties().group(ItemGroup.SEARCH)),
						"runeforge_block_item"));

		Item fireRune = new FireRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item waterRune = new WaterRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item earthRune = new EarthRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item airRune = new AirRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item bloodRune = new BloodRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item frostRune = new FrostRuneItem(new Item.Properties().group(ItemGroup.SEARCH));

		event.getRegistry().registerAll(setup(fireRune, "fire_rune_item"), setup(waterRune, "water_rune_item"),
				setup(earthRune, "earth_rune_item"), setup(airRune, "air_rune_item"),
				setup(bloodRune, "blood_rune_item"), setup(frostRune, "frost_rune_item"));
	}

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
		event.getRegistry()
				.register(setup(
						new RuneforgeBlock(
								Block.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(2, 6)),
						"runeforge_block"));
	}

	@SubscribeEvent
	public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
		TileEntityType<RuneforgeTileEntity> runeforgeTileEntityType = TileEntityType.Builder
				.<RuneforgeTileEntity>create(() -> new RuneforgeTileEntity(), Main.RUNEFORGE_BLOCK).build(null);

		event.getRegistry().register(setup(runeforgeTileEntityType, "runeforge_tile_entity"));

	}

	@SubscribeEvent
	public static void onRegisterContainer(RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(setup(IForgeContainerType.create(RuneforgeContainer::new), "runeforge_container"));
	}

	@SubscribeEvent
	public static void onRegisterEntity(RegistryEvent.Register<EntityType<?>> event) {
		EntityType<FrostGolemEntity> frostGolemEntityType = EntityType.Builder
				.<FrostGolemEntity>create(FrostGolemEntity::new, EntityClassification.MISC).size(0.7F, 1.9F)
				.trackingRange(8).build(new ResourceLocation(Main.MODID, "frost_golem_entity").toString());
		event.getRegistry().register(setup(frostGolemEntityType, "frost_golem_entity"));

		EntityType<FrostballEntity> frostballEntityType = EntityType.Builder
				.<FrostballEntity>create(FrostballEntity::new, EntityClassification.MISC).size(0.25F, 0.25F)
				.trackingRange(4).func_233608_b_(10)
				.build(new ResourceLocation(Main.MODID, "frostball_entity").toString());
		event.getRegistry().register(setup(frostballEntityType, "frostball_entity"));

	}

	@SubscribeEvent
	public static void onRegisterLootModifier(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
		event.getRegistry()
				.register(setup(new RuneLootModifier.Serializer(RuneItem.FIRE_RUNE_ITEM), "fire_rune_loot_modifier"));
		event.getRegistry()
				.register(setup(new RuneLootModifier.Serializer(RuneItem.WATER_RUNE_ITEM), "water_rune_loot_modifier"));
		event.getRegistry()
				.register(setup(new RuneLootModifier.Serializer(RuneItem.EARTH_RUNE_ITEM), "earth_rune_loot_modifier"));
		event.getRegistry()
				.register(setup(new RuneLootModifier.Serializer(RuneItem.BLOOD_RUNE_ITEM), "blood_rune_loot_modifier"));
		event.getRegistry()
				.register(setup(new RuneLootModifier.Serializer(RuneItem.AIR_RUNE_ITEM), "air_rune_loot_modifier"));
	}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();

		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			BlockTagsProvider blockTagsProvider = new BlockTagsProvider(generator, Main.MODID, existingFileHelper);
			generator.addProvider(new ModItemTagsProvider(generator, blockTagsProvider, existingFileHelper));
		}
	}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(Runes.class, new Runes.Storage(), Runes::new);

		Network.INSTANCE.registerMessage(0, SyncRunesMessage.class, SyncRunesMessage::encode, SyncRunesMessage::decode,
				SyncRunesMessage::handle);

		event.enqueueWork(() -> {
			LootConditions.register();
			registerEntityAttributes();
		});

	}

	private static void registerEntityAttributes() {
		GlobalEntityTypeAttributes.put(Main.FROST_GOLEM_ENTITY, SnowGolemEntity.func_234226_m_().create());
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(Main.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}
}
