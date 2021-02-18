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


/* TODO:
 * It turns out there is a corner case with using the RandomChanceWithLooting LootCondition with global loot modifier,
 * since then the DamageSource will be null if there is no entity (and this might happen because every loot modifier is run after every block destroyed etc)
 * Normally this is not a problem, but if another mod subscribes to the LootingLevelEvent and assumes that the DamageSource is not null,
 * there might be a null pointer exception.
 * Easy fix: Replace RandomChanceWithLooting with RandomChance in all loot modifier jsons
 * More involved fix: Create a new loot condition that works around the problem?
 */
public class RuneLootModifier extends LootModifier {

	private Item rune;

	public RuneLootModifier(ILootCondition[] conditionsIn, Item rune) {
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