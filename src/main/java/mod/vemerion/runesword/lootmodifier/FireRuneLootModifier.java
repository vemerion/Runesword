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

public class FireRuneLootModifier extends LootModifier {

	protected FireRuneLootModifier(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(new ItemStack(RuneItem.FIRE_RUNE_ITEM));
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<FireRuneLootModifier> {

		@Override
		public FireRuneLootModifier read(ResourceLocation name, JsonObject object,
				ILootCondition[] conditionsIn) {
			return new FireRuneLootModifier(conditionsIn);
		}

		@Override
		public JsonObject write(FireRuneLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
