package ru.betterend.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroup;
import net.minecraft.world.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.entity.CubozoaEntity;
import ru.betterend.entity.DragonflyEntity;
import ru.betterend.entity.EndFishEntity;
import ru.betterend.entity.EndSlimeEntity;
import ru.betterend.entity.ShadowWalkerEntity;
import ru.betterend.entity.SilkMothEntity;
import ru.betterend.util.MHelper;
import ru.betterend.util.SpawnHelper;

public class EndEntities {
	public static final EntityType<DragonflyEntity> DRAGONFLY = register("dragonfly", SpawnGroup.AMBIENT, 0.6F, 0.5F,
			DragonflyEntity::new, DragonflyEntity.createMobAttributes(), true, MHelper.color(32, 42, 176),
			MHelper.color(115, 225, 249));
	public static final EntityType<EndSlimeEntity> END_SLIME = register("end_slime", SpawnGroup.MONSTER, 2F, 2F,
			EndSlimeEntity::new, EndSlimeEntity.createMobAttributes(), false, MHelper.color(28, 28, 28),
			MHelper.color(99, 11, 99));
	public static final EntityType<EndFishEntity> END_FISH = register("end_fish", SpawnGroup.WATER_AMBIENT, 0.5F, 0.5F,
			EndFishEntity::new, EndFishEntity.createMobAttributes(), true, MHelper.color(3, 50, 76),
			MHelper.color(120, 206, 255));
	public static final EntityType<ShadowWalkerEntity> SHADOW_WALKER = register("shadow_walker", SpawnGroup.MONSTER,
			0.6F, 1.95F, ShadowWalkerEntity::new, ShadowWalkerEntity.createMobAttributes(), true,
			MHelper.color(30, 30, 30), MHelper.color(5, 5, 5));
	public static final EntityType<CubozoaEntity> CUBOZOA = register("cubozoa", SpawnGroup.WATER_AMBIENT, 0.6F, 1F,
			CubozoaEntity::new, CubozoaEntity.createMobAttributes(), true, MHelper.color(151, 77, 181),
			MHelper.color(93, 176, 238));
	public static final EntityType<SilkMothEntity> SILK_MOTH = register("silk_moth", SpawnGroup.AMBIENT, 0.6F, 0.6F,
			SilkMothEntity::new, SilkMothEntity.createMobAttributes(), true, MHelper.color(198, 138, 204),
			MHelper.color(242, 220, 236));

	public static void register() {
		SpawnHelper.restrictionAir(DRAGONFLY, DragonflyEntity::canSpawn);
		SpawnHelper.restrictionLand(END_SLIME, EndSlimeEntity::canSpawn);
		SpawnHelper.restrictionWater(END_FISH, EndFishEntity::canSpawn);
		SpawnHelper.restrictionLand(SHADOW_WALKER, ShadowWalkerEntity::canSpawn);
		SpawnHelper.restrictionWater(CUBOZOA, CubozoaEntity::canSpawn);
		SpawnHelper.restrictionAir(SILK_MOTH, SilkMothEntity::canSpawn);
	}

	protected static <T extends Entity> EntityType<T> register(String name, SpawnGroup group, float width, float height,
			EntityFactory<T> entity) {
		ResourceLocation id = BetterEnd.makeID(name);
		EntityType<T> type = FabricEntityTypeBuilder.<T>create(group, entity)
				.dimensions(EntityDimensions.fixed(width, height)).build();
		if (Configs.ENTITY_CONFIG.getBooleanRoot(id.getPath(), true)) {
			return Registry.register(Registry.ENTITY_TYPE, id, type);
		}
		return type;
	}

	private static <T extends LivingEntity> EntityType<T> register(String name, SpawnGroup group, float width,
			float height, EntityFactory<T> entity, Builder attributes, boolean fixedSize, int eggColor, int dotsColor) {
		ResourceLocation id = BetterEnd.makeID(name);
		EntityType<T> type = FabricEntityTypeBuilder.<T>create(group, entity)
				.dimensions(
						fixedSize ? EntityDimensions.fixed(width, height) : EntityDimensions.changing(width, height))
				.build();
		if (Configs.ENTITY_CONFIG.getBooleanRoot(id.getPath(), true)) {
			FabricDefaultAttributeRegistry.register(type, attributes);
			EndItems.registerEgg("spawn_egg_" + name, type, eggColor, dotsColor);
			return Registry.register(Registry.ENTITY_TYPE, BetterEnd.makeID(name), type);
		}
		return type;
	}
}
