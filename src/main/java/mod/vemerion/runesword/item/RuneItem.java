package mod.vemerion.runesword.item;

import net.minecraft.item.Item;

public class RuneItem extends Item {
	private final int color;

	public RuneItem(int color, Properties properties) {
		super(properties);
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}

}
