package mod.vemerion.runesword.entity;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import mod.vemerion.runesword.helpers.Helper;
import mod.vemerion.runesword.init.ModEntities;
import mod.vemerion.runesword.init.ModSounds;
import mod.vemerion.runesword.item.MagicRuneItem;
import mod.vemerion.runesword.particle.MagicBallParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class MagicBallEntity extends AbstractArrow implements IEntityAdditionalSpawnData {

	private static final int MAX_DURATION = 20 * 1;

	private int duration;
	private Map<Enchantment, Integer> enchantments;
	private Enchantment[] enchantmentArr;
	private boolean boomerang;
	private BlockPos startPos;

	public MagicBallEntity(EntityType<? extends MagicBallEntity> entityTypeIn, Level level) {
		super(entityTypeIn, level);
		this.setBaseDamage(4);
		this.setNoGravity(true);
		this.enchantments = new HashMap<>();
		this.enchantmentArr = new Enchantment[0];
		this.startPos = new BlockPos(getX(), getY(), getZ());
		this.setSoundEvent(ModSounds.PROJECTILE_IMPACT.get());
	}

	public MagicBallEntity(double x, double y, double z, Level level, Map<Enchantment, Integer> enchantments) {
		super(ModEntities.MAGIC_BALL.get(), x, y, z, level);
		this.setBaseDamage(4);
		this.setNoGravity(true);
		this.enchantments = enchantments;
		this.enchantmentArr = enchantments.keySet().toArray(new Enchantment[0]);
		this.boomerang = random.nextDouble() < getEnchantmentLevel(Enchantments.LOYALTY) * 0.1;
		this.startPos = new BlockPos(getX(), getY(), getZ());
		this.setSoundEvent(ModSounds.PROJECTILE_IMPACT.get());
	}

	private int getEnchantmentLevel(Enchantment enchantment) {
		return enchantments.getOrDefault(enchantment, 0);
	}

	@Override
	public void tick() {
		super.tick();

		duration++;

		if (!level.isClientSide) {
			if (duration > getDuration()) {
				discard();
			}

			if (random.nextDouble() < getEnchantmentLevel(Enchantments.FIRE_PROTECTION) * 0.01)
				MagicRuneItem.leaveTrail(level, blockPosition(), Blocks.FIRE.defaultBlockState());
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.FROST_WALKER) * 0.03)
				MagicRuneItem.leaveTrail(level, blockPosition(), Blocks.SNOW.defaultBlockState());

			deflectProjectiles();
		} else {
			createParticles();
		}

		if (duration == getDuration() / 2 && boomerang)
			setDeltaMovement(getDeltaMovement().scale(-1));
	}

	private void deflectProjectiles() {
		int projectileProt = getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION);
		if (projectileProt <= 0)
			return;

		var projectiles = level.getEntitiesOfClass(Projectile.class,
				getBoundingBox().inflate(1), e -> {
					if (e == this)
						return false;

					Entity shooter = getOwner();
					Entity otherShooter = e.getOwner();
					return shooter == null || otherShooter == null || shooter != otherShooter;
				});
		for (Projectile projectile : projectiles) {
			if (random.nextDouble() < projectileProt * 0.01)
				projectile.discard();
		}
	}

	private int getDuration() {
		return MAX_DURATION * (getEnchantmentLevel(Enchantments.INFINITY_ARROWS) + 1);
	}

	@Override
	protected float getWaterInertia() {
		return 0.95f + getEnchantmentLevel(Enchantments.DEPTH_STRIDER) * 0.008f;
	}

	private void createParticles() {
		for (int i = 0; i < 10; i++) {
			Color color = MagicRuneItem.getRandEnchColor(random, enchantmentArr);
			Vec3 pos = new Vec3(getX() + randCoord(), getY() + getBbHeight() / 2 + randCoord(),
					getZ() + randCoord());
			level.addParticle(
					new MagicBallParticleData(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f),
					pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

	private double randCoord() {
		return (random.nextDouble() - 0.5) * 0.5;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("duration"))
			duration = compound.getInt("duration");
		if (compound.contains("enchantments"))
			initEnchantments(compound.getList("enchantments", Tag.TAG_COMPOUND));
		if (compound.contains("boomerang"))
			boomerang = compound.getBoolean("boomerang");
		if (compound.contains("startPos"))
			startPos = NbtUtils.readBlockPos(compound.getCompound("startPos"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("duration", duration);
		compound.put("enchantments", MagicRuneItem.serializeEnchantments(enchantments));
		compound.putBoolean("boomerang", boomerang);
		compound.put("startPos", NbtUtils.writeBlockPos(startPos));
	}

	private void initEnchantments(ListTag list) {
		enchantments = MagicRuneItem.deserializeEnchantments(list);
		enchantmentArr = enchantments.keySet().toArray(new Enchantment[0]);
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return ModSounds.PROJECTILE_IMPACT.get();
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);

		Vec3 pos = result.getLocation();
		Entity shooter = getOwner();
		if (!level.isClientSide) {
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.BLAST_PROTECTION) * 0.05)
				level.explode(null, pos.x, pos.y, pos.z, 2, Level.ExplosionInteraction.MOB);

			// AoE
			int sweeping = getEnchantmentLevel(Enchantments.SWEEPING_EDGE);
			Entity target = result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) result).getEntity()
					: null;
			if (sweeping > 0) {
				DamageSource source = shooter == null ? Helper.magicDamage() : Helper.magicDamage(this, shooter);
				for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class,
						getBoundingBox().inflate(sweeping * 0.2), e -> e != shooter && e != target)) {
					e.hurt(source, 2);
				}
			}
		}

		// Pull
		int lure = getEnchantmentLevel(Enchantments.FISHING_SPEED);
		if (lure > 0) {
			for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(lure * 0.2),
					e -> e != shooter)) {
				Vec3 direction = pos.subtract(e.position()).normalize();
				e.push(direction.x, direction.y, direction.z);
			}
		}

		// Riptide
		if (shooter != null && random.nextDouble() < getEnchantmentLevel(Enchantments.RIPTIDE)) {
			Vec3 direction = pos.subtract(shooter.position()).normalize();
			shooter.push(direction.x, direction.y, direction.z);
		}

	}

	@Override
	protected void onHitBlock(BlockHitResult result) { // onHitBlock
		super.onHitBlock(result);
		discard();
	}

	// Reminder for self: 1 damage = half heart
	@Override
	protected void onHitEntity(EntityHitResult result) {
		playSound(getDefaultHitGroundSoundEvent(), 1, Helper.soundPitch(random));

		if (!level.isClientSide) {
			Entity target = result.getEntity();
			DamageSource source = Helper.magicDamage();

			if (getOwner() != null && getOwner() instanceof Player) { // getShooter()
				Player player = (Player) getOwner();
				source = Helper.magicDamage(this, player);

				// Restore air
				int respiration = getEnchantmentLevel(Enchantments.RESPIRATION);
				int air = player.getAirSupply() + (int) ((respiration * 0.34) * ((float) player.getMaxAirSupply() / 10));
				player.setAirSupply(Math.min(player.getMaxAirSupply(), air));

				// Lightning
				if (random.nextDouble() < getEnchantmentLevel(Enchantments.CHANNELING) * 0.15) {
					var lightning = EntityType.LIGHTNING_BOLT.create(level);
					lightning.moveTo(Vec3.atBottomCenterOf(target.blockPosition()));
					lightning.setCause((ServerPlayer) player);
					level.addFreshEntity(lightning);
				}

				// Heal
				if (random.nextDouble() < getEnchantmentLevel(Enchantments.MENDING) * 0.33) {
					player.heal(2);
				}

				// Protection buff
				if (random.nextDouble() < getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION) * 0.015)
					player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 10));
			}

			// Bypass armor
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.UNBREAKING) * 0.01) {
				source.bypassArmor();
			}

			// Potion effects
			if (target instanceof LivingEntity) {
				LivingEntity living = (LivingEntity) target;
				if (random.nextDouble() < getEnchantmentLevel(Enchantments.FALL_PROTECTION) * 0.015)
					living.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20 * 3));
				if (random.nextDouble() < getEnchantmentLevel(Enchantments.THORNS) * 0.03)
					living.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 7));
			}

			MagicRuneItem.applyMagicDamage(target, source, (float) getBaseDamage() + distanceDamage(), enchantments, random,
					1);

			// Drops
			if (!target.isAlive() && level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
				spawnEgg(target);
				MagicRuneItem.bonusExp(level, getEnchantmentLevel(Enchantments.MOB_LOOTING), position());
			}

			// Fire
			if (random.nextDouble() < fireChance()) {
				target.setSecondsOnFire(3);
			}

			// Knockback
			MagicRuneItem.knockback(getEnchantmentLevel(Enchantments.KNOCKBACK), getEnchantmentLevel(Enchantments.PUNCH_ARROWS), getDeltaMovement(), target);

			// Piercing
			if (random.nextDouble() >= getEnchantmentLevel(Enchantments.PIERCING) * 0.04)
				discard();
		}
	}

	private void spawnEgg(Entity target) {
		if (!target.canChangeDimensions() || random.nextDouble() >= getEnchantmentLevel(Enchantments.SILK_TOUCH) * 0.01)
			return;

		SpawnEggItem egg = null;
		for (SpawnEggItem e : SpawnEggItem.eggs()) {
			if (e.getType(null) == target.getType()) {
				egg = e;
				break;
			}
		}
		if (egg != null) {
			ItemEntity eggEntity = new ItemEntity(level, target.getX(), target.getY(), target.getZ(),
					new ItemStack(egg));
			level.addFreshEntity(eggEntity);
		}
	}

	private double fireChance() {
		return getEnchantmentLevel(Enchantments.FIRE_ASPECT) * 0.08 + getEnchantmentLevel(Enchantments.FLAMING_ARROWS) * 0.16;
	}

	private float distanceDamage() {
		int efficiency = getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
		return efficiency / 15f * (float) position().distanceTo(Vec3.atCenterOf(startPos)) * 0.3f;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		CompoundTag compound = new CompoundTag();
		compound.put("enchantments", MagicRuneItem.serializeEnchantments(enchantments));
		buffer.writeNbt(compound);
		buffer.writeBoolean(boomerang);

		// Shooter
		CompoundTag shooterNBT = new CompoundTag();
		Entity shooter = getOwner();
		shooterNBT.putBoolean("exists", shooter != null && shooter instanceof Player);
		if (shooter != null)
			shooterNBT.putUUID("uuid", shooter.getUUID());
		buffer.writeNbt(shooterNBT);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		CompoundTag compound = buffer.readNbt();
		ListTag list = compound.getList("enchantments", Tag.TAG_COMPOUND);
		initEnchantments(list);
		boomerang = buffer.readBoolean();

		// Shooter
		CompoundTag shooterNBT = buffer.readNbt();
		if (shooterNBT.getBoolean("exists")) {
			setOwner(level.getPlayerByUUID(shooterNBT.getUUID("uuid")));
		}
	}

}
