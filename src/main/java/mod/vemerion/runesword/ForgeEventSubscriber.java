package mod.vemerion.runesword;

import mod.vemerion.runesword.network.RunesContainerListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
}
