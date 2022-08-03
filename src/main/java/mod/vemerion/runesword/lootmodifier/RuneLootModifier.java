package mod.vemerion.runesword.lootmodifier;

import java.util.List;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class RuneLootModifier extends LootModifier {

	private Supplier<Item> rune;

	public RuneLootModifier(LootItemCondition[] conditionsIn, Supplier<Item> rune) {
		super(conditionsIn);
		this.rune = rune;
	}
	

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(new ItemStack(rune.get()));
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<RuneLootModifier> {

		private Supplier<Item> rune;

		public Serializer(Supplier<Item> rune) {
			this.rune = rune;
		}

		@Override
		public RuneLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
			return new RuneLootModifier(conditionsIn, rune);
		}

		@Override
		public JsonObject write(RuneLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}