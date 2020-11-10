package mod.vemerion.runesword.container;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.tileentity.RuneforgeTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RuneforgeContainer extends Container {

	private IWorldPosCallable pos;

	public static final int[] RUNE_SLOTS_Y = new int[] { 9, 35, 61, 35 };
	public static final int[] RUNE_SLOTS_X = new int[] { 80, 106, 80, 54 };

	private Slot swordSlot;
	private List<Slot> runeSlots;
	private WrapperRuneHandler runeHandler;

	public RuneforgeContainer(int id, PlayerInventory playerInv, PacketBuffer buffer) {
		this(id, playerInv, new ItemStackHandler(), IWorldPosCallable.DUMMY);
	}

	public RuneforgeContainer(int id, PlayerInventory playerInv, ItemStackHandler swordHandler, IWorldPosCallable pos) {
		super(Main.RUNEFORGE_CONTAINER, id);
		this.pos = pos;

		runeSlots = new ArrayList<>();
		runeHandler = new WrapperRuneHandler();
		swordSlot = addSlot(new SlotSwordHandler(swordHandler, RuneforgeTileEntity.SWORD_SLOT, 80, 35));
		updateRuneSlots();
		for (int i = 0; i < Runes.RUNES_COUNT; i++) {
			runeSlots.add(addSlot(new SlotRuneHandler(runeHandler, i, RUNE_SLOTS_X[i], RUNE_SLOTS_Y[i])));
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

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(pos, playerIn, Main.RUNEFORGE_BLOCK);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		int runeforgeEnd = 5;
		int playerInvStart = runeforgeEnd;
		int playerInvEnd = playerInvStart + 3 * 9;
		int hotbarStart = playerInvEnd;
		int hotbarEnd = hotbarStart + 9;

		ItemStack copy = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			copy = stack.copy();
			if (index < runeforgeEnd) { // Sword slot + rune slots
				if (!mergeItemStack(stack, 5, 39, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= hotbarStart && index < hotbarEnd) { // Hotbar
				if (!mergeItemStack(stack, 0, runeforgeEnd, false))
					if (!mergeItemStack(stack, playerInvStart, playerInvEnd, false))
						return ItemStack.EMPTY;
			} else if (index >= playerInvStart && index < playerInvEnd) { // Player Inventory
				if (!mergeItemStack(stack, 0, runeforgeEnd, false))
					if (!mergeItemStack(stack, hotbarStart, hotbarEnd, false))
						return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (stack.getCount() == copy.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, stack);
		}

		return copy;
	}

	public ItemStack getSword() {
		return swordSlot.getStack();
	}

	@Override
	public void detectAndSendChanges() {
		ItemStack sword = getSword();
		sword.getCapability(Runes.CAPABILITY).ifPresent(runes -> {
			if (runes.isDirty()) {
				CompoundNBT tag = sword.getOrCreateTag();
				tag.putBoolean("dirty", !tag.getBoolean("dirty"));
				sword.setTag(tag);
			}
		});
		super.detectAndSendChanges();
	}

	private void updateRuneSlots() {
		LazyOptional<Runes> maybeRunes = Runes.getRunes(swordSlot.getStack());
		maybeRunes.ifPresent(runes -> {
			runeHandler.enable(runes);
		});

		if (!maybeRunes.isPresent())
			runeHandler.disable();
	}

	private class SlotSwordHandler extends SlotItemHandler {

		public SlotSwordHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return super.isItemValid(stack) && stack.getItem() instanceof SwordItem;
		}

		@Override
		public void onSlotChanged() {
			super.onSlotChanged();
			updateRuneSlots();
		}
	}

	private static class SlotRuneHandler extends SlotItemHandler {

		private WrapperRuneHandler itemHandler;

		public SlotRuneHandler(WrapperRuneHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
			this.itemHandler = itemHandler;
		}

		@Override
		public boolean isEnabled() {
			return !itemHandler.disabled;
		}

	}

	private static class WrapperRuneHandler extends ItemStackHandler {
		private ItemStackHandler inner;
		private boolean disabled;

		public WrapperRuneHandler() {
			this.inner = new ItemStackHandler(Runes.RUNES_COUNT);
			this.disabled = true;
		}

		private void enable(ItemStackHandler inner) {
			this.inner = inner;
			disabled = false;
		}

		private void disable() {
			disabled = true;
		}

		@Override
		public void setSize(int size) {
			inner.setSize(size);
		}

		@Override
		public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
			inner.setStackInSlot(slot, stack);
		}

		@Override
		public int getSlots() {
			return inner.getSlots();
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return inner.getStackInSlot(slot);
		}

		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return inner.insertItem(slot, stack, simulate);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return inner.extractItem(slot, amount, simulate);
		}

		@Override
		public int getSlotLimit(int slot) {
			return inner.getSlotLimit(slot);
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return inner.isItemValid(slot, stack) && !disabled;
		}

		@Override
		public CompoundNBT serializeNBT() {
			return inner.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			inner.deserializeNBT(nbt);
		}
	}
}
