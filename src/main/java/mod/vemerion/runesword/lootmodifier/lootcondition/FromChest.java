package mod.vemerion.runesword.lootmodifier.lootcondition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class FromChest implements ILootCondition {
	private static final FromChest INSTANCE = new FromChest();

	public FromChest() {
	}

	@Override
	public boolean test(LootContext t) {
		if (t.has(LootParameters.field_237457_g_)) {
			BlockPos pos = new BlockPos(t.get(LootParameters.field_237457_g_));
			TileEntity te = pos == null ? null : t.getWorld().getTileEntity(pos);
			return te instanceof LockableLootTileEntity;
		}
		return false;
	}

	@Override
	public LootConditionType func_230419_b_() {
		return LootConditions.FROM_CHEST;
	}

	public static class Serializer implements ILootSerializer<FromChest> {
		public void serialize(JsonObject json, FromChest instance, JsonSerializationContext p_230424_3_) {
		}

		public FromChest deserialize(JsonObject json, JsonDeserializationContext p_230423_2_) {
			return INSTANCE;
		}
	}

}
