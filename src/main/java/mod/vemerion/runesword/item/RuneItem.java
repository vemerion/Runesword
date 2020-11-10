package mod.vemerion.runesword.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;

import net.minecraft.item.Item;

public class RuneItem extends Item {
	
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
