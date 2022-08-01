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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.ItemStackHandler;

public class Runes extends ItemStackHandler {

	public static final Capability<Runes> CAPABILITY = CapabilityManager.get(new CapabilityToken<Runes>() {
	});

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

		CompoundTag nbt = owner.getOrCreateTag();
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
	public void onAttack(Player player, Entity target) {
		if (player.level.isClientSide || player.getAttackStrengthScale(0) < 0.9)
			return;

		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			entry.getKey().onAttack(owner, player, target, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			((RuneItem) major.getItem()).onAttackMajor(owner, player, target, major);
	}

	// Logical-Server only
	public void onKill(Player player, LivingEntity entityLiving, DamageSource source) {
		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			entry.getKey().onKill(owner, player, entityLiving, source, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			((RuneItem) major.getItem()).onKillMajor(owner, player, entityLiving, source, major);
	}

	// On both sides
	public void tick(Player player) {

	}

	public float onHurt(Player player, DamageSource source, float amount) {
		if (!player.level.isClientSide) {

			for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
				amount = entry.getKey().onHurt(owner, player, source, amount, entry.getValue());

			ItemStack major = getStackInSlot(MAJOR_SLOT);
			if (!major.isEmpty())
				amount = ((RuneItem) major.getItem()).onHurtMajor(owner, player, source, amount, major);
		}

		return amount;
	}

	// On both sides
	public void onRightClick(Player player) {
		if (player.level.isClientSide)
			return;

		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			entry.getKey().onRightClick(owner, player, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			((RuneItem) major.getItem()).onRightClickMajor(owner, player, major);

	}

	// On both sides
	public float onBreakSpeed(Player player, BlockState state, BlockPos pos, float speed) {
		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			speed = entry.getKey().onBreakSpeed(owner, player, state, pos, speed, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			speed = ((RuneItem) major.getItem()).onBreakSpeedMajor(owner, player, state, pos, speed, major);

		return speed;
	}

	// Server only
	public boolean onHarvestCheck(Player player, BlockState state, boolean canHarvest) {
		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			canHarvest = ((RuneItem) major.getItem()).onHarvestCheckMajor(owner, player, state, canHarvest, major);

		return canHarvest;
	}

	// Server only
	public void onBlockBreak(Player player, BlockState state, BlockPos pos) {
		for (Entry<RuneItem, Set<ItemStack>> entry : getRunesMap().entrySet())
			entry.getKey().onBlockBreak(owner, player, state, pos, entry.getValue());

		ItemStack major = getStackInSlot(MAJOR_SLOT);
		if (!major.isEmpty())
			((RuneItem) major.getItem()).onBlockBreakMajor(owner, player, state, pos, major);
	}

	public Collection<? extends Component> getTooltip() {
		List<Component> tooltip = new ArrayList<>();
		for (int i = 0; i < RUNES_COUNT; i++) {
			ItemStack rune = getStackInSlot(i);
			TranslatableComponent prefix = i == MAJOR_SLOT
					? new TranslatableComponent("tooltip." + Main.MODID + ".major")
					: new TranslatableComponent("tooltip." + Main.MODID + ".minor");
			if (!rune.isEmpty()) {
				var text = new TranslatableComponent(rune.getDescriptionId())
						.withStyle(Style.EMPTY.withColor(((RuneItem) rune.getItem()).getColor()));
				tooltip.add(prefix.append(text));
			}
		}
		return tooltip;
	}

	public boolean isSlotUnlocked(int slot) {
		if (owner.is(Main.RUNE_TIER_4)) {
			return true;
		} else if (owner.is(Main.RUNE_TIER_3)) {
			return slot == FIRST_MINOR_SLOT || slot == SECOND_MINOR_SLOT || slot == THIRD_MINOR_SLOT;
		} else if (owner.is(Main.RUNE_TIER_2)) {
			return slot == FIRST_MINOR_SLOT || slot == SECOND_MINOR_SLOT;
		} else if (owner.is(Main.RUNE_TIER_1)) {
			return slot == FIRST_MINOR_SLOT;
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
	public static class Provider implements ICapabilitySerializable<CompoundTag> {
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
