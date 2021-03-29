package mod.vemerion.runesword.capability;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.network.Network;
import mod.vemerion.runesword.network.SyncBleedingMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;

public class EntityRuneData implements INBTSerializable<CompoundNBT> {

	@CapabilityInject(EntityRuneData.class)
	public static final Capability<EntityRuneData> CAPABILITY = null;

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
	public CompoundNBT serializeNBT() {
		CompoundNBT compound = new CompoundNBT();
		compound.putBoolean("isBleeding", isBleeding);
		return compound;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		isBleeding = nbt.getBoolean("isBleeding");
	}

	public static LazyOptional<EntityRuneData> get(Entity e) {
		return e.getCapability(CAPABILITY);
	}

	public static void synchBleeding(LivingEntity e) {
		Network.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> e),
				new SyncBleedingMessage(e.isPotionActive(Main.BLEED_EFFECT), e.getEntityId()));
	}

	public static void synchBleeding(PlayerEntity player, LivingEntity e) {
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new SyncBleedingMessage(e.isPotionActive(Main.BLEED_EFFECT), e.getEntityId()));
	}

	@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
	public static class Provider implements ICapabilitySerializable<INBT> {
		private static final ResourceLocation SAVE_LOCATION = new ResourceLocation(Main.MODID, "entityrunedata");

		@SubscribeEvent
		public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof LivingEntity)
				event.addCapability(SAVE_LOCATION, new Provider());
		}

		private LazyOptional<EntityRuneData> instance = LazyOptional.of(() -> new EntityRuneData());

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return CAPABILITY.orEmpty(cap, instance);
		}

		@Override
		public INBT serializeNBT() {
			return CAPABILITY.getStorage().writeNBT(CAPABILITY,
					instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
		}

		@Override
		public void deserializeNBT(INBT nbt) {
			CAPABILITY.getStorage().readNBT(CAPABILITY,
					instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null,
					nbt);
		}
	}

	public static class Storage implements IStorage<EntityRuneData> {

		@Override
		public INBT writeNBT(Capability<EntityRuneData> capability, EntityRuneData instance, Direction side) {
			return instance.serializeNBT();

		}

		@Override
		public void readNBT(Capability<EntityRuneData> capability, EntityRuneData instance, Direction side, INBT nbt) {
			instance.deserializeNBT((CompoundNBT) nbt);
		}
	}
}
