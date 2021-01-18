package mod.vemerion.runesword;

import mod.vemerion.runesword.container.RuneforgeContainer;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import mod.vemerion.runesword.item.RuneswordItemGroup;
import mod.vemerion.runesword.tileentity.RuneforgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
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

	
	public static final ItemGroup RUNES_ITEM_GROUP = new RuneswordItemGroup();
}
