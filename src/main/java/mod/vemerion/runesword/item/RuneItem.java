package mod.vemerion.runesword.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;

import mod.vemerion.runesword.Main;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Main.MODID)
public class RuneItem extends Item {

	public static final Item FIRE_RUNE_ITEM = null;
	public static final Item WATER_RUNE_ITEM = null;
	public static final Item EARTH_RUNE_ITEM = null;
	public static final Item AIR_RUNE_ITEM = null;
	public static final Item BLOOD_RUNE_ITEM = null;

	private static final List<RuneItem> RUNES = new ArrayList<>();

	private final int color;

	public RuneItem(int color, Properties properties) {
		super(properties);
		this.color = color;
		RUNES.add(this);
	}

	public int getColor() {
		return color;
	}

	public static Iterable<RuneItem> getRunes() {
		return Iterables.unmodifiableIterable(RUNES);
	}

}
