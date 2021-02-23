package mod.vemerion.runesword.entity;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.helpers.Helper;
import mod.vemerion.runesword.particle.MagicBallParticleData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicBallEntity extends AbstractArrowEntity implements IEntityAdditionalSpawnData {

	private static final int MAX_DURATION = 20 * 1;
	public static final Color DEFAULT_COLOR = new Color(100, 0, 100, 255);

	private static final Map<Enchantment, Color> ENCHANTMENT_COLORS = new HashMap<>();

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

	private int duration;
	private Map<Enchantment, Integer> enchantments;
	private Enchantment[] enchantmentArr;
	private boolean boomerang;
	private BlockPos startPos;

	public MagicBallEntity(EntityType<? extends MagicBallEntity> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
		this.setDamage(4);
		this.setNoGravity(true);
		this.enchantments = new HashMap<>();
		this.enchantmentArr = new Enchantment[0];
		this.startPos = new BlockPos(getPosX(), getPosY(), getPosZ());
	}

	public MagicBallEntity(double x, double y, double z, World world, Map<Enchantment, Integer> enchantments) {
		super(Main.MAGIC_BALL_ENTITY, x, y, z, world);
		this.setDamage(4);
		this.setNoGravity(true);
		this.enchantments = enchantments;
		this.enchantmentArr = enchantments.keySet().toArray(new Enchantment[0]);
		this.boomerang = rand.nextDouble() < getEnchantmentLevel(Enchantments.LOYALTY) * 0.1;
		this.startPos = new BlockPos(getPosX(), getPosY(), getPosZ());
	}

	private int getEnchantmentLevel(Enchantment enchantment) {
		return enchantments.getOrDefault(enchantment, 0);
	}

	@Override
	public void tick() {
		super.tick();

		duration++;

		if (!world.isRemote) {
			if (duration > getDuration()) {
				remove();
			}

			if (rand.nextDouble() < getEnchantmentLevel(Enchantments.FIRE_PROTECTION) * 0.01)
				leaveTrail(Blocks.FIRE.getDefaultState());
			if (rand.nextDouble() < getEnchantmentLevel(Enchantments.FROST_WALKER) * 0.03)
				leaveTrail(Blocks.SNOW.getDefaultState());

			deflectProjectiles();
		} else {
			createParticles();
		}

		if (duration == getDuration() / 2 && boomerang)
			setMotion(getMotion().scale(-1));
	}

	private void deflectProjectiles() {
		int projectileProt = getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION);
		if (projectileProt <= 0)
			return;

		List<ProjectileEntity> projectiles = world.getEntitiesWithinAABB(ProjectileEntity.class,
				getBoundingBox().grow(1), e -> {
					if (e == this)
						return false;

					Entity shooter = func_234616_v_();
					Entity otherShooter = e.func_234616_v_();
					return shooter == null || otherShooter == null || shooter != otherShooter;
				});
		for (ProjectileEntity projectile : projectiles) {
			if (rand.nextDouble() < projectileProt * 0.01)
				projectile.remove();
		}
	}

	private void leaveTrail(BlockState trail) {
		BlockPos pos = getPosition();
		for (int i = 0; i < 5; i++) {
			if (!world.isAirBlock(pos))
				return;

			BlockState state = world.getBlockState(pos.down());
			if (state.isSolid() && state.isNormalCube(world, pos)) {
				world.setBlockState(pos, trail);
				return;
			}
			pos = pos.down();
		}
	}

	private int getDuration() {
		return MAX_DURATION * (getEnchantmentLevel(Enchantments.INFINITY) + 1);
	}

	@Override
	protected float getWaterDrag() {
		return 0.95f + getEnchantmentLevel(Enchantments.DEPTH_STRIDER) * 0.008f;
	}

	private void createParticles() {
		for (int i = 0; i < 10; i++) {
			Color color = DEFAULT_COLOR;
			if (!enchantments.isEmpty())
				color = ENCHANTMENT_COLORS.getOrDefault(enchantmentArr[rand.nextInt(enchantmentArr.length)],
						DEFAULT_COLOR);
			Vector3d pos = new Vector3d(getPosX() + randCoord(), getPosY() + getHeight() / 2 + randCoord(),
					getPosZ() + randCoord());
			world.addParticle(
					new MagicBallParticleData(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f),
					pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

	private double randCoord() {
		return (rand.nextDouble() - 0.5) * 0.5;
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("duration"))
			duration = compound.getInt("duration");
		if (compound.contains("enchantments"))
			initEnchantments(compound.getList("enchantments", Constants.NBT.TAG_COMPOUND));
		if (compound.contains("boomerang"))
			boomerang = compound.getBoolean("boomerang");
		if (compound.contains("startPos"))
			startPos = NBTUtil.readBlockPos(compound.getCompound("startPos"));
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("duration", duration);
		compound.put("enchantments", serializeEnchantments(enchantments));
		compound.putBoolean("boomerang", boomerang);
		compound.put("startPos", NBTUtil.writeBlockPos(startPos));
	}

	private void initEnchantments(ListNBT list) {
		enchantments = deserializeEnchantments(list);
		enchantmentArr = enchantments.keySet().toArray(new Enchantment[0]);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);

		Vector3d pos = result.getHitVec();
		Entity shooter = func_234616_v_();
		if (!world.isRemote) {
			if (rand.nextDouble() < getEnchantmentLevel(Enchantments.BLAST_PROTECTION) * 0.04)
				world.createExplosion(null, pos.x, pos.y, pos.z, 2, Mode.BREAK);

			// AoE
			int sweeping = getEnchantmentLevel(Enchantments.SWEEPING);
			Entity target = result.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult) result).getEntity()
					: null;
			if (sweeping > 0) {
				DamageSource source = shooter == null ? Helper.magicDamage() : Helper.magicDamage(this, shooter);
				for (LivingEntity e : world.getEntitiesWithinAABB(LivingEntity.class,
						getBoundingBox().grow(sweeping * 0.2), e -> e != shooter && e != target)) {
					e.attackEntityFrom(source, 2);
				}
			}
		}

		// Pull
		int lure = getEnchantmentLevel(Enchantments.LURE);
		if (lure > 0) {
			for (LivingEntity e : world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(lure * 0.2),
					e -> e != shooter)) {
				Vector3d direction = pos.subtract(e.getPositionVec()).normalize();
				e.addVelocity(direction.x, direction.y, direction.z);
			}
		}

		// Riptide
		if (shooter != null && rand.nextDouble() < getEnchantmentLevel(Enchantments.RIPTIDE)) {
			Vector3d direction = pos.subtract(shooter.getPositionVec()).normalize();
			shooter.addVelocity(direction.x, direction.y, direction.z);
		}

	}

	@Override
	protected void func_230299_a_(BlockRayTraceResult result) { // onHitBlock
		super.func_230299_a_(result);
		remove();
	}

	// Reminder for self: 1 damage = half heart
	@Override
	protected void onEntityHit(EntityRayTraceResult result) {
		if (!world.isRemote) {
			Entity target = result.getEntity();
			DamageSource source = Helper.magicDamage();

			if (func_234616_v_() != null && func_234616_v_() instanceof PlayerEntity) { // getShooter()
				PlayerEntity player = (PlayerEntity) func_234616_v_();
				source = Helper.magicDamage(this, player);

				// Restore air
				int respiration = getEnchantmentLevel(Enchantments.RESPIRATION);
				int air = player.getAir() + (int) ((respiration * 0.34) * ((float) player.getMaxAir() / 10));
				player.setAir(Math.min(player.getMaxAir(), air));

				// Lightning
				if (rand.nextDouble() < getEnchantmentLevel(Enchantments.CHANNELING) * 0.1) {
					LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
					lightning.moveForced(Vector3d.copyCenteredHorizontally(target.getPosition()));
					lightning.setCaster((ServerPlayerEntity) player);
					world.addEntity(lightning);
				}

				// Heal
				if (rand.nextDouble() < getEnchantmentLevel(Enchantments.MENDING) * 0.33) {
					player.heal(1);
				}

				// Protection buff
				if (rand.nextDouble() < getEnchantmentLevel(Enchantments.PROTECTION) * 0.015)
					player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 20 * 10));
			}

			// Bypass armor
			if (rand.nextDouble() < getEnchantmentLevel(Enchantments.UNBREAKING) * 0.1) {
				source.setDamageBypassesArmor();
			}

			// Potion effects
			if (target instanceof LivingEntity) {
				LivingEntity living = (LivingEntity) target;
				if (rand.nextDouble() < getEnchantmentLevel(Enchantments.FEATHER_FALLING) * 0.015)
					living.addPotionEffect(new EffectInstance(Effects.LEVITATION, 20 * 3));
				if (rand.nextDouble() < getEnchantmentLevel(Enchantments.THORNS) * 0.03)
					living.addPotionEffect(new EffectInstance(Effects.POISON, 20 * 7));
			}

			applyDamage(target, source);

			// Drops
			if (!target.isAlive() && world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
				spawnEgg(target);
				spawnExp();
			}

			// Fire
			if (rand.nextDouble() < fireChance()) {
				target.setFire(3);
			}

			// Knockback
			Vector3d direction = getMotion().scale(getEnchantmentLevel(Enchantments.KNOCKBACK) * 0.15);
			target.addVelocity(direction.x, getEnchantmentLevel(Enchantments.PUNCH) * 0.05, direction.z);
			target.setOnGround(false);

			// Piercing
			if (rand.nextDouble() >= getEnchantmentLevel(Enchantments.PIERCING) * 0.04)
				remove();
		}
	}

	private void spawnEgg(Entity target) {
		if (!target.isNonBoss() || rand.nextDouble() >= getEnchantmentLevel(Enchantments.SILK_TOUCH) * 0.01)
			return;

		SpawnEggItem egg = null;
		for (SpawnEggItem e : SpawnEggItem.getEggs()) {
			if (e.getType(null) == target.getType()) {
				egg = e;
				break;
			}
		}
		if (egg != null) {
			ItemEntity eggEntity = new ItemEntity(world, target.getPosX(), target.getPosY(), target.getPosZ(),
					new ItemStack(egg));
			world.addEntity(eggEntity);
		}
	}

	private void spawnExp() {
		int looting = getEnchantmentLevel(Enchantments.LOOTING);
		if (looting <= 0)
			return;

		int exp = rand.nextInt(looting + 1);

		while (exp > 0) {
			int fragment = ExperienceOrbEntity.getXPSplit(exp);
			exp -= fragment;
			world.addEntity(new ExperienceOrbEntity(world, getPosX(), getPosY(), getPosZ(), fragment));
		}
	}

	private double fireChance() {
		return getEnchantmentLevel(Enchantments.FIRE_ASPECT) * 0.08 + getEnchantmentLevel(Enchantments.FLAME) * 0.16;
	}

	private void applyDamage(Entity target, DamageSource source) {
		float damage = (float) getDamage();
		if (target instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) target;
			if (living.getCreatureAttribute() == CreatureAttribute.ARTHROPOD)
				damage += getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS) * 0.4;
			else if (living.getCreatureAttribute() == CreatureAttribute.ARTHROPOD)
				damage += getEnchantmentLevel(Enchantments.SMITE) * 0.4;
			else if (living.getCreatureAttribute() == CreatureAttribute.WATER)
				damage += getEnchantmentLevel(Enchantments.IMPALING) * 0.4;
		}
		damage += getEnchantmentLevel(Enchantments.SHARPNESS) * 0.3;

		if (target.isInWater())
			damage += getEnchantmentLevel(Enchantments.AQUA_AFFINITY) * 2;

		damage += distanceDamage();

		// Crit
		if (rand.nextDouble() < getEnchantmentLevel(Enchantments.FORTUNE) * 0.045)
			damage *= 2;

		target.attackEntityFrom(source, damage);
	}

	private float distanceDamage() {
		int efficiency = getEnchantmentLevel(Enchantments.EFFICIENCY);
		return efficiency / 15f * (float) getPositionVec().distanceTo(Vector3d.copyCentered(startPos)) * 0.3f;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		CompoundNBT compound = new CompoundNBT();
		compound.put("enchantments", serializeEnchantments(enchantments));
		buffer.writeCompoundTag(compound);
		buffer.writeBoolean(boomerang);

		// Shooter
		CompoundNBT shooterNBT = new CompoundNBT();
		Entity shooter = func_234616_v_();
		shooterNBT.putBoolean("exists", shooter != null && shooter instanceof PlayerEntity);
		if (shooter != null)
			shooterNBT.putUniqueId("uuid", shooter.getUniqueID());
		buffer.writeCompoundTag(shooterNBT);
	}

	@Override
	public void readSpawnData(PacketBuffer buffer) {
		CompoundNBT compound = buffer.readCompoundTag();
		ListNBT list = compound.getList("enchantments", Constants.NBT.TAG_COMPOUND);
		initEnchantments(list);
		boomerang = buffer.readBoolean();

		// Shooter
		CompoundNBT shooterNBT = buffer.readCompoundTag();
		if (shooterNBT.getBoolean("exists")) {
			setShooter(world.getPlayerByUuid(shooterNBT.getUniqueId("uuid")));
		}
	}

	private static ListNBT serializeEnchantments(Map<Enchantment, Integer> enchantments) {
		ListNBT list = new ListNBT();
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			CompoundNBT compound = new CompoundNBT();
			compound.putString("ench", entry.getKey().getRegistryName().toString());
			compound.putInt("level", entry.getValue());
			list.add(compound);
		}
		return list;
	}

	private static Map<Enchantment, Integer> deserializeEnchantments(ListNBT list) {
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

}
