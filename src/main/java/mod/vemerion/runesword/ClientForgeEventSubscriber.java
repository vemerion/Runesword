package mod.vemerion.runesword;

import mod.vemerion.runesword.capability.Runes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {
	
	@SubscribeEvent
	public static void addRuneTooltip(ItemTooltipEvent event) {
		event.getItemStack().getCapability(Runes.CAPABILITY).ifPresent(runes -> {
			event.getToolTip().addAll(runes.getTooltip());
		});
	}

}
