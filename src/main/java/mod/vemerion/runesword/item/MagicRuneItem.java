package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.entity.MagicBallEntity;
import mod.vemerion.runesword.helpers.Helper;
import mod.vemerion.runesword.network.AxeMagicPowersMessage;
import mod.vemerion.runesword.network.Network;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicRuneItem extends RuneItem {

	public MagicRuneItem(Properties properties) {
		super(Helper.color(100, 0, 100, 255), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	private static class AxePowers extends RunePowers {

		private static final double START_RADIUS = 2;
		private static final float START_DAMAGE = 3;
		private static final int START_COOLDOWN = 20 * 14;

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return enchantment.getRegistryName().getNamespace().equals("minecraft");
		}

		@Override
		public float onHurtMajor(ItemStack runeable, PlayerEntity player, DamageSource source, float amount,
				ItemStack rune) {
			Runes.getRunes(runeable).ifPresent(runes -> {
				Map<Enchantment, Integer> enchants = getEnchantments(minorMagicRunes(runes));
				if (source.getTrueSource() instanceof LivingEntity
						&& random.nextDouble() < getEnchantmentLevel(Enchantments.PROTECTION, enchants) * 0.01)
					magicArea(enchants, player, player.world);
			});
			return super.onHurtMajor(runeable, player, source, amount, rune);
		}

		@Override
		public void onRightClickMajor(ItemStack runeable, PlayerEntity player, ItemStack rune) {
			CooldownTracker cdTracker = player.getCooldownTracker();
			if (cdTracker.hasCooldown(rune.getItem()))
				return;
			
			Runes.getRunes(runeable).ifPresent(runes -> {
				World world = player.world;
				Map<Enchantment, Integer> enchants = getEnchantments(minorMagicRunes(runes));
				
				int cooldown = START_COOLDOWN;
				int quickCharge = getEnchantmentLevel(Enchantments.QUICK_CHARGE, enchants);
				cooldown *= (1 - quickCharge * 0.05);
				cooldown += getEnchantmentLevel(Enchantments.INFINITY, enchants) * 20;
				cdTracker.setCooldown(rune.getItem(), cooldown);

				magicArea(enchants, player, world);
			});
		}

		private void magicArea(Map<Enchantment, Integer> enchants, PlayerEntity player, World world) {
			world.playSound(null, player.getPosition(), Main.PROJECTILE_IMPACT_SOUND, SoundCategory.PLAYERS, 1,
					Helper.soundPitch(random));
			
			double radius = START_RADIUS;
			float damage = START_DAMAGE;
			DamageSource source = Helper.magicDamage(player);

			// More radius in water with depth strider
			if (player.isInWater())
				radius += getEnchantmentLevel(Enchantments.DEPTH_STRIDER, enchants) / 9d;

			// More radius with sweeping
			radius += getEnchantmentLevel(Enchantments.SWEEPING, enchants) / 9d;
			
			// More radius if riding
			if (player.getRidingEntity() instanceof HorseEntity)
				radius += getEnchantmentLevel(Enchantments.LOYALTY, enchants) / 6d;

			// Bypass armor
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.UNBREAKING, enchants) * 0.1) {
				source.setDamageBypassesArmor();
			}

			// More damage if full attack strength
			if (player.getCooledAttackStrength(0) > 0.99)
				damage += getEnchantmentLevel(Enchantments.POWER, enchants) * 0.5;
			
			// More damage in nether with flame
			if (world.getDimensionKey() == World.THE_NETHER)
				damage += getEnchantmentLevel(Enchantments.FLAME, enchants);
			
			// More damage with speed effect
			if (player.isPotionActive(Effects.SPEED))
				damage += getEnchantmentLevel(Enchantments.SOUL_SPEED, enchants) * 0.4;
			
			// More damage with infinity
			damage += getEnchantmentLevel(Enchantments.INFINITY, enchants) * 2;


			AxisAlignedBB box = new AxisAlignedBB(player.getPositionVec(), player.getPositionVec())
					.grow(radius, 0, radius).expand(0, 2, 0);
			int channeling = getEnchantmentLevel(Enchantments.CHANNELING, enchants);
			int lure = getEnchantmentLevel(Enchantments.LURE, enchants);
			int looting = getEnchantmentLevel(Enchantments.LOOTING, enchants);
			int knockback = getEnchantmentLevel(Enchantments.KNOCKBACK, enchants);
			int punch = getEnchantmentLevel(Enchantments.PUNCH, enchants);
			for (Entity e : world.getEntitiesInAABBexcluding(player, box, e -> e != player.getRidingEntity())) {
				// More damage further away with channeling
				damage += channeling * player.getDistance(e) * 0.3f;
				
				// More damage if mob has armor
				if (hasArmor(e))
					damage += getEnchantmentLevel(Enchantments.PIERCING, enchants) * 0.4;

				applyMagicDamage(e, source, damage, enchants, random, 0.7f);

				// Pull
				Vector3d inwards = player.getPositionVec().subtract(e.getPositionVec()).normalize();
				Vector3d pull = inwards.scale(lure / 9d);
				e.addVelocity(pull.x, pull.y, pull.z);

				// Fire
				if (random.nextDouble() < getEnchantmentLevel(Enchantments.FIRE_ASPECT, enchants) * 0.08)
					e.setFire(3);

				// Exp
				if (!e.isAlive() && world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
					bonusExp(world, looting, e.getPositionVec());

				// Knockback
				knockback(knockback, punch, inwards.scale(-1), e);

				// Remove projectiles
				if (e instanceof ProjectileEntity && random
						.nextDouble() < getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, enchants) * 0.05)
					e.remove();
			}

			// Explosion
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.BLAST_PROTECTION, enchants) * 0.025)
				world.createExplosion(player, player.getPosX(), player.getPosY(), player.getPosZ(), 2, Mode.BREAK);

			int efficiency = getEnchantmentLevel(Enchantments.EFFICIENCY, enchants);
			int thorns = getEnchantmentLevel(Enchantments.THORNS, enchants);
			int fireProt = getEnchantmentLevel(Enchantments.FIRE_PROTECTION, enchants);
			int frostWalker = getEnchantmentLevel(Enchantments.FROST_WALKER, enchants);
			int silkTouch = getEnchantmentLevel(Enchantments.SILK_TOUCH, enchants);
			int luckSea = getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, enchants);
			BlockPos.getAllInBox(box).forEach(p -> {

				// Break wood
				BlockState state = world.getBlockState(p);
				if (state.getHarvestTool() == ToolType.AXE && random.nextDouble() < efficiency * 0.03)
					world.destroyBlock(p, true);

				// Trail
				if (random.nextDouble() < thorns * 0.02)
					leaveTrail(world, p, Blocks.OAK_SAPLING.getDefaultState());
				else if (random.nextDouble() < fireProt * 0.01)
					leaveTrail(world, p, Blocks.FIRE.getDefaultState());
				else if (random.nextDouble() < frostWalker * 0.03)
					leaveTrail(world, p, Blocks.SNOW.getDefaultState());
				else if (random.nextDouble() < luckSea * 0.02 && player.isInWater())
					leaveTrail(world, p, Blocks.SEAGRASS.getDefaultState());

				// Bonemeal
				if (random.nextDouble() < silkTouch * 0.1)
					BoneMealItem.applyBonemeal(Items.BONE_MEAL.getDefaultInstance(), world, p, player);
			});

			// Heal player
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.MENDING, enchants) / 3f)
				player.heal(1);
			
			// Restore air
			int respiration = getEnchantmentLevel(Enchantments.RESPIRATION, enchants);
			restoreAir(player, respiration * 0.03f);

			// Send out magic ball
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.MULTISHOT, enchants) / 3f) {
				float pitch = random.nextFloat() * 360;
				float yaw = random.nextFloat() * 360;
				Vector3d position = player.getPositionVec().add(0, 1.5, 0);
				shootMagicBall(player, world, enchants, position, pitch, yaw, 0, 0.5f);
			}

			// Send particle message
			Network.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
					new AxeMagicPowersMessage(enchants, player.getPositionVec(), radius));
		}

		private boolean hasArmor(Entity e) {
			for (ItemStack stack : e.getArmorInventoryList())
				if (!stack.isEmpty())
					return true;
			return false;
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final int COOLDOWN = 20 * 10;

		@Override
		public void onRightClickMajor(ItemStack sword, PlayerEntity player, ItemStack rune) {
			CooldownTracker cdTracker = player.getCooldownTracker();
			if (cdTracker.hasCooldown(rune.getItem()))
				return;

			Runes.getRunes(sword).ifPresent(runes -> {
				World world = player.world;
				Map<Enchantment, Integer> enchantments = getEnchantments(minorMagicRunes(runes));
				int multishot = enchantments.getOrDefault(Enchantments.MULTISHOT, 0);
				float inaccuracy = 1 - enchantments.getOrDefault(Enchantments.POWER, 0) / 15f;
				float speed = 0.5f + enchantments.getOrDefault(Enchantments.SOUL_SPEED, 0) * 0.05f;

				int quickCharge = enchantments.getOrDefault(Enchantments.QUICK_CHARGE, 0);
				cdTracker.setCooldown(rune.getItem(), (int) (COOLDOWN * (1 - quickCharge * 0.05)));

				world.playSound(null, player.getPosition(), Main.PROJECTILE_LAUNCH_SOUND, SoundCategory.PLAYERS, 1,
						Helper.soundPitch(random));

				if (multishot <= 0) {
					Vector3d direction = Vector3d.fromPitchYaw(player.getPitchYaw());
					Vector3d position = player.getPositionVec().add(direction.getX() * 1, 1.2, direction.getZ() * 1);
					shootMagicBall(player, world, enchantments, position, player.rotationPitch, player.rotationYaw,
							inaccuracy, speed);
				} else { // Multishot
					int count = random.nextInt(multishot) + 1;
					for (int i = 0; i < count; i++) {
						Vector3d direction = Vector3d.fromPitchYaw(player.getPitchYaw())
								.rotateYaw((random.nextFloat() - 0.5f) * 0.1f);
						Vector3d position = player.getPositionVec().add(direction.getX() * 1,
								1.2 + (random.nextDouble() - 0.5) * 0.5, direction.getZ() * 1);
						shootMagicBall(player, world, enchantments, position,
								player.rotationPitch + random.nextInt(30) - 15,
								player.rotationYaw + random.nextInt(30) - 15, inaccuracy, speed);
					}
				}

				// Underwater breathing buff
				int luckOfTheSea = enchantments.getOrDefault(Enchantments.LUCK_OF_THE_SEA, 0);
				if (luckOfTheSea > 0)
					player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, luckOfTheSea * 3 * 20));

			});
		}

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return enchantment.getRegistryName().getNamespace().equals("minecraft");
		}

	}

	public static void knockback(int knockback, int punch, Vector3d direction, Entity target) {
		Vector3d motion = direction.scale(knockback * 0.15);
		target.addVelocity(motion.x, punch * 0.05, motion.z);
		target.setOnGround(false);
	}

	public static void leaveTrail(World world, BlockPos pos, BlockState trail) {
		for (int i = 0; i < 5; i++) {
			if (!world.isAirBlock(pos))
				return;

			BlockState state = world.getBlockState(pos.down());
			if (state.isSolid() && state.isNormalCube(world, pos.down()) && trail.isValidPosition(world, pos)) {
				world.setBlockState(pos, trail);
				return;
			}
			pos = pos.down();
		}
	}

	public static void bonusExp(World world, int looting, Vector3d pos) {
		if (looting <= 0)
			return;

		int exp = world.rand.nextInt(looting + 1);

		while (exp > 0) {
			int fragment = ExperienceOrbEntity.getXPSplit(exp);
			exp -= fragment;
			world.addEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, fragment));
		}
	}

	private static void shootMagicBall(PlayerEntity player, World world, Map<Enchantment, Integer> enchantments,
			Vector3d position, float pitch, float yaw, float inaccuracy, float speed) {
		MagicBallEntity ball = new MagicBallEntity(position.getX(), position.getY(), position.getZ(), world,
				enchantments);
		ball.setShooter(player);
		ball.func_234612_a_(player, pitch, yaw, 0, speed, inaccuracy); // shoot()
		world.addEntity(ball);
	}

	private static Set<ItemStack> minorMagicRunes(Runes runes) {
		Set<ItemStack> minorRunes = new HashSet<>();
		for (int i = Runes.FIRST_MINOR_SLOT; i < Runes.RUNES_COUNT; i++) {
			ItemStack rune = runes.getStackInSlot(i);
			if (rune.getItem() instanceof MagicRuneItem)
				minorRunes.add(rune);
		}
		return minorRunes;
	}

	public static void applyMagicDamage(Entity target, DamageSource source, float damage,
			Map<Enchantment, Integer> enchants, Random rand, float multiplier) {
		if (target instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) target;
			if (living.getCreatureAttribute() == CreatureAttribute.ARTHROPOD)
				damage += enchants.getOrDefault(Enchantments.BANE_OF_ARTHROPODS, 0) * 0.4 * multiplier;
			else if (living.getCreatureAttribute() == CreatureAttribute.UNDEAD)
				damage += enchants.getOrDefault(Enchantments.SMITE, 0) * 0.4 * multiplier;
			else if (living.getCreatureAttribute() == CreatureAttribute.WATER)
				damage += enchants.getOrDefault(Enchantments.IMPALING, 0) * 0.4 * multiplier;
		}
		damage += enchants.getOrDefault(Enchantments.SHARPNESS, 0) * 0.3 * multiplier;

		if (target.isInWater())
			damage += enchants.getOrDefault(Enchantments.AQUA_AFFINITY, 0) * 2 * multiplier;

		// Crit
		if (rand.nextDouble() < enchants.getOrDefault(Enchantments.FORTUNE, 0) * 0.045)
			damage *= 2;

		target.attackEntityFrom(source, damage);
	}

	public static ListNBT serializeEnchantments(Map<Enchantment, Integer> enchantments) {
		ListNBT list = new ListNBT();
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			CompoundNBT compound = new CompoundNBT();
			compound.putString("ench", entry.getKey().getRegistryName().toString());
			compound.putInt("level", entry.getValue());
			list.add(compound);
		}
		return list;
	}

	public static Map<Enchantment, Integer> deserializeEnchantments(ListNBT list) {
		Map<Enchantment, Integer> enchantments = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			CompoundNBT compound = list.getCompound(i);
			if (!compound.contains("ench") || !compound.contains("level"))
				continue;
			ResourceLocation ench = new ResourceLocation(compound.getString("ench"));
			if (!ForgeRegistries.ENCHANTMENTS.containsKey(ench))
				continue;
			Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ench);
			int level = compound.getInt("level");
			enchantments.put(enchantment, level);
		}
		return enchantments;
	}

	public static Color getRandEnchColor(Random rand, Enchantment[] enchantments) {
		Color color = DEFAULT_COLOR;
		if (enchantments.length > 0)
			color = MagicRuneItem.ENCHANTMENT_COLORS.getOrDefault(enchantments[rand.nextInt(enchantments.length)],
					MagicRuneItem.DEFAULT_COLOR);
		return color;
	}

	private static final Map<Enchantment, Color> ENCHANTMENT_COLORS = new HashMap<>();
	private static final Color DEFAULT_COLOR = new Color(100, 0, 100, 255);

	static {
		ENCHANTMENT_COLORS.put(Enchantments.AQUA_AFFINITY, new Color(0, 0, 200));
		ENCHANTMENT_COLORS.put(Enchantments.BANE_OF_ARTHROPODS, new Color(0, 0, 0));
		ENCHANTMENT_COLORS.put(Enchantments.BLAST_PROTECTION, new Color(50, 20, 0));
		ENCHANTMENT_COLORS.put(Enchantments.CHANNELING, new Color(255, 255, 0));
		ENCHANTMENT_COLORS.put(Enchantments.DEPTH_STRIDER, new Color(10, 10, 130));
		ENCHANTMENT_COLORS.put(Enchantments.EFFICIENCY, new Color(180, 180, 180));
		ENCHANTMENT_COLORS.put(Enchantments.FEATHER_FALLING, new Color(150, 220, 220));
		ENCHANTMENT_COLORS.put(Enchantments.FIRE_ASPECT, new Color(255, 130, 0));
		ENCHANTMENT_COLORS.put(Enchantments.FIRE_PROTECTION, new Color(200, 130, 70));
		ENCHANTMENT_COLORS.put(Enchantments.FLAME, new Color(255, 130, 0));
		ENCHANTMENT_COLORS.put(Enchantments.FORTUNE, new Color(220, 220, 20));
		ENCHANTMENT_COLORS.put(Enchantments.FROST_WALKER, new Color(23, 220, 200));
		ENCHANTMENT_COLORS.put(Enchantments.IMPALING, new Color(30, 30, 30));
		ENCHANTMENT_COLORS.put(Enchantments.INFINITY, new Color(255, 255, 255));
		ENCHANTMENT_COLORS.put(Enchantments.KNOCKBACK, new Color(100, 100, 100));
		ENCHANTMENT_COLORS.put(Enchantments.LOOTING, new Color(190, 180, 40));
		ENCHANTMENT_COLORS.put(Enchantments.LOYALTY, new Color(140, 80, 80));
		ENCHANTMENT_COLORS.put(Enchantments.LUCK_OF_THE_SEA, new Color(30, 90, 150));
		ENCHANTMENT_COLORS.put(Enchantments.LURE, new Color(40, 70, 150));
		ENCHANTMENT_COLORS.put(Enchantments.MENDING, new Color(40, 130, 50));
		ENCHANTMENT_COLORS.put(Enchantments.MULTISHOT, new Color(170, 190, 180));
		ENCHANTMENT_COLORS.put(Enchantments.PIERCING, new Color(200, 200, 200));
		ENCHANTMENT_COLORS.put(Enchantments.POWER, new Color(255, 20, 20));
		ENCHANTMENT_COLORS.put(Enchantments.PROJECTILE_PROTECTION, new Color(200, 200, 200));
		ENCHANTMENT_COLORS.put(Enchantments.PROTECTION, new Color(200, 200, 200));
		ENCHANTMENT_COLORS.put(Enchantments.PUNCH, new Color(100, 100, 100));
		ENCHANTMENT_COLORS.put(Enchantments.QUICK_CHARGE, new Color(240, 70, 0));
		ENCHANTMENT_COLORS.put(Enchantments.RESPIRATION, new Color(190, 255, 240));
		ENCHANTMENT_COLORS.put(Enchantments.RIPTIDE, new Color(60, 100, 190));
		ENCHANTMENT_COLORS.put(Enchantments.SHARPNESS, new Color(230, 230, 230));
		ENCHANTMENT_COLORS.put(Enchantments.SILK_TOUCH, new Color(245, 245, 245));
		ENCHANTMENT_COLORS.put(Enchantments.SMITE, new Color(255, 230, 40));
		ENCHANTMENT_COLORS.put(Enchantments.SOUL_SPEED, new Color(50, 50, 40));
		ENCHANTMENT_COLORS.put(Enchantments.SWEEPING, new Color(200, 200, 200));
		ENCHANTMENT_COLORS.put(Enchantments.THORNS, new Color(0, 100, 20));
		ENCHANTMENT_COLORS.put(Enchantments.UNBREAKING, new Color(200, 200, 200));
	}
}
