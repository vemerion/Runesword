package mod.vemerion.runesword.init;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.block.RuneforgeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);

	public static final RegistryObject<Block> RUNEFORGE = BLOCKS.register("runeforge",
			() -> new RuneforgeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2, 6)));
}
