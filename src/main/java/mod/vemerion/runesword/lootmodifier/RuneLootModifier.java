package mod.vemerion.runesword.lootmodifier;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class RuneLootModifier extends LootModifier {

	private Item rune;

	protected RuneLootModifier(ILootCondition[] conditionsIn, Item rune) {
		super(conditionsIn);
		this.rune = rune;
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(new ItemStack(rune));
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<RuneLootModifier> {

		private Item rune;

		public Serializer(Item bloodRuneItem) {
			this.rune = bloodRuneItem;
		}

		@Override
		public RuneLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
			return new RuneLootModifier(conditionsIn, rune);
		}

		@Override
		public JsonObject write(RuneLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}