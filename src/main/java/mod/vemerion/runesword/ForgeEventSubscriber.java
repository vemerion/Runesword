package mod.vemerion.runesword;

import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.network.RunesContainerListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
public class ForgeEventSubscriber {

	@SubscribeEvent
	public static void addRunesListener(PlayerLoggedInEvent event) {
		if (!event.getPlayer().world.isRemote)
			event.getPlayer().container.addListener(new RunesContainerListener((ServerPlayerEntity) event.getPlayer()));
	}

	@SubscribeEvent
	public static void addRunesListener(PlayerRespawnEvent event) {
		if (!event.getPlayer().world.isRemote)
			event.getPlayer().container.addListener(new RunesContainerListener((ServerPlayerEntity) event.getPlayer()));
	}

	@SubscribeEvent
	public static void addRunesListener(PlayerChangedDimensionEvent event) {
		if (!event.getPlayer().world.isRemote)
			event.getPlayer().container.addListener(new RunesContainerListener((ServerPlayerEntity) event.getPlayer()));
	}

	@SubscribeEvent
	public static void addRunesListener(PlayerEvent.Clone event) {
		if (!event.getPlayer().world.isRemote)
			event.getPlayer().container.addListener(new RunesContainerListener((ServerPlayerEntity) event.getPlayer()));
	}

	@SubscribeEvent
	public static void addRunesListener(PlayerContainerEvent.Open event) {
		if (!event.getPlayer().world.isRemote)
			event.getContainer().addListener(new RunesContainerListener((ServerPlayerEntity) event.getPlayer()));
	}

	@SubscribeEvent
	public static void runeAttack(AttackEntityEvent event) {
		event.getPlayer().getHeldItemMainhand().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onAttack(event.getPlayer(), event.getTarget()));
	}

	@SubscribeEvent
	public static void runeKill(LivingDeathEvent event) {
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
			player.getHeldItemMainhand().getCapability(Runes.CAPABILITY)
					.ifPresent(runes -> runes.onKill(player, event.getEntityLiving(), event.getSource()));
		}
	}

	@SubscribeEvent
	public static void runeTick(PlayerTickEvent event) {
		if (event.phase == Phase.START)
			event.player.getHeldItemMainhand().getCapability(Runes.CAPABILITY)
					.ifPresent(runes -> runes.tick(event.player));
	}

	@SubscribeEvent
	public static void runeHurt(LivingHurtEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity))
			return;

		event.getEntityLiving().getHeldItemMainhand().getCapability(Runes.CAPABILITY).ifPresent(runes -> event
				.setAmount(runes.onHurt((PlayerEntity) event.getEntityLiving(), event.getSource(), event.getAmount())));
	}

	@SubscribeEvent
	public static void runeRightClick(PlayerInteractEvent.RightClickItem event) {
		event.getPlayer().getHeldItemMainhand().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onRightClick(event.getPlayer()));

	}

	@SubscribeEvent
	public static void runeRightClick(PlayerInteractEvent.RightClickBlock event) {
		event.getPlayer().getHeldItemMainhand().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onRightClick(event.getPlayer()));

	}

	@SubscribeEvent
	public static void runeRightClick(PlayerInteractEvent.EntityInteract event) {
		event.getPlayer().getHeldItemMainhand().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onRightClick(event.getPlayer()));

	}
}
