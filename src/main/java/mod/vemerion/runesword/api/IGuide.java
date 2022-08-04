package mod.vemerion.runesword.api;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;

public interface IGuide {

	/**
	 * DO NOT USE! Use {@link #registerGuide()} (client-side!) to register guides
	 * and {@link #openGuide(Player, String)} (server-side!) to open guides instead
	 * of using this method
	 */
	@Deprecated
	void openGuide(IGuideChapter startChapter);

	/**
	 * Call on client side (for example in FMLClientSetupEvent) to register guide
	 * Guides loaded via resource pack are automatically registered
	 */
	void registerGuide(String id, IGuideChapter startChapter);

	/**
	 * Call on server side to send message to client to open guide
	 */
	void openGuide(Player player, String id);

	/**
	 * Call this to get a client side data provider for the given guide
	 */
	DataProvider guideProvider(DataGenerator generator, String modid, String guideId, IGuideChapter startChapter);

	IGuideChapter getGuide(String id);

	IGuideChapter createGuideChapter(ItemLike icon, Component title);

	IGuideChapter createGuideChapter(ResourceLocation icon, Component title);
}
