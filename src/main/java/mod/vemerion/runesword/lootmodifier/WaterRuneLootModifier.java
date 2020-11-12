package mod.vemerion.runesword.lootmodifier;

import java.util.List;

import com.google.gson.JsonObject;

import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class WaterRuneLootModifier extends LootModifier {

	protected WaterRuneLootModifier(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(new ItemStack(RuneItem.WATER_RUNE_ITEM));
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
