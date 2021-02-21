package mod.vemerion.runesword.entity;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.helpers.Helper;
import mod.vemerion.runesword.particle.MagicBallParticleData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicBallEntity extends AbstractArrowEntity implements IEntityAdditionalSpawnData {

	private static final int MAX_DURATION = 20 * 3;
	public static final Color DEFAULT_COLOR = new Color(100, 0, 100, 255);

	private static final Map<Enchantment, Color> ENCHANTMENT_COLORS = new HashMap<>();

	static {
		ENCHANTMENT_COLORS.put(Enchantments.AQUA_AFFINITY, new Color(0, 255, 0));
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

	public MagicBallEntity(EntityType<? extends MagicBallEntity> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
		this.setDamage(4);
		this.setNoGravity(true);
		this.enchantments = new HashMap<>();
		this.enchantmentArr = new Enchantment[0];
	}

	public MagicBallEntity(double x, double y, double z, World world, Map<Enchantment, Integer> enchantments) {
		super(Main.MAGIC_BALL_ENTITY, x, y, z, world);
		this.setDamage(4);
		this.setNoGravity(true);
		this.enchantments = enchantments;
		this.enchantmentArr = enchantments.keySet().toArray(new Enchantment[0]);
	}

	@Override
	public void tick() {
		super.tick();

		duration++;

		if (!world.isRemote) {
			if (duration > MAX_DURATION) {
				remove();
			}
		} else {
			createParticles();
		}
	}

	private void createParticles() {
		for (int i = 0; i < 10; i++) {
			Color color = DEFAULT_COLOR;
			if (!enchantments.isEmpty())
				color = ENCHANTMENT_COLORS.getOrDefault(enchantmentArr[rand.nextInt(enchantmentArr.length)],
						DEFAULT_COLOR);
			Vector3d pos = new Vector3d(getPosX() + rand.nextDouble() - 0.5, getPosY() + rand.nextDouble() - 0.5,
					getPosZ() + rand.nextDouble() - 0.5);
			world.addParticle(
					new MagicBallParticleData(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f),
					pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("duration"))
			duration = compound.getInt("duration");
		if (compound.contains("enchantments"))
			initEnchantments(compound.getList("enchantments", Constants.NBT.TAG_COMPOUND));
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("duration", duration);
		compound.put("enchantments", serializeEnchantments(enchantments));
	}
	
	private void initEnchantments(ListNBT list) {
		enchantments = deserializeEnchantments(list);
		enchantmentArr = enchantments.keySet().toArray(new Enchantment[0]);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
		if (!world.isRemote) {
			remove();
		}
	}

	@Override
	protected void onEntityHit(EntityRayTraceResult result) {
		if (!world.isRemote) {
			Entity target = result.getEntity();
			if (func_234616_v_() != null && func_234616_v_() instanceof PlayerEntity) { // getShooter()
				target.attackEntityFrom(Helper.magicDamage(this, (PlayerEntity) func_234616_v_()), (float) getDamage());
			} else {
				target.attackEntityFrom(Helper.magicDamage(), (float) getDamage());
			}
		}
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
	}

	@Override
	public void readSpawnData(PacketBuffer buffer) {
		CompoundNBT compound = buffer.readCompoundTag();
		ListNBT list = compound.getList("enchantments", Constants.NBT.TAG_COMPOUND);
		initEnchantments(list);
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
