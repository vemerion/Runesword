package mod.vemerion.runesword.network;

import java.awt.Color;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import mod.vemerion.runesword.item.MagicRuneItem;
import mod.vemerion.runesword.particle.MagicBallParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

public class AxeMagicPowersMessage {

	private Map<Enchantment, Integer> enchantments;
	private Vector3d pos;
	private double radius;

	public AxeMagicPowersMessage(Map<Enchantment, Integer> enchantments, Vector3d pos, double radius) {
		this.enchantments = enchantments;
		this.pos = pos;
		this.radius = radius;
	}

	public void encode(final PacketBuffer buffer) {
		CompoundNBT compound = new CompoundNBT();
		compound.put("enchantments", MagicRuneItem.serializeEnchantments(enchantments));
		buffer.writeCompoundTag(compound);
		buffer.writeDouble(pos.x);
		buffer.writeDouble(pos.y);
		buffer.writeDouble(pos.z);
		buffer.writeDouble(radius);
	}

	public static AxeMagicPowersMessage decode(final PacketBuffer buffer) {
		Map<Enchantment, Integer> enchantments = MagicRuneItem
				.deserializeEnchantments(buffer.readCompoundTag().getList("enchantments", Constants.NBT.TAG_COMPOUND));
		Vector3d pos = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		double radius = buffer.readDouble();
		return new AxeMagicPowersMessage(enchantments, pos, radius);
	}

	public void handle(final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(
				() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Particles.create(enchantments, pos, radius)));
	}

	private static class Particles {
		private static SafeRunnable create(Map<Enchantment, Integer> enchantments, Vector3d pos, double radius) {
			return new SafeRunnable() {
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					World world = Minecraft.getInstance().world;
					Random rand = world.rand;
					Enchantment[] enchantmentArr = enchantments.keySet().toArray(new Enchantment[0]);

					for (int i = 0; i < 200; i++) {
						Color color = MagicRuneItem.getRandEnchColor(rand, enchantmentArr);
						double direction = Math.toRadians(rand.nextDouble() * 360);
						double distance = rand.nextDouble() * radius;
						Vector3d particlePos = pos.add(Math.cos(direction) * distance, rand.nextDouble() * 2,
								Math.sin(direction) * distance);
						world.addParticle(new MagicBallParticleData(color.getRed() / 255f, color.getGreen() / 255f,
								color.getBlue() / 255f), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
					}
				}
			};
		}
	}
}
