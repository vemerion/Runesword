package mod.vemerion.runesword.item;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneswordItemGroup extends CreativeModeTab {

	public RuneswordItemGroup() {
		super(Main.MODID);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.FIRE_RUNE.get());
	}

	@Override
	public void fillItemList(NonNullList<ItemStack> items) {
		for (Item item : ForgeRegistries.ITEMS) {
			if (item != null)
				if (item.getRegistryName().getNamespace().equals(Main.MODID)) {
					item.fillItemCategory(CreativeModeTab.TAB_SEARCH, items);
				}
		}
	}
}
