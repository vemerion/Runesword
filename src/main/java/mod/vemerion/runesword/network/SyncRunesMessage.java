package mod.vemerion.runesword.network;

import java.util.function.Supplier;

import mod.vemerion.runesword.capability.Runes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

@Deprecated
public class SyncRunesMessage {

	private CompoundNBT compound;
	private int slot;
	private int windowId;

	public SyncRunesMessage(CompoundNBT compound, int slot, int windowId) {
		this.compound = compound;
		this.slot = slot;
		this.windowId = windowId;
	}

	public void encode(final PacketBuffer buffer) {
		buffer.writeCompoundTag(compound);
		buffer.writeInt(slot);
		buffer.writeInt(windowId);
	}

	public static SyncRunesMessage decode(final PacketBuffer buffer) {
		return new SyncRunesMessage(buffer.readCompoundTag(), buffer.readInt(), buffer.readInt());
	}

	public void handle(final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(
				() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.syncRunes(compound, slot, windowId)));
	}

	private static class Handle {
		private static SafeRunnable syncRunes(CompoundNBT compound, int slot, int windowId) {
			return new SafeRunnable() {
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					ItemStack stack = ItemStack.EMPTY;
					PlayerEntity player = Minecraft.getInstance().player;
					if (windowId == 0) {
						stack = player.container.getSlot(slot).getStack();
					} else {
						stack = player.openContainer.getSlot(slot).getStack();
					}
					stack.getCapability(Runes.CAPABILITY).ifPresent(runes -> runes.deserializeNBT(compound));
				}
			};
		}
	}
}
