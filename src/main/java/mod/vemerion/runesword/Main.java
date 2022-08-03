package mod.vemerion.runesword;

import mod.vemerion.runesword.init.ModBlockEntities;
import mod.vemerion.runesword.init.ModBlocks;
import mod.vemerion.runesword.init.ModEffects;
import mod.vemerion.runesword.init.ModEntities;
import mod.vemerion.runesword.init.ModItems;
import mod.vemerion.runesword.init.ModLootModifiers;
import mod.vemerion.runesword.init.ModMenus;
import mod.vemerion.runesword.init.ModParticles;
import mod.vemerion.runesword.init.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {
	public static final String MODID = "runesword";
	
	public Main() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.BLOCKS.register(bus);
		ModBlockEntities.BLOCK_ENTITIES.register(bus);
		ModEffects.EFFECTS.register(bus);
		ModEntities.ENTITIES.register(bus);
		ModItems.ITEMS.register(bus);
		ModLootModifiers.LOOT_MODIFIERS.register(bus);
		ModMenus.MENUS.register(bus);
		ModParticles.PARTICLES.register(bus);
		ModSounds.SOUNDS.register(bus);
	}
	
	public static final TagKey<Item> RUNEABLE_SWORDS = ItemTags.create(new ResourceLocation(Main.MODID, "runeable/swords"));
	public static final TagKey<Item> RUNEABLE_AXES = ItemTags.create(new ResourceLocation(Main.MODID, "runeable/axes"));
	
	public static final TagKey<Item> RUNE_TIER_1 = ItemTags.create(new ResourceLocation(Main.MODID, "rune_tiers/tier1"));
	public static final TagKey<Item> RUNE_TIER_2 = ItemTags.create(new ResourceLocation(Main.MODID, "rune_tiers/tier2"));
	public static final TagKey<Item> RUNE_TIER_3 = ItemTags.create(new ResourceLocation(Main.MODID, "rune_tiers/tier3"));
	public static final TagKey<Item> RUNE_TIER_4 = ItemTags.create(new ResourceLocation(Main.MODID, "rune_tiers/tier4"));

}
