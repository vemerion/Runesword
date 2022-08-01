package mod.vemerion.runesword.network;

import java.util.function.Supplier;

import mod.vemerion.runesword.capability.Runes;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.network.NetworkEvent;

@Deprecated
public class SyncRunesMessage {

	private CompoundTag compound;
	private int slot;
	private int windowId;

	public SyncRunesMessage(CompoundTag compound, int slot, int windowId) {
		this.compound = compound;
		this.slot = slot;
		this.windowId = windowId;
	}

	public void encode(final FriendlyByteBuf buffer) {
		buffer.writeNbt(compound);
		buffer.writeInt(slot);
		buffer.writeInt(windowId);
	}

	public static SyncRunesMessage decode(final FriendlyByteBuf buffer) {
		return new SyncRunesMessage(buffer.readNbt(), buffer.readInt(), buffer.readInt());
	}

	public void handle(final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(
				() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.syncRunes(compound, slot, windowId)));
	}

	private static class Handle {
		private static SafeRunnable syncRunes(CompoundTag compound, int slot, int windowId) {
			return new SafeRunnable() {
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					var stack = ItemStack.EMPTY;
					var mc = Minecraft.getInstance();
					var player = mc.player;
					if (windowId == 0) {
						stack = player.inventoryMenu.getSlot(slot).getItem();
					} else {
						stack = player.containerMenu.getSlot(slot).getItem();
					}
					stack.getCapability(Runes.CAPABILITY).ifPresent(runes -> runes.deserializeNBT(compound));
				}
			};
		}
	}
}
