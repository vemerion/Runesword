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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
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
		public float onHurtMajor(ItemStack runeable, Player player, DamageSource source, float amount,
				ItemStack rune) {
			Runes.getRunes(runeable).ifPresent(runes -> {
				Map<Enchantment, Integer> enchants = getEnchantments(minorMagicRunes(runes));
				if (source.getEntity() instanceof LivingEntity
						&& player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, enchants) * 0.01)
					magicArea(enchants, player, player.level);
			});
			return super.onHurtMajor(runeable, player, source, amount, rune);
		}

		@Override
		public void onRightClickMajor(ItemStack runeable, Player player, ItemStack rune) {
			var cdTracker = player.getCooldowns();
			if (cdTracker.isOnCooldown(rune.getItem()))
				return;
			
			Runes.getRunes(runeable).ifPresent(runes -> {
				Level level = player.level;
				Map<Enchantment, Integer> enchants = getEnchantments(minorMagicRunes(runes));
				
				int cooldown = START_COOLDOWN;
				int quickCharge = getEnchantmentLevel(Enchantments.QUICK_CHARGE, enchants);
				cooldown *= (1 - quickCharge * 0.05);
				cooldown += getEnchantmentLevel(Enchantments.INFINITY_ARROWS, enchants) * 20;
				cdTracker.addCooldown(rune.getItem(), cooldown);

				magicArea(enchants, player, level);
			});
		}

		private void magicArea(Map<Enchantment, Integer> enchants, Player player, Level level) {
			level.playSound(null, player.blockPosition(), Main.PROJECTILE_IMPACT_SOUND, SoundSource.PLAYERS, 1,
					Helper.soundPitch(player.getRandom()));
			
			double radius = START_RADIUS;
			float damage = START_DAMAGE;
			DamageSource source = Helper.magicDamage(player);

			// More radius in water with depth strider
			if (player.isInWater())
				radius += getEnchantmentLevel(Enchantments.DEPTH_STRIDER, enchants) / 9d;

			// More radius with sweeping
			radius += getEnchantmentLevel(Enchantments.SWEEPING_EDGE, enchants) / 9d;
			
			// More radius if riding
			if (player.getVehicle() instanceof Horse)
				radius += getEnchantmentLevel(Enchantments.LOYALTY, enchants) / 6d;

			// Bypass armor
			if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.UNBREAKING, enchants) * 0.1) {
				source.bypassArmor();
			}

			// More damage if full attack strength
			if (player.getAttackStrengthScale(0) > 0.99)
				damage += getEnchantmentLevel(Enchantments.POWER_ARROWS, enchants) * 0.5;
			
			// More damage in nether with flame
			if (level.dimension() == Level.NETHER)
				damage += getEnchantmentLevel(Enchantments.FLAMING_ARROWS, enchants);
			
			// More damage with speed effect
			if (player.hasEffect(MobEffects.MOVEMENT_SPEED))
				damage += getEnchantmentLevel(Enchantments.SOUL_SPEED, enchants) * 0.4;
			
			// More damage with infinity
			damage += getEnchantmentLevel(Enchantments.INFINITY_ARROWS, enchants) * 2;


			var box = new AABB(player.position(), player.position())
					.inflate(radius, 0, radius).expandTowards(0, 2, 0);
			int channeling = getEnchantmentLevel(Enchantments.CHANNELING, enchants);
			int lure = getEnchantmentLevel(Enchantments.FISHING_SPEED, enchants);
			int looting = getEnchantmentLevel(Enchantments.MOB_LOOTING, enchants);
			int knockback = getEnchantmentLevel(Enchantments.KNOCKBACK, enchants);
			int punch = getEnchantmentLevel(Enchantments.PUNCH_ARROWS, enchants);
			for (Entity e : level.getEntities(player, box, e -> e != player.getVehicle())) {
				// More damage further away with channeling
				damage += channeling * player.distanceTo(e) * 0.3f;
				
				// More damage if mob has armor
				if (hasArmor(e))
					damage += getEnchantmentLevel(Enchantments.PIERCING, enchants) * 0.4;

				applyMagicDamage(e, source, damage, enchants, player.getRandom(), 0.7f);

				// Pull
				Vec3 inwards = player.position().subtract(e.position()).normalize();
				Vec3 pull = inwards.scale(lure / 9d);
				e.push(pull.x, pull.y, pull.z);

				// Fire
				if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.FIRE_ASPECT, enchants) * 0.08)
					e.setSecondsOnFire(3);

				// Exp
				if (!e.isAlive() && level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
					bonusExp(level, looting, e.position());

				// Knockback
				knockback(knockback, punch, inwards.scale(-1), e);

				// Remove projectiles
				if (e instanceof Projectile && player.getRandom()
						.nextDouble() < getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, enchants) * 0.05)
					e.discard();
			}

			// Explosion
			if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.BLAST_PROTECTION, enchants) * 0.025)
				level.explode(player, player.getX(), player.getY(), player.getZ(), 2, Explosion.BlockInteraction.BREAK);

			int efficiency = getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, enchants);
			int thorns = getEnchantmentLevel(Enchantments.THORNS, enchants);
			int fireProt = getEnchantmentLevel(Enchantments.FIRE_PROTECTION, enchants);
			int frostWalker = getEnchantmentLevel(Enchantments.FROST_WALKER, enchants);
			int silkTouch = getEnchantmentLevel(Enchantments.SILK_TOUCH, enchants);
			int luckSea = getEnchantmentLevel(Enchantments.FISHING_LUCK, enchants);
			BlockPos.betweenClosedStream(box).forEach(p -> {

				// Break wood
				BlockState state = level.getBlockState(p);
				if (state.is(BlockTags.MINEABLE_WITH_AXE) && player.getRandom().nextDouble() < efficiency * 0.03)
					level.destroyBlock(p, true);

				// Trail
				if (player.getRandom().nextDouble() < thorns * 0.02)
					leaveTrail(level, p, Blocks.OAK_SAPLING.defaultBlockState());
				else if (player.getRandom().nextDouble() < fireProt * 0.01)
					leaveTrail(level, p, Blocks.FIRE.defaultBlockState());
				else if (player.getRandom().nextDouble() < frostWalker * 0.03)
					leaveTrail(level, p, Blocks.SNOW.defaultBlockState());
				else if (player.getRandom().nextDouble() < luckSea * 0.02 && player.isInWater())
					leaveTrail(level, p, Blocks.SEAGRASS.defaultBlockState());

				// Bonemeal
				if (player.getRandom().nextDouble() < silkTouch * 0.1)
					BoneMealItem.applyBonemeal(Items.BONE_MEAL.getDefaultInstance(), level, p, player);
			});

			// Heal player
			if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.MENDING, enchants) / 3f)
				player.heal(1);
			
			// Restore air
			int respiration = getEnchantmentLevel(Enchantments.RESPIRATION, enchants);
			restoreAir(player, respiration * 0.03f);

			// Send out magic ball
			if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.MULTISHOT, enchants) / 3f) {
				float pitch = player.getRandom().nextFloat() * 360;
				float yaw = player.getRandom().nextFloat() * 360;
				Vec3 position = player.position().add(0, 1.5, 0);
				shootMagicBall(player, level, enchants, position, pitch, yaw, 0, 0.5f);
			}

			// Send particle message
			Network.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
					new AxeMagicPowersMessage(enchants, player.position(), radius));
		}

		private boolean hasArmor(Entity e) {
			for (ItemStack stack : e.getArmorSlots())
				if (!stack.isEmpty())
					return true;
			return false;
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final int COOLDOWN = 20 * 10;

		@Override
		public void onRightClickMajor(ItemStack sword, Player player, ItemStack rune) {
			var cdTracker = player.getCooldowns();
			if (cdTracker.isOnCooldown(rune.getItem()))
				return;

			Runes.getRunes(sword).ifPresent(runes -> {
				var level = player.level;
				var random = player.getRandom();
				Map<Enchantment, Integer> enchantments = getEnchantments(minorMagicRunes(runes));
				int multishot = enchantments.getOrDefault(Enchantments.MULTISHOT, 0);
				float inaccuracy = 1 - enchantments.getOrDefault(Enchantments.POWER_ARROWS, 0) / 15f;
				float speed = 0.5f + enchantments.getOrDefault(Enchantments.SOUL_SPEED, 0) * 0.05f;

				int quickCharge = enchantments.getOrDefault(Enchantments.QUICK_CHARGE, 0);
				cdTracker.addCooldown(rune.getItem(), (int) (COOLDOWN * (1 - quickCharge * 0.05)));

				level.playSound(null, player.blockPosition(), Main.PROJECTILE_LAUNCH_SOUND, SoundSource.PLAYERS, 1,
						Helper.soundPitch(random));

				if (multishot <= 0) {
					Vec3 direction = Vec3.directionFromRotation(player.getRotationVector());
					Vec3 position = player.position().add(direction.x() * 1, 1.2, direction.z() * 1);
					shootMagicBall(player, level, enchantments, position, player.getXRot(), player.getYRot(),
							inaccuracy, speed);
				} else { // Multishot
					int count = random.nextInt(multishot) + 1;
					for (int i = 0; i < count; i++) {
						Vec3 direction = Vec3.directionFromRotation(player.getRotationVector())
								.yRot((random.nextFloat() - 0.5f) * 0.1f);
						Vec3 position = player.position().add(direction.x() * 1,
								1.2 + (random.nextDouble() - 0.5) * 0.5, direction.z() * 1);
						shootMagicBall(player, level, enchantments, position,
								player.getXRot() + random.nextInt(30) - 15,
								player.getYRot() + random.nextInt(30) - 15, inaccuracy, speed);
					}
				}

				// Underwater breathing buff
				int luckOfTheSea = enchantments.getOrDefault(Enchantments.FISHING_LUCK, 0);
				if (luckOfTheSea > 0)
					player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, luckOfTheSea * 3 * 20));

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

	public static void knockback(int knockback, int punch, Vec3 direction, Entity target) {
		Vec3 motion = direction.scale(knockback * 0.15);
		target.push(motion.x, punch * 0.05, motion.z);
		target.setOnGround(false);
	}

	public static void leaveTrail(Level level, BlockPos pos, BlockState trail) {
		for (int i = 0; i < 5; i++) {
			if (!level.isEmptyBlock(pos))
				return;

			BlockState state = level.getBlockState(pos.below());
			if (state.canOcclude() && state.isRedstoneConductor(level, pos.below()) && trail.canSurvive(level, pos)) {
				level.setBlockAndUpdate(pos, trail);
				return;
			}
			pos = pos.below();
		}
	}

	public static void bonusExp(Level level, int looting, Vec3 pos) {
		if (looting <= 0)
			return;

		int exp = level.random.nextInt(looting + 1);

		while (exp > 0) {
			int fragment = ExperienceOrb.getExperienceValue(exp);
			exp -= fragment;
			level.addFreshEntity(new ExperienceOrb(level, pos.x, pos.y, pos.z, fragment));
		}
	}

	private static void shootMagicBall(Player player, Level level, Map<Enchantment, Integer> enchantments,
			Vec3 position, float pitch, float yaw, float inaccuracy, float speed) {
		MagicBallEntity ball = new MagicBallEntity(position.x(), position.y(), position.z(), level,
				enchantments);
		ball.setOwner(player);
		ball.shootFromRotation(player, pitch, yaw, 0, speed, inaccuracy); // shoot()
		level.addFreshEntity(ball);
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
			if (living.getMobType() == MobType.ARTHROPOD)
				damage += enchants.getOrDefault(Enchantments.BANE_OF_ARTHROPODS, 0) * 0.4 * multiplier;
			else if (living.getMobType() == MobType.UNDEAD)
				damage += enchants.getOrDefault(Enchantments.SMITE, 0) * 0.4 * multiplier;
			else if (living.getMobType() == MobType.WATER)
				damage += enchants.getOrDefault(Enchantments.IMPALING, 0) * 0.4 * multiplier;
		}
		damage += enchants.getOrDefault(Enchantments.SHARPNESS, 0) * 0.3 * multiplier;

		if (target.isInWater())
			damage += enchants.getOrDefault(Enchantments.AQUA_AFFINITY, 0) * 2 * multiplier;

		// Crit
		if (rand.nextDouble() < enchants.getOrDefault(Enchantments.BLOCK_FORTUNE, 0) * 0.045)
			damage *= 2;

		target.hurt(source, damage);
	}

	public static ListTag serializeEnchantments(Map<Enchantment, Integer> enchantments) {
		ListTag list = new ListTag();
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			CompoundTag compound = new CompoundTag();
			compound.putString("ench", entry.getKey().getRegistryName().toString());
			compound.putInt("level", entry.getValue());
			list.add(compound);
		}
		return list;
	}

	public static Map<Enchantment, Integer> deserializeEnchantments(ListTag list) {
		Map<Enchantment, Integer> enchantments = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			CompoundTag compound = list.getCompound(i);
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
		ENCHANTMENT_COLORS.put(Enchantments.BLOCK_EFFICIENCY, new Color(180, 180, 180));
		ENCHANTMENT_COLORS.put(Enchantments.FALL_PROTECTION, new Color(150, 220, 220));
		ENCHANTMENT_COLORS.put(Enchantments.FIRE_ASPECT, new Color(255, 130, 0));
		ENCHANTMENT_COLORS.put(Enchantments.FIRE_PROTECTION, new Color(200, 130, 70));
		ENCHANTMENT_COLORS.put(Enchantments.FLAMING_ARROWS, new Color(255, 130, 0));
		ENCHANTMENT_COLORS.put(Enchantments.BLOCK_FORTUNE, new Color(220, 220, 20));
		ENCHANTMENT_COLORS.put(Enchantments.FROST_WALKER, new Color(23, 220, 200));
		ENCHANTMENT_COLORS.put(Enchantments.IMPALING, new Color(30, 30, 30));
		ENCHANTMENT_COLORS.put(Enchantments.INFINITY_ARROWS, new Color(255, 255, 255));
		ENCHANTMENT_COLORS.put(Enchantments.KNOCKBACK, new Color(100, 100, 100));
		ENCHANTMENT_COLORS.put(Enchantments.MOB_LOOTING, new Color(190, 180, 40));
		ENCHANTMENT_COLORS.put(Enchantments.LOYALTY, new Color(140, 80, 80));
		ENCHANTMENT_COLORS.put(Enchantments.FISHING_LUCK, new Color(30, 90, 150));
		ENCHANTMENT_COLORS.put(Enchantments.FISHING_SPEED, new Color(40, 70, 150));
		ENCHANTMENT_COLORS.put(Enchantments.MENDING, new Color(40, 130, 50));
		ENCHANTMENT_COLORS.put(Enchantments.MULTISHOT, new Color(170, 190, 180));
		ENCHANTMENT_COLORS.put(Enchantments.PIERCING, new Color(200, 200, 200));
		ENCHANTMENT_COLORS.put(Enchantments.POWER_ARROWS, new Color(255, 20, 20));
		ENCHANTMENT_COLORS.put(Enchantments.PROJECTILE_PROTECTION, new Color(200, 200, 200));
		ENCHANTMENT_COLORS.put(Enchantments.ALL_DAMAGE_PROTECTION, new Color(200, 200, 200));
		ENCHANTMENT_COLORS.put(Enchantments.PUNCH_ARROWS, new Color(100, 100, 100));
		ENCHANTMENT_COLORS.put(Enchantments.QUICK_CHARGE, new Color(240, 70, 0));
		ENCHANTMENT_COLORS.put(Enchantments.RESPIRATION, new Color(190, 255, 240));
		ENCHANTMENT_COLORS.put(Enchantments.RIPTIDE, new Color(60, 100, 190));
		ENCHANTMENT_COLORS.put(Enchantments.SHARPNESS, new Color(230, 230, 230));
		ENCHANTMENT_COLORS.put(Enchantments.SILK_TOUCH, new Color(245, 245, 245));
		ENCHANTMENT_COLORS.put(Enchantments.SMITE, new Color(255, 230, 40));
		ENCHANTMENT_COLORS.put(Enchantments.SOUL_SPEED, new Color(50, 50, 40));
		ENCHANTMENT_COLORS.put(Enchantments.SWEEPING_EDGE, new Color(200, 200, 200));
		ENCHANTMENT_COLORS.put(Enchantments.THORNS, new Color(0, 100, 20));
		ENCHANTMENT_COLORS.put(Enchantments.UNBREAKING, new Color(200, 200, 200));
	}
}
