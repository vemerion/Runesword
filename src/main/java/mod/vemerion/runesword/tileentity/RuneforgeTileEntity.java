package mod.vemerion.runesword.tileentity;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.container.RuneforgeContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RuneforgeTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {

	public static final int SWORD_SLOT = 0;

	private ItemStack prevItem = ItemStack.EMPTY;
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

	public ItemStack getSword() {
		return runeforge.getStackInSlot(SWORD_SLOT);
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			ItemStack input = getSword();
			if (!ItemStack.areItemStacksEqual(input, prevItem)) {
				world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
			}

			prevItem = input.copy();
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> runeforge));
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
		return new RuneforgeContainer(id, playerInv, runeforge, IWorldPosCallable.of(world, pos));
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(Main.RUNEFORGE_BLOCK.getTranslationKey());
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("runeforge", runeforge.serializeNBT());
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		runeforge.deserializeNBT(nbt.getCompound("runeforge"));
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(getBlockState(), pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT compound = new CompoundNBT();
		write(compound);
		return compound;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		read(state, tag);
	}
}
