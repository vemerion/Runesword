package mod.vemerion.runesword;

import java.awt.Color;

import mod.vemerion.runesword.block.RuneforgeBlock;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.container.RuneforgeContainer;
import mod.vemerion.runesword.item.RuneItem;
import mod.vemerion.runesword.tileentity.RuneforgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
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

		event.getRegistry()
				.register(setup(new RuneItem(Color.ORANGE.getRGB(), new Item.Properties().group(ItemGroup.SEARCH)), "fire_rune_item"));
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
	public static void setup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(Runes.class, new Runes.Storage(), Runes::new);
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(Main.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}
}
