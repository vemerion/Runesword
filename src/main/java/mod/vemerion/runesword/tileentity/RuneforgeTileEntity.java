package mod.vemerion.runesword.tileentity;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.container.RuneforgeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class RuneforgeTileEntity extends TileEntity implements INamedContainerProvider {

	public static final int SWORD_SLOT = 0;

	private ItemStackHandler runeforge = new ItemStackHandler() {
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.getItem() instanceof SwordItem;
		}
	};

	public RuneforgeTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public RuneforgeTileEntity() {
		super(Main.RUNEFORGE_TILE_ENTITY);
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
		return new RuneforgeContainer(id, playerInv, runeforge, pos);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(Main.RUNEFORGE_BLOCK.getTranslationKey());
	}
}
