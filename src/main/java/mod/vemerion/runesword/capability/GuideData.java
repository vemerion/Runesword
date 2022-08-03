package mod.vemerion.runesword.capability;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.network.GuideMessage;
import mod.vemerion.runesword.network.Network;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;

public class GuideData implements INBTSerializable<CompoundTag> {

	public static final Capability<GuideData> CAPABILITY = CapabilityManager.get(new CapabilityToken<GuideData>() {
	});

	private boolean mute;

	public GuideData() {
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag compound = new CompoundTag();
		compound.putBoolean("mute", mute);
		return compound;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		mute = nbt.getBoolean("mute");
	}

	public static void openGuide(Player player, String id) {
		player.getCapability(CAPABILITY).ifPresent(data -> {
			Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					new GuideMessage(id, data.mute));
		});
	}

	public static void receiveData(Player player, GuideMessage message) {
		player.getCapability(CAPABILITY).ifPresent(data -> {
			data.mute = message.isMute();
		});
	}

	@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
	public static class Provider implements ICapabilitySerializable<CompoundTag> {
		private static final ResourceLocation SAVE_LOCATION = new ResourceLocation(Main.MODID, "guidedata");

		@SubscribeEvent
		public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof Player)
				event.addCapability(SAVE_LOCATION, new Provider());
		}

		private LazyOptional<GuideData> instance = LazyOptional.of(() -> new GuideData());

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return CAPABILITY.orEmpty(cap, instance);
		}

		@Override
		public CompoundTag serializeNBT() {
			return instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"))
					.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"))
					.deserializeNBT(nbt);
		}
	}
}
