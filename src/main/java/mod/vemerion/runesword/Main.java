package mod.vemerion.runesword;

import mod.vemerion.runesword.container.RuneforgeMenu;
import mod.vemerion.runesword.effect.BleedEffect;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import mod.vemerion.runesword.entity.MagicBallEntity;
import mod.vemerion.runesword.item.RuneswordItemGroup;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import mod.vemerion.runesword.particle.MagicBallParticleData;
import mod.vemerion.runesword.tileentity.RuneforgeBlockEntity;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Main.MODID)
public class Main {
	public static final String MODID = "runesword";

	@ObjectHolder(Main.MODID + ":runeforge_tile_entity")
	public static final BlockEntityType<RuneforgeBlockEntity> RUNEFORGE_BLOCK_ENTITY = null;

	@ObjectHolder(Main.MODID + ":runeforge_container")
	public static final MenuType<RuneforgeMenu> RUNEFORGE_MENU = null;

	@ObjectHolder(Main.MODID + ":runeforge_block")
	public static final Block RUNEFORGE_BLOCK = null;

	@ObjectHolder(Main.MODID + ":frost_golem_entity")
	public static final EntityType<FrostGolemEntity> FROST_GOLEM_ENTITY = null;

	@ObjectHolder(Main.MODID + ":frostball_entity")
	public static final EntityType<FrostballEntity> FROSTBALL_ENTITY = null;

	@ObjectHolder(Main.MODID + ":magic_ball_entity")
	public static final EntityType<MagicBallEntity> MAGIC_BALL_ENTITY = null;

	@ObjectHolder(Main.MODID + ":guide_item")
	public static final Item GUIDE_ITEM = null;

	@ObjectHolder(Main.MODID + ":air_rune_loot_modifier")
	public static final GlobalLootModifierSerializer<RuneLootModifier> AIR_RUNE_LOOT_MODIFIER = null;

	@ObjectHolder(Main.MODID + ":blood_rune_loot_modifier")
	public static final GlobalLootModifierSerializer<RuneLootModifier> BLOOD_RUNE_LOOT_MODIFIER = null;

	@ObjectHolder(Main.MODID + ":earth_rune_loot_modifier")
	public static final GlobalLootModifierSerializer<RuneLootModifier> EARTH_RUNE_LOOT_MODIFIER = null;

	@ObjectHolder(Main.MODID + ":fire_rune_loot_modifier")
	public static final GlobalLootModifierSerializer<RuneLootModifier> FIRE_RUNE_LOOT_MODIFIER = null;

	@ObjectHolder(Main.MODID + ":water_rune_loot_modifier")
	public static final GlobalLootModifierSerializer<RuneLootModifier> WATER_RUNE_LOOT_MODIFIER = null;

	@ObjectHolder(Main.MODID + ":guide_click")
	public static final SoundEvent GUIDE_CLICK = null;

	@ObjectHolder(Main.MODID + ":projectile_impact_sound")
	public static final SoundEvent PROJECTILE_IMPACT_SOUND = null;

	@ObjectHolder(Main.MODID + ":projectile_launch_sound")
	public static final SoundEvent PROJECTILE_LAUNCH_SOUND = null;

	@ObjectHolder(Main.MODID + ":magic_ball_particle")
	public static final ParticleType<MagicBallParticleData> MAGIC_BALL_PARTICLE = null;

	@ObjectHolder(Main.MODID + ":bleed_particle")
	public static final SimpleParticleType BLEED_PARTICLE = null;

	@ObjectHolder(Main.MODID + ":bleed_effect")
	public static final BleedEffect BLEED_EFFECT = null;

	public static final CreativeModeTab RUNES_ITEM_GROUP = new RuneswordItemGroup();

	public static final TagKey<Item> RUNEABLE_SWORDS = ItemTags.create(new ResourceLocation(Main.MODID, "runeable/swords"));
	public static final TagKey<Item> RUNEABLE_AXES = ItemTags.create(new ResourceLocation(Main.MODID, "runeable/axes"));
	
	public static final TagKey<Item> RUNE_TIER_1 = ItemTags.create(new ResourceLocation(Main.MODID, "rune_tiers/tier1"));
	public static final TagKey<Item> RUNE_TIER_2 = ItemTags.create(new ResourceLocation(Main.MODID, "rune_tiers/tier2"));
	public static final TagKey<Item> RUNE_TIER_3 = ItemTags.create(new ResourceLocation(Main.MODID, "rune_tiers/tier3"));
	public static final TagKey<Item> RUNE_TIER_4 = ItemTags.create(new ResourceLocation(Main.MODID, "rune_tiers/tier4"));

}
