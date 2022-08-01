package mod.vemerion.runesword;

import mod.vemerion.runesword.capability.EntityRuneData;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.effect.BleedEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
public class ForgeEventSubscriber {

	@SubscribeEvent
	public static void synchBleeding(PlayerEvent.StartTracking event) {
		Player player = event.getPlayer();
		Entity target = event.getTarget();
		if (!player.level.isClientSide && target instanceof LivingEntity)
			EntityRuneData.synchBleeding(player, (LivingEntity) target);
	}

	@SubscribeEvent
	public static void bleeding(LivingUpdateEvent event) {

		LivingEntity entity = event.getEntityLiving();
		Level level = entity.level;
		if (!level.isClientSide) {
			boolean isBleeding = entity.hasEffect(Main.BLEED_EFFECT);
			EntityRuneData.get(entity).ifPresent(d -> {
				if (isBleeding != d.isBleeding())
					EntityRuneData.synchBleeding(entity);
				d.setBleeding(isBleeding);
			});
		} else {
			EntityRuneData.get(entity).ifPresent(d -> {
				if (d.isBleeding() && entity.tickCount % 10 == 0)
					BleedEffect.addBleedingParticles(entity);
			});
		}
	}

	@SubscribeEvent
	public static void runeAttack(AttackEntityEvent event) {
		event.getPlayer().getMainHandItem().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onAttack(event.getPlayer(), event.getTarget()));
	}

	@SubscribeEvent
	public static void runeKill(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof Player) {
			Player player = (Player) event.getSource().getEntity();
			player.getMainHandItem().getCapability(Runes.CAPABILITY)
					.ifPresent(runes -> runes.onKill(player, event.getEntityLiving(), event.getSource()));
		}
	}

	@SubscribeEvent
	public static void runeTick(PlayerTickEvent event) {
		if (event.phase == Phase.START)
			event.player.getMainHandItem().getCapability(Runes.CAPABILITY).ifPresent(runes -> runes.tick(event.player));
	}

	@SubscribeEvent
	public static void runeHurt(LivingHurtEvent event) {
		if (!(event.getEntityLiving() instanceof Player))
			return;

		event.getEntityLiving().getMainHandItem().getCapability(Runes.CAPABILITY).ifPresent(runes -> event
				.setAmount(runes.onHurt((Player) event.getEntityLiving(), event.getSource(), event.getAmount())));
	}

	@SubscribeEvent
	public static void runeRightClick(PlayerInteractEvent.RightClickItem event) {
		event.getPlayer().getMainHandItem().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onRightClick(event.getPlayer()));
	}

	@SubscribeEvent
	public static void runeBreakSpeed(PlayerEvent.BreakSpeed event) {
		event.getPlayer().getMainHandItem().getCapability(Runes.CAPABILITY).ifPresent(runes -> event.setNewSpeed(
				runes.onBreakSpeed(event.getPlayer(), event.getState(), event.getPos(), event.getOriginalSpeed())));
	}

	@SubscribeEvent
	public static void runeHarvestCheck(PlayerEvent.HarvestCheck event) {
		event.getPlayer().getMainHandItem().getCapability(Runes.CAPABILITY).ifPresent(runes -> event
				.setCanHarvest(runes.onHarvestCheck(event.getPlayer(), event.getTargetBlock(), event.canHarvest())));
	}

	@SubscribeEvent
	public static void runeBlockBreak(BlockEvent.BreakEvent event) {
		event.getPlayer().getMainHandItem().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onBlockBreak(event.getPlayer(), event.getState(), event.getPos()));
	}
}
