package mod.vemerion.runesword;

import com.mojang.serialization.Codec;

import mod.vemerion.runesword.api.RuneswordAPI;
import mod.vemerion.runesword.block.RuneforgeBlock;
import mod.vemerion.runesword.capability.EntityRuneData;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.container.RuneforgeContainer;
import mod.vemerion.runesword.effect.BleedEffect;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import mod.vemerion.runesword.entity.MagicBallEntity;
import mod.vemerion.runesword.guide.Guide;
import mod.vemerion.runesword.item.AirRuneItem;
import mod.vemerion.runesword.item.BloodRuneItem;
import mod.vemerion.runesword.item.EarthRuneItem;
import mod.vemerion.runesword.item.FireRuneItem;
import mod.vemerion.runesword.item.FrostRuneItem;
import mod.vemerion.runesword.item.GuideItem;
import mod.vemerion.runesword.item.MagicRuneItem;
import mod.vemerion.runesword.item.RuneItem;
import mod.vemerion.runesword.item.WaterRuneItem;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import mod.vemerion.runesword.lootmodifier.lootcondition.LootConditions;
import mod.vemerion.runesword.network.AxeMagicPowersMessage;
import mod.vemerion.runesword.network.Network;
import mod.vemerion.runesword.network.SyncBleedingMessage;
import mod.vemerion.runesword.particle.MagicBallParticleData;
import mod.vemerion.runesword.tileentity.RuneforgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.MOD)
public class ModEventSubscriber {

	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		event.getRegistry()
				.register(setup(new BlockItem(Main.RUNEFORGE_BLOCK, new Item.Properties().group(ItemGroup.SEARCH)),
						"runeforge_block_item"));
		event.getRegistry().register(setup(new GuideItem(new Item.Properties().group(ItemGroup.SEARCH)), "guide_item"));

		Item fireRune = new FireRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item waterRune = new WaterRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item earthRune = new EarthRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item airRune = new AirRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item bloodRune = new BloodRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item frostRune = new FrostRuneItem(new Item.Properties().group(ItemGroup.SEARCH));
		Item magicRune = new MagicRuneItem(new Item.Properties().group(ItemGroup.SEARCH));

		event.getRegistry().registerAll(setup(fireRune, "fire_rune_item"), setup(waterRune, "water_rune_item"),
				setup(earthRune, "earth_rune_item"), setup(airRune, "air_rune_item"),
				setup(bloodRune, "blood_rune_item"), setup(frostRune, "frost_rune_item"),
				setup(magicRune, "magic_rune_item"));
	}

	@SubscribeEvent
	public static void onRegisterSound(RegistryEvent.Register<SoundEvent> event) {
		SoundEvent guide_click = new SoundEvent(new ResourceLocation(Main.MODID, "guide_click"));
		event.getRegistry().register(setup(guide_click, "guide_click"));
		SoundEvent projectile_impact_sound = new SoundEvent(
				new ResourceLocation(Main.MODID, "projectile_impact_sound"));
		event.getRegistry().register(setup(projectile_impact_sound, "projectile_impact_sound"));
		SoundEvent projectile_launch_sound = new SoundEvent(
				new ResourceLocation(Main.MODID, "projectile_launch_sound"));
		event.getRegistry().register(setup(projectile_launch_sound, "projectile_launch_sound"));
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

		EntityType<MagicBallEntity> magicBallEntityType = EntityType.Builder
				.<MagicBallEntity>create(MagicBallEntity::new, EntityClassification.MISC).size(0.5f, 0.5f)
				.trackingRange(4).func_233608_b_(20)
				.build(new ResourceLocation(Main.MODID, "magic_ball_entity").toString());
		event.getRegistry().register(setup(magicBallEntityType, "magic_ball_entity"));

	}

	@SubscribeEvent
	public static void onRegisterParticle(RegistryEvent.Register<ParticleType<?>> event) {
		event.getRegistry().register(
				setup(new ParticleType<MagicBallParticleData>(true, new MagicBallParticleData.Deserializer()) {

					@Override
					public Codec<MagicBallParticleData> func_230522_e_() {
						return MagicBallParticleData.CODEC;
					}
				}, "magic_ball_particle"));

		event.getRegistry().register(setup(new BasicParticleType(true), "bleed_particle"));
	}

	@SubscribeEvent
	public static void onRegisterLootModifier(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
		LootConditions.register();
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
	public static void onRegisterEffect(RegistryEvent.Register<Effect> event) {
		event.getRegistry().register(setup(new BleedEffect(), "bleed_effect"));
	}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		RuneswordAPI.guide = new Guide();

		CapabilityManager.INSTANCE.register(Runes.class, new Runes.Storage(), Runes::new);
		CapabilityManager.INSTANCE.register(EntityRuneData.class, new EntityRuneData.Storage(), EntityRuneData::new);

		Network.INSTANCE.registerMessage(0, AxeMagicPowersMessage.class, AxeMagicPowersMessage::encode,
				AxeMagicPowersMessage::decode, AxeMagicPowersMessage::handle);
		Network.INSTANCE.registerMessage(1, SyncBleedingMessage.class, SyncBleedingMessage::encode,
				SyncBleedingMessage::decode, SyncBleedingMessage::handle);
	}

	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(Main.FROST_GOLEM_ENTITY, SnowGolemEntity.func_234226_m_().create());
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(Main.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}
}
