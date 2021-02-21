package mod.vemerion.runesword;

import mod.vemerion.runesword.item.RuneItem;
import mod.vemerion.runesword.particle.MagicBallParticle;
import mod.vemerion.runesword.renderer.RuneforgeTileEntityRenderer;
import mod.vemerion.runesword.screen.RuneforgeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SnowManRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {

	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(Main.RUNEFORGE_BLOCK, RenderType.getCutout());

		ClientRegistry.bindTileEntityRenderer(Main.RUNEFORGE_TILE_ENTITY, RuneforgeTileEntityRenderer::new);

		ScreenManager.registerFactory(Main.RUNEFORGE_CONTAINER, RuneforgeScreen::new);

		RenderingRegistry.registerEntityRenderingHandler(Main.FROST_GOLEM_ENTITY, SnowManRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(Main.MAGIC_BALL_ENTITY, NoRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(Main.FROSTBALL_ENTITY,
				r -> new SpriteRenderer<>(r, Minecraft.getInstance().getItemRenderer()));
	}
	
	@SubscribeEvent
	public static void onRegisterParticleFactory(ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		mc.particles.registerFactory(Main.MAGIC_BALL_PARTICLE, (s) -> new MagicBallParticle.Factory(s));
	}

	@SubscribeEvent
	public static void onRegisterColor(ColorHandlerEvent.Item event) {
		for (RuneItem rune : RuneItem.getRunes()) {
			event.getItemColors().register((stack, layer) -> {
				return layer == 0 ? -1 : rune.getColor();
			}, rune);
		}
	}

	private static class NoRenderer<T extends Entity> extends EntityRenderer<T> {

		protected NoRenderer(EntityRendererManager renderManager) {
			super(renderManager);
		}

		@Override
		public ResourceLocation getEntityTexture(T entity) {
			return null;
		}

	}
}
