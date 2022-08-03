package mod.vemerion.runesword.network;

import java.util.function.Supplier;

import mod.vemerion.runesword.api.RuneswordAPI;
import mod.vemerion.runesword.capability.GuideData;
import mod.vemerion.runesword.guide.GuideChapter;
import mod.vemerion.runesword.screen.GuideScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class GuideMessage {

	public static final GuideMessage DUMMY = new GuideMessage(null, false);

	private String id;
	private boolean mute;

	public GuideMessage(String id, boolean mute) {
		this.id = id;
		this.mute = mute;
	}

	public void encode(final FriendlyByteBuf buffer) {
		buffer.writeUtf(id);
		buffer.writeBoolean(mute);
	}

	public static GuideMessage decode(final FriendlyByteBuf buffer) {
		return new GuideMessage(buffer.readUtf(), buffer.readBoolean());
	}

	public void handle(final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
			var sender = context.getSender();
			if (sender != null) {
				GuideData.receiveData(sender, this);
			}
		} else if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
			context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.openGuide(this)));
		}
	}

	public String getId() {
		return id;
	}

	public boolean isMute() {
		return mute;
	}

	private static class Handle {
		private static SafeRunnable openGuide(GuideMessage message) {
			return new SafeRunnable() {
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					var mc = Minecraft.getInstance();
					if (mc == null)
						return;

					var guide = RuneswordAPI.guide.getGuide(message.id);
					if (guide != null)
						mc.setScreen(new GuideScreen((GuideChapter) guide, message));
				}
			};
		}
	}
}
