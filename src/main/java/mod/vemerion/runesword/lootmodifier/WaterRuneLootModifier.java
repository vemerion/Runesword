package mod.vemerion.runesword.lootmodifier;

import java.util.List;

import com.google.gson.JsonObject;

import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class WaterRuneLootModifier extends LootModifier {

	protected WaterRuneLootModifier(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if (context.has(LootParameters.field_237457_g_)) {
			BlockPos pos = new BlockPos(context.get(LootParameters.field_237457_g_));
			if (context.getWorld().getTileEntity(pos) != null) {
				TileEntity te = context.getWorld().getTileEntity(pos);
				if (te instanceof LockableLootTileEntity) {
					generatedLoot.add(new ItemStack(RuneItem.WATER_RUNE_ITEM));
					System.out.println(context.getWorld().getChunk(pos).getStructureStarts());

				}
			}
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<WaterRuneLootModifier> {

		@Override
		public WaterRuneLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
			return new WaterRuneLootModifier(conditionsIn);
		}

		@Override
		public JsonObject write(WaterRuneLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
