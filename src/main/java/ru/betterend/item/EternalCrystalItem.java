package ru.betterend.item;

import net.minecraft.world.item.Rarity;
import ru.betterend.registry.EndItems;

public class EternalCrystalItem extends PatternedItem {
	public EternalCrystalItem() {
		super(EndItems.makeItemSettings().maxCount(16).rarity(Rarity.EPIC));
	}
}