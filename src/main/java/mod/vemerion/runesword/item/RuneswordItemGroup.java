package mod.vemerion.runesword.item;

import mod.vemerion.runesword.Main;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneswordItemGroup extends ItemGroup {

	public RuneswordItemGroup() {
		super(Main.MODID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(RuneItem.FIRE_RUNE_ITEM);
	}

	@Override
	public void fill(NonNullList<ItemStack> items) {
		for (Item item : ForgeRegistries.ITEMS) {
			if (item != null)
				if (item.getRegistryName().getNamespace().equals(Main.MODID)) {
					item.fillItemGroup(ItemGroup.SEARCH, items);
				}
		}
	}
}
