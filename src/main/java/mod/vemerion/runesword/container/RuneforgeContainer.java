package mod.vemerion.runesword.container;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.tileentity.RuneforgeTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RuneforgeContainer extends Container {
	
	private BlockPos pos;
	
	private static final int[] MINOR_SLOTS_Y = new int[] { 35, 61, 35 };
	private static final int[] MINOR_SLOTS_X = new int[] { 106, 80, 54 };
	
	public RuneforgeContainer(int id, PlayerInventory playerInv, PacketBuffer buffer) {
		this(id, playerInv, new ItemStackHandler(RuneforgeTileEntity.SLOT_COUNT), buffer.readBlockPos());
	}

	public RuneforgeContainer(int id, PlayerInventory playerInv, ItemStackHandler runeforge, BlockPos pos) {
		super(Main.RUNEFORGE_CONTAINER, id);
		this.pos = pos;
		
		addSlot(new SlotItemHandler(runeforge, RuneforgeTileEntity.SWORD_SLOT, 80, 35));
		addSlot(new SlotItemHandler(runeforge, RuneforgeTileEntity.MAJOR_RUNE_SLOT, 80, 9));
		for (int i = 0; i < RuneforgeTileEntity.MINOR_RUNE_SLOTS.length; i++) {
			addSlot(new SlotItemHandler(runeforge, RuneforgeTileEntity.MINOR_RUNE_SLOTS[i], MINOR_SLOTS_X[i], MINOR_SLOTS_Y[i]));			
		}
		
		// Player inventory
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		// Player hotbar
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInv, x, 8 + x * 18, 142));
		}
		
	}

	// TODO: Implement
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
	
	// TODO: Implement
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		return ItemStack.EMPTY;
	}

}
