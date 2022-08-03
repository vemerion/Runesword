package mod.vemerion.runesword.blockentity;

import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.container.RuneforgeMenu;
import mod.vemerion.runesword.init.ModBlockEntities;
import mod.vemerion.runesword.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RuneforgeBlockEntity extends BlockEntity implements MenuProvider {

	public static final int RUNEABLE_SLOT = 0;

	private ItemStack prevItem = ItemStack.EMPTY;
	private ItemStackHandler runeforge = new ItemStackHandler() {
		public boolean isItemValid(int slot, ItemStack stack) {
			return Runes.isRuneable(stack);
		}

		protected void onContentsChanged(int slot) {
			setChanged();
		};
	};

	public RuneforgeBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pWorldPosition, BlockState pBlockState) {
		super(tileEntityTypeIn, pWorldPosition, pBlockState);
	}

	public RuneforgeBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		this(ModBlockEntities.RUNEFORGE.get(), pWorldPosition, pBlockState);
	}

	public ItemStack getRuneable() {
		return runeforge.getStackInSlot(RUNEABLE_SLOT);
	}

	public void tick() {
		if (!level.isClientSide) {
			ItemStack input = getRuneable();
			if (!ItemStack.matches(input, prevItem)) {
				level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
			}

			prevItem = input.copy();
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> runeforge));
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
		return new RuneforgeMenu(id, playerInv, runeforge, ContainerLevelAccess.create(level, worldPosition));
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent(ModBlocks.RUNEFORGE.get().getDescriptionId());
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.put("runeforge", runeforge.serializeNBT());
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		runeforge.deserializeNBT(pTag.getCompound("runeforge"));
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithoutMetadata();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}
}
