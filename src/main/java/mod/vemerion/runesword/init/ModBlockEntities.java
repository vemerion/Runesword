package mod.vemerion.runesword.init;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.blockentity.RuneforgeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, Main.MODID);

	public static final RegistryObject<BlockEntityType<RuneforgeBlockEntity>> RUNEFORGE = BLOCK_ENTITIES
			.register("runeforge", () -> BlockEntityType.Builder
					.<RuneforgeBlockEntity>of(RuneforgeBlockEntity::new, ModBlocks.RUNEFORGE.get()).build(null));
}
