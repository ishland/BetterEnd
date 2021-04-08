package ru.betterend.item.material;

import java.util.function.Supplier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public enum EndArmorMaterial implements ArmorMaterial {
	THALLASIUM("thallasium", 17, new int[] { 1, 4, 5, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
		return Ingredient.of(EndBlocks.THALLASIUM.ingot);
	}), TERMINITE("terminite", 26, new int[] { 3, 6, 7, 3 }, 14, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, 0.05F, () -> {
		return Ingredient.of(EndBlocks.TERMINITE.ingot);
	}),
	AETERNIUM("aeternium", 40, new int[] { 4, 7, 9, 4 }, 18, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3.5F, 0.2F, () -> {
		return Ingredient.of(EndItems.AETERNIUM_INGOT);
	}),
	CRYSTALITE("crystalite", 30, new int[] { 3, 6, 8, 3 }, 24, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1.2F, 0.1F, () -> {
		return Ingredient.of(EndBlocks.TERMINITE.ingot);
	});

	private static final int[] BASE_DURABILITY = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int durabilityMultiplier;
	private final int[] protectionAmounts;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	private EndArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability,
			SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {

		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
	}

	@Override
	public int getDurability(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slot) {
		return this.protectionAmounts[slot.getEntitySlotId()];
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}

}
