package mod.vemerion.runesword.container;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mod.vemerion.runesword.blockentity.RuneforgeBlockEntity;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.init.ModBlocks;
import mod.vemerion.runesword.init.ModMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RuneforgeMenu extends AbstractContainerMenu {

	private ContainerLevelAccess access;

	public static final int[] RUNE_SLOTS_Y = new int[] { 9, 35, 61, 35 };
	public static final int[] RUNE_SLOTS_X = new int[] { 80, 106, 80, 54 };

	private Slot runeableSlot;
	private List<Slot> runeSlots;
	private WrapperRuneHandler runeHandler;

	public RuneforgeMenu(int id, Inventory playerInv, FriendlyByteBuf buffer) {
		this(id, playerInv, new ItemStackHandler(), ContainerLevelAccess.NULL);
	}

	public RuneforgeMenu(int id, Inventory playerInv, ItemStackHandler runeableHandler,
			ContainerLevelAccess access) {
		super(ModMenus.RUNEFORGE.get(), id);
		this.access = access;

		runeSlots = new ArrayList<>();
		runeHandler = new WrapperRuneHandler();
		runeableSlot = addSlot(new SlotRuneableHandler(runeableHandler, RuneforgeBlockEntity.RUNEABLE_SLOT, 80, 35));
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
	public boolean stillValid(Player playerIn) {
		return stillValid(access, playerIn, ModBlocks.RUNEFORGE.get());
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		int runeforgeEnd = 5;
		int playerInvStart = runeforgeEnd;
		int playerInvEnd = playerInvStart + 3 * 9;
		int hotbarStart = playerInvEnd;
		int hotbarEnd = hotbarStart + 9;

		ItemStack copy = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack = slot.getItem();
			copy = stack.copy();
			if (index < runeforgeEnd) { // Runeable slot + rune slots
				if (!moveItemStackTo(stack, 5, 39, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= hotbarStart && index < hotbarEnd) { // Hotbar
				if (!moveItemStackTo(stack, 0, runeforgeEnd, false))
					if (!moveItemStackTo(stack, playerInvStart, playerInvEnd, false))
						return ItemStack.EMPTY;
			} else if (index >= playerInvStart && index < playerInvEnd) { // Player Inventory
				if (!moveItemStackTo(stack, 0, runeforgeEnd, false))
					if (!moveItemStackTo(stack, hotbarStart, hotbarEnd, false))
						return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (stack.getCount() == copy.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, stack);
		}

		return copy;
	}

	public ItemStack getRuneable() {
		return runeableSlot.getItem();
	}

	private void updateRuneSlots() {
		LazyOptional<Runes> maybeRunes = Runes.getRunes(runeableSlot.getItem());
		maybeRunes.ifPresent(runes -> {
			runeHandler.enable(runes);
		});

		if (!maybeRunes.isPresent())
			runeHandler.disable();
	}

	private class SlotRuneableHandler extends SlotItemHandler {

		public SlotRuneableHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return super.mayPlace(stack) && Runes.isRuneable(stack);
		}

		@Override
		public void setChanged() {
			super.setChanged();
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
		public boolean isActive() {
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
		public CompoundTag serializeNBT() {
			return inner.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			inner.deserializeNBT(nbt);
		}
	}
}
