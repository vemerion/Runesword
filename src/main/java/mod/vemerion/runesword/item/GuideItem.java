package mod.vemerion.runesword.item;

import mod.vemerion.runesword.screen.GuideScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class GuideItem extends Item {

	public GuideItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (worldIn.isRemote)
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OpenTable.open());
		return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());

	}

	private static class OpenTable {
		private static DistExecutor.SafeRunnable open() {
			return new DistExecutor.SafeRunnable() {
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					Minecraft mc = Minecraft.getInstance();
					if (mc != null)
						Minecraft.getInstance().displayGuiScreen(new GuideScreen(GuideScreen.START_CHAPTER));
				}
			};
		}
	}
}
