package mod.vemerion.runesword.network;

import java.awt.Color;
import java.util.Map;
import java.util.function.Supplier;

import mod.vemerion.runesword.item.MagicRuneItem;
import mod.vemerion.runesword.particle.MagicBallParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.network.NetworkEvent;

public class AxeMagicPowersMessage {

	private Map<Enchantment, Integer> enchantments;
	private Vec3 pos;
	private double radius;

	public AxeMagicPowersMessage(Map<Enchantment, Integer> enchantments, Vec3 pos, double radius) {
		this.enchantments = enchantments;
		this.pos = pos;
		this.radius = radius;
	}

	public void encode(final FriendlyByteBuf buffer) {
		CompoundTag compound = new CompoundTag();
		compound.put("enchantments", MagicRuneItem.serializeEnchantments(enchantments));
		buffer.writeNbt(compound);
		buffer.writeDouble(pos.x);
		buffer.writeDouble(pos.y);
		buffer.writeDouble(pos.z);
		buffer.writeDouble(radius);
	}

	public static AxeMagicPowersMessage decode(final FriendlyByteBuf buffer) {
		Map<Enchantment, Integer> enchantments = MagicRuneItem
				.deserializeEnchantments(buffer.readNbt().getList("enchantments", Tag.TAG_COMPOUND));
		Vec3 pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
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
		private static SafeRunnable create(Map<Enchantment, Integer> enchantments, Vec3 pos, double radius) {
			return new SafeRunnable() {
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					var mc = Minecraft.getInstance();
					var level = mc.level;
					Player player = mc.player;
					if (level != null && player != null) {
						createParticles(enchantments, pos, radius, level);

						// Move player
						double up = enchantments.getOrDefault(Enchantments.FALL_PROTECTION, 0) / 12d;
						double forward = enchantments.getOrDefault(Enchantments.RIPTIDE, 0) / 9d;
						Vec3 direction = Vec3.directionFromRotation(0, player.getYRot());
						player.push(direction.x * forward, up, direction.z * forward);
					}
				}

				private void createParticles(Map<Enchantment, Integer> enchantments, Vec3 pos, double radius,
						Level level) {
					var rand = level.random;
					Enchantment[] enchantmentArr = enchantments.keySet().toArray(new Enchantment[0]);

					for (int i = 0; i < 200; i++) {
						Color color = MagicRuneItem.getRandEnchColor(rand, enchantmentArr);
						double direction = Math.toRadians(rand.nextDouble() * 360);
						double distance = rand.nextDouble() * radius;
						Vec3 particlePos = pos.add(Math.cos(direction) * distance, rand.nextDouble() * 2,
								Math.sin(direction) * distance);
						level.addParticle(new MagicBallParticleData(color.getRed() / 255f, color.getGreen() / 255f,
								color.getBlue() / 255f), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
					}
				}
			};
		}
	}
}
