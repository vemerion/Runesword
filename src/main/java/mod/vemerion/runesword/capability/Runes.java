package mod.vemerion.runesword.capability;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.ItemStackHandler;

public class Runes extends ItemStackHandler {

	@CapabilityInject(Runes.class)
	public static final Capability<Runes> CAPABILITY = null;

	public static final int RUNES_COUNT = 4;
	public static final int MAJOR_SLOT = 0;
	public static final int FIRST_MINOR_SLOT = 1;
	public static final int SECOND_MINOR_SLOT = 2;
	public static final int THIRD_MINOR_SLOT = 3;

	private ItemStack owner;

	public Runes() {
		this(ItemStack.EMPTY);
	}

	public Runes(ItemStack owner) {
		super(RUNES_COUNT);
		this.owner = owner;
	}
	
	public boolean isSlotUnlocked(int slot) {
		int level = ((SwordItem) owner.getItem()).getTier().getHarvestLevel();
		
		switch (slot) {
		case FIRST_MINOR_SLOT:
			return level > 0;
		case SECOND_MINOR_SLOT:
			return level > 1;
		case THIRD_MINOR_SLOT:
			return level > 2;
		case MAJOR_SLOT:
			return level > 3;
		default:
			break;
		}
		return false;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if (!(stack.getItem() instanceof RuneItem))
			return false;
		
		return isSlotUnlocked(slot);
	}

	public static LazyOptional<Runes> getRunes(ItemStack stack) {
		return stack.getCapability(CAPABILITY);
	}

	@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
	public static class Provider implements ICapabilitySerializable<INBT> {
		private static final ResourceLocation SAVE_LOCATION = new ResourceLocation(Main.MODID, "runes");

		@SubscribeEvent
		public static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
			if (event.getObject().getItem() instanceof SwordItem)
				event.addCapability(SAVE_LOCATION, new Provider(event.getObject()));
		}
		
		public Provider(ItemStack owner) {
			this.owner = owner;
		}

		private ItemStack owner;
		private LazyOptional<Runes> instance = LazyOptional.of(() -> new Runes(owner));

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

	public static class Storage implements IStorage<Runes> {

		@Override
		public INBT writeNBT(Capability<Runes> capability, Runes instance, Direction side) {
			return instance.serializeNBT();

		}

		@Override
		public void readNBT(Capability<Runes> capability, Runes instance, Direction side, INBT nbt) {
			instance.deserializeNBT((CompoundNBT) nbt);
		}
	}

}
