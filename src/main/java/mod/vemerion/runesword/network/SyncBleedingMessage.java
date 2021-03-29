package mod.vemerion.runesword.network;

import java.util.function.Supplier;

import mod.vemerion.runesword.capability.EntityRuneData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncBleedingMessage {

	private boolean isBleeding;
	private int id;

	public SyncBleedingMessage(boolean isBleeding, int id) {
		this.isBleeding = isBleeding;
		this.id = id;
	}

	public void encode(final PacketBuffer buffer) {
		buffer.writeBoolean(isBleeding);
		buffer.writeInt(id);
	}

	public static SyncBleedingMessage decode(final PacketBuffer buffer) {
		return new SyncBleedingMessage(buffer.readBoolean(), buffer.readInt());
	}

	public void handle(final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.bleeding(isBleeding, id)));
	}

	private static class Handle {
		private static SafeRunnable bleeding(boolean isBleeding, int id) {
			return new SafeRunnable() {
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					Minecraft mc = Minecraft.getInstance();
					World world = mc.world;
					if (world != null) {
						Entity e = world.getEntityByID(id);
						if (e != null)
							EntityRuneData.get(e).ifPresent(d -> d.setBleeding(isBleeding));
					}
				}
			};
		}
	}
}
