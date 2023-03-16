package mod.vemerion.runesword.capability;

import mod.vemerion.runesword.init.ModEffects;
import mod.vemerion.runesword.network.Network;
import mod.vemerion.runesword.network.SyncBleedingMessage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class EntityRuneData implements INBTSerializable<CompoundTag> {

	public static final Capability<EntityRuneData> CAPABILITY = CapabilityManager
			.get(new CapabilityToken<EntityRuneData>() {
			});

	private boolean isBleeding;

	public EntityRuneData() {
	}

	public void setBleeding(boolean b) {
		isBleeding = b;
	}

	public boolean isBleeding() {
		return isBleeding;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag compound = new CompoundTag();
		compound.putBoolean("isBleeding", isBleeding);
		return compound;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		isBleeding = nbt.getBoolean("isBleeding");
	}

	public static LazyOptional<EntityRuneData> get(Entity e) {
		return e.getCapability(CAPABILITY);
	}

	public static void synchBleeding(LivingEntity e) {
		Network.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> e),
				new SyncBleedingMessage(e.hasEffect(ModEffects.BLEED.get()), e.getId()));
	}

	public static void synchBleeding(Player player, LivingEntity e) {
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
				new SyncBleedingMessage(e.hasEffect(ModEffects.BLEED.get()), e.getId()));
	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		private LazyOptional<EntityRuneData> instance = LazyOptional.of(() -> new EntityRuneData());

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
