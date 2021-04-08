package ru.betterend.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnReason;
import net.minecraft.world.entity.attribute.DefaultAttributeContainer;
import net.minecraft.world.entity.attribute.EntityAttributes;
import net.minecraft.world.entity.damage.DamageSource;
import net.minecraft.world.entity.data.DataTracker;
import net.minecraft.world.entity.data.TrackedData;
import net.minecraft.world.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.entity.passive.SchoolingFishEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.level.Level;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndItems;

public class EndFishEntity extends SchoolingFishEntity {
	public static final int VARIANTS_NORMAL = 5;
	public static final int VARIANTS_SULPHUR = 3;
	public static final int VARIANTS = VARIANTS_NORMAL + VARIANTS_SULPHUR;
	private static final TrackedData<Byte> VARIANT = DataTracker.registerData(EndFishEntity.class,
			TrackedDataHandlerRegistry.BYTE);
	private static final TrackedData<Byte> SCALE = DataTracker.registerData(EndFishEntity.class,
			TrackedDataHandlerRegistry.BYTE);

	public EndFishEntity(EntityType<EndFishEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
			EntityData entityData, CompoundTag entityTag) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if (EndBiomes.getFromBiome(world.getBiome(getBlockPos())) == EndBiomes.SULPHUR_SPRINGS) {
			this.dataTracker.set(VARIANT, (byte) (random.nextInt(VARIANTS_SULPHUR) + VARIANTS_NORMAL));
		}
		this.calculateDimensions();
		return data;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(VARIANT, (byte) this.getRandom().nextInt(VARIANTS_NORMAL));
		this.dataTracker.startTracking(SCALE, (byte) this.getRandom().nextInt(16));
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putByte("Variant", (byte) getVariant());
		tag.putByte("Scale", (byte) getScale());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		if (tag.contains("Variant")) {
			this.dataTracker.set(VARIANT, tag.getByte("Variant"));
		}
		if (tag.contains("Scale")) {
			this.dataTracker.set(SCALE, tag.getByte("Scale"));
		}
	}

	@Override
	protected ItemStack getFishBucketItem() {
		return new ItemStack(EndItems.BUCKET_END_FISH);
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SALMON_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SALMON_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SALMON_HURT;
	}

	@Override
	public void tick() {
		super.tick();
		if (random.nextInt(8) == 0 && getBlockState().is(Blocks.WATER)) {
			double x = getX() + random.nextGaussian() * 0.2;
			double y = getY() + random.nextGaussian() * 0.2;
			double z = getZ() + random.nextGaussian() * 0.2;
			world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
		}
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.75);
	}

	public int getVariant() {
		return (int) this.dataTracker.get(VARIANT);
	}

	public float getScale() {
		return this.dataTracker.get(SCALE) / 32F + 0.75F;
	}

	public static boolean canSpawn(EntityType<EndFishEntity> type, ServerWorldAccess world, SpawnReason spawnReason,
			BlockPos pos, Random random) {
		Box box = new Box(pos).expand(16);
		List<EndFishEntity> list = world.getEntitiesByClass(EndFishEntity.class, box, (entity) -> {
			return true;
		});
		return list.size() < 9;
	}

	@Override
	protected void dropLoot(DamageSource source, boolean causedByPlayer) {
		ItemEntity drop = new ItemEntity(world, getX(), getY(), getZ(), new ItemStack(EndItems.END_FISH_RAW));
		this.world.spawnEntity(drop);
	}
}
