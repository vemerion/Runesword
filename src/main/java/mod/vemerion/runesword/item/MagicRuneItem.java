package mod.vemerion.runesword.item;

import mod.vemerion.runesword.helpers.Helper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class MagicRuneItem extends RuneItem {

	public MagicRuneItem(Properties properties) {
		super(Helper.color(100, 0, 100, 255), properties);
	}

	
	@Override
	public void onRightClickMajor(ItemStack sword, PlayerEntity player, ItemStack rune) {
		
	}
}
