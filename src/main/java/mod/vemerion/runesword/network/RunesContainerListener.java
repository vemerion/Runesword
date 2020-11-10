package mod.vemerion.runesword.network;

import mod.vemerion.runesword.capability.Runes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.PacketDistributor;

public class RunesContainerListener implements IContainerListener {

	private ServerPlayerEntity player;

	public RunesContainerListener(ServerPlayerEntity player) {
		this.player = player;
	}

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		for (int i = 0; i < containerToSend.inventorySlots.size(); i++) {
			int idx = i;
			containerToSend.getSlot(i).getStack().getCapability(Runes.CAPABILITY).ifPresent(runes -> {
				Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
						new SyncRunesMessage(runes.serializeNBT(), idx, containerToSend.windowId));
			});
		}
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
		stack.getCapability(Runes.CAPABILITY).ifPresent(runes -> {
			Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
					new SyncRunesMessage(runes.serializeNBT(), slotInd, containerToSend.windowId));
		});
	}

	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
	}

}
