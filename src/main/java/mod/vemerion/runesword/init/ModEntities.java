package mod.vemerion.runesword.init;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import mod.vemerion.runesword.entity.MagicBallEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
			Main.MODID);

	public static final RegistryObject<EntityType<FrostGolemEntity>> FROST_GOLEM = ENTITIES.register("frost_golem",
			() -> EntityType.Builder.<FrostGolemEntity>of(FrostGolemEntity::new, MobCategory.MISC).sized(0.7F, 1.9F)
					.clientTrackingRange(8).build(""));

	public static final RegistryObject<EntityType<FrostballEntity>> FROSTBALL = ENTITIES.register("frostball",
			() -> EntityType.Builder.<FrostballEntity>of(FrostballEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)
					.clientTrackingRange(4).updateInterval(10).build(""));

	public static final RegistryObject<EntityType<MagicBallEntity>> MAGIC_BALL = ENTITIES.register("magic_ball",
			() -> EntityType.Builder.<MagicBallEntity>of(MagicBallEntity::new, MobCategory.MISC).sized(0.5f, 0.5f)
					.clientTrackingRange(4).updateInterval(20).build(""));
}
