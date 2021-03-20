package mod.vemerion.runesword.capability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import mod.vemerion.runesword.item.RunePowers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
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

		loadRunes();
	}

	private void loadRunes() {
		if (owner.isEmpty())
			return;

		CompoundNBT nbt = owner.getOrCreateTag();
		if (nbt.contains(Main.MODID))
			deserializeNBT(nbt.getCompound(Main.MODID));
	}
	
	@Override
	protected void onContentsChanged(int slot) {
		owner.getOrCreateTag().put(Main.MODID, serializeNBT());
	}

	private Map<RuneItem, Set<ItemStack>> getRunesMap() {
		Map<RuneItem, Set<ItemStack>> runes = new HashMap<>();
		for (int i = FIRST_MINOR_SLOT; i < RUNES_COUNT; i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack.isEmpty())
				continue;
			RuneItem rune = (RuneItem) stack.getItem();
			if (runes.containsKey(rune)) {
				runes.get(rune).add(stack);
			} else {
				Set<ItemStack> stacks = new HashSet<>();
				stacks.add(stack);
				runes.put(rune, stacks);
			}
		}
		return runes;
	}

	// On both sides
	public void onAttack(PlayerEntity player, Entity target) {
		if (player.world.isRemote || player.getCooledAttackStrength(0) < 0.9)
			return;

		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			entry.getKey().onAttack(owner, player, target, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			((RuneItem) major.getItem()).onAttackMajor(owner, player, target, major);
	}

	// Logical-Server only
	public void onKill(PlayerEntity player, LivingEntity entityLiving, DamageSource source) {
		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			entry.getKey().onKill(owner, player, entityLiving, source, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			((RuneItem) major.getItem()).onKillMajor(owner, player, entityLiving, source, major);
	}

	// On both sides
	public void tick(PlayerEntity player) {

	}

	public float onHurt(PlayerEntity player, DamageSource source, float amount) {
		if (!player.world.isRemote) {

			for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
				amount = entry.getKey().onHurt(owner, player, source, amount, entry.getValue());

			ItemStack major = getStackInSlot(MAJOR_SLOT);
			if (!major.isEmpty())
				amount = ((RuneItem) major.getItem()).onHurtMajor(owner, player, source, amount, major);
		}

		return amount;
	}

	// On both sides
	public void onRightClick(PlayerEntity player) {
		if (player.world.isRemote)
			return;

		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			entry.getKey().onRightClick(owner, player, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			((RuneItem) major.getItem()).onRightClickMajor(owner, player, major);

	}

	public float onBreakSpeed(PlayerEntity player, BlockState state, BlockPos pos, float speed) {
		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			speed = entry.getKey().onBreakSpeed(owner, player, state, pos, speed, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			speed = ((RuneItem) major.getItem()).onBreakSpeedMajor(owner, player, state, pos, speed, major);

		return speed;
	}
	
	public boolean onHarvestCheck(PlayerEntity player, BlockState state, boolean canHarvest) {
		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			canHarvest = entry.getKey().onHarvestCheck(owner, player, state, canHarvest, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			canHarvest = ((RuneItem) major.getItem()).onHarvestCheckMajor(owner, player, state, canHarvest, major);

		return canHarvest;
	}

	public Collection<? extends ITextComponent> getTooltip() {
		List<ITextComponent> tooltip = new ArrayList<>();
		for (int i = 0; i < RUNES_COUNT; i++) {
			ItemStack rune = getStackInSlot(i);
			TranslationTextComponent prefix = i == MAJOR_SLOT
					? new TranslationTextComponent("tooltip." + Main.MODID + ".major")
					: new TranslationTextComponent("tooltip." + Main.MODID + ".minor");
			if (!rune.isEmpty()) {
				ITextComponent text = new TranslationTextComponent(rune.getTranslationKey())
						.mergeStyle(Style.EMPTY.setColor(Color.fromInt(((RuneItem) rune.getItem()).getColor())));
				tooltip.add(prefix.append(text));
			}
		}
		return tooltip;
	}

	public boolean isSlotUnlocked(int slot) {
		int level = 4;
		if (owner.getItem() instanceof TieredItem)
			level = ((TieredItem) owner.getItem()).getTier().getHarvestLevel();

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

	public static boolean isRuneable(ItemStack stack) {
		return RunePowers.isAxe(stack) || RunePowers.isSword(stack);
	}

	public static LazyOptional<Runes> getRunes(ItemStack stack) {
		return stack.getCapability(CAPABILITY);
	}

	@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
	public static class Provider implements ICapabilitySerializable<INBT> {
		private static final ResourceLocation SAVE_LOCATION = new ResourceLocation(Main.MODID, "runes");

		@SubscribeEvent
		public static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
			if (isRuneable(event.getObject()))
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
