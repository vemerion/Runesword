package mod.vemerion.runesword;

import mod.vemerion.runesword.container.RuneforgeContainer;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import mod.vemerion.runesword.entity.MagicBallEntity;
import mod.vemerion.runesword.item.RuneswordItemGroup;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import mod.vemerion.runesword.tileentity.RuneforgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Main.MODID)
public class Main {
	public static final String MODID = "runesword";
	
	@ObjectHolder(Main.MODID + ":runeforge_tile_entity")
	public static final TileEntityType<RuneforgeTileEntity> RUNEFORGE_TILE_ENTITY = null;

	@ObjectHolder(Main.MODID + ":runeforge_container")
	public static final ContainerType<RuneforgeContainer> RUNEFORGE_CONTAINER = null;
	
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

	
	public static final ItemGroup RUNES_ITEM_GROUP = new RuneswordItemGroup();
}
