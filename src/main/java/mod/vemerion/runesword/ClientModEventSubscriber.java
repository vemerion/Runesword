package mod.vemerion.runesword;

import mod.vemerion.runesword.init.ModBlockEntities;
import mod.vemerion.runesword.init.ModBlocks;
import mod.vemerion.runesword.init.ModEntities;
import mod.vemerion.runesword.init.ModMenus;
import mod.vemerion.runesword.init.ModParticles;
import mod.vemerion.runesword.item.RuneItem;
import mod.vemerion.runesword.particle.BleedParticle;
import mod.vemerion.runesword.particle.MagicBallParticle;
import mod.vemerion.runesword.renderer.RuneforgeBlockEntityRenderer;
import mod.vemerion.runesword.screen.RuneforgeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SnowGolemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {

	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.RUNEFORGE.get(), RenderType.cutout());

		event.enqueueWork(() -> {
			MenuScreens.register(ModMenus.RUNEFORGE.get(), RuneforgeScreen::new);
		});
	}

	@SubscribeEvent
	public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.RUNEFORGE.get(), RuneforgeBlockEntityRenderer::new);

		event.registerEntityRenderer(ModEntities.FROST_GOLEM.get(), SnowGolemRenderer::new);
		event.registerEntityRenderer(ModEntities.MAGIC_BALL.get(), NoRenderer::new);
		event.registerEntityRenderer(ModEntities.FROSTBALL.get(), r -> new ThrownItemRenderer<>(r));
	}

	@SubscribeEvent
	public static void onRegisterParticleFactory(ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		mc.particleEngine.register(ModParticles.MAGIC_BALL.get(), (s) -> new MagicBallParticle.Provider(s));
		mc.particleEngine.register(ModParticles.BLEED.get(), (s) -> new BleedParticle.Provider(s));
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

		protected NoRenderer(EntityRendererProvider.Context pContext) {
			super(pContext);
		}

		@Override
		public ResourceLocation getTextureLocation(T entity) {
			return null;
		}

	}
}
