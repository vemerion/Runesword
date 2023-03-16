package mod.vemerion.runesword;

import mod.vemerion.runesword.capability.EntityRuneData;
import mod.vemerion.runesword.capability.GuideData;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.effect.BleedEffect;
import mod.vemerion.runesword.init.ModEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
public class ForgeEventSubscriber {

	@SubscribeEvent
	public static void synchBleeding(PlayerEvent.StartTracking event) {
		Player player = event.getEntity();
		Entity target = event.getTarget();
		if (!player.level.isClientSide && target instanceof LivingEntity)
			EntityRuneData.synchBleeding(player, (LivingEntity) target);
	}

	@SubscribeEvent
	public static void bleeding(LivingTickEvent event) {

		LivingEntity entity = event.getEntity();
		Level level = entity.level;
		if (!level.isClientSide) {
			boolean isBleeding = entity.hasEffect(ModEffects.BLEED.get());
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
		event.getEntity().getMainHandItem().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onAttack(event.getEntity(), event.getTarget()));
	}

	@SubscribeEvent
	public static void runeKill(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof Player) {
			Player player = (Player) event.getSource().getEntity();
			player.getMainHandItem().getCapability(Runes.CAPABILITY)
					.ifPresent(runes -> runes.onKill(player, event.getEntity(), event.getSource()));
		}
	}

	@SubscribeEvent
	public static void runeTick(PlayerTickEvent event) {
		if (event.phase == Phase.START)
			event.player.getMainHandItem().getCapability(Runes.CAPABILITY).ifPresent(runes -> runes.tick(event.player));
	}

	@SubscribeEvent
	public static void runeHurt(LivingHurtEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		event.getEntity().getMainHandItem().getCapability(Runes.CAPABILITY).ifPresent(runes -> event
				.setAmount(runes.onHurt((Player) event.getEntity(), event.getSource(), event.getAmount())));
	}

	@SubscribeEvent
	public static void runeRightClick(PlayerInteractEvent.RightClickItem event) {
		event.getEntity().getMainHandItem().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onRightClick(event.getEntity()));
	}

	@SubscribeEvent
	public static void runeBreakSpeed(PlayerEvent.BreakSpeed event) {
		event.getEntity().getMainHandItem().getCapability(Runes.CAPABILITY).ifPresent(runes -> event.setNewSpeed(runes
				.onBreakSpeed(event.getEntity(), event.getState(), event.getPosition(), event.getOriginalSpeed())));
	}

	@SubscribeEvent
	public static void runeHarvestCheck(PlayerEvent.HarvestCheck event) {
		event.getEntity().getMainHandItem().getCapability(Runes.CAPABILITY).ifPresent(runes -> event
				.setCanHarvest(runes.onHarvestCheck(event.getEntity(), event.getTargetBlock(), event.canHarvest())));
	}

	@SubscribeEvent
	public static void runeBlockBreak(BlockEvent.BreakEvent event) {
		event.getPlayer().getMainHandItem().getCapability(Runes.CAPABILITY)
				.ifPresent(runes -> runes.onBlockBreak(event.getPlayer(), event.getState(), event.getPos()));
	}

	private static final ResourceLocation SAVE_LOCATION_ENTITY_RUNE_DATA = new ResourceLocation(Main.MODID,
			"entityrunedata");
	private static final ResourceLocation SAVE_LOCATION_GUIDE_DATA = new ResourceLocation(Main.MODID, "guidedata");
	private static final ResourceLocation SAVE_LOCATION_RUNES = new ResourceLocation(Main.MODID, "runes");

	@SubscribeEvent
	public static void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(SAVE_LOCATION_ENTITY_RUNE_DATA, new EntityRuneData.Provider());
		}

		if (event.getObject() instanceof Player)
			event.addCapability(SAVE_LOCATION_GUIDE_DATA, new GuideData.Provider());
	}

	@SubscribeEvent
	public static void attachCapabilityItemStack(AttachCapabilitiesEvent<ItemStack> event) {
		if (Runes.isRuneable(event.getObject()))
			event.addCapability(SAVE_LOCATION_RUNES, new Runes.Provider(event.getObject()));
	}
}
