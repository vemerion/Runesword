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

public class EarthRuneLootModifier extends LootModifier {

	protected EarthRuneLootModifier(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(new ItemStack(RuneItem.EARTH_RUNE_ITEM));
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<EarthRuneLootModifier> {

		@Override
		public EarthRuneLootModifier read(ResourceLocation name, JsonObject object,
				ILootCondition[] conditionsIn) {
			return new EarthRuneLootModifier(conditionsIn);
		}

		@Override
		public JsonObject write(EarthRuneLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}