package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.MHelper;

public class EndLeavesBlock extends LeavesBlock implements BlockPatterned, IRenderTypeable {
	private final Block sapling;

	public EndLeavesBlock(Block sapling, MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).allowsSpawning((state, world, pos, type) -> {
			return false;
		}).suffocates((state, world, pos) -> {
			return false;
		}).blockVision((state, world, pos) -> {
			return false;
		}).materialColor(color).breakByTool(FabricToolTags.HOES).breakByTool(FabricToolTags.SHEARS).breakByHand(true));
		this.sapling = sapling;
	}

	public EndLeavesBlock(Block sapling, MaterialColor color, int light) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).allowsSpawning((state, world, pos, type) -> {
			return false;
		}).suffocates((state, world, pos) -> {
			return false;
		}).blockVision((state, world, pos) -> {
			return false;
		}).materialColor(color).luminance(light).breakByTool(FabricToolTags.HOES).breakByTool(FabricToolTags.SHEARS));
		this.sapling = sapling;
	}

	@Override
	public String getStatesPattern(Reader data) {
		String blockId = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}

	@Override
	public String getModelPattern(String block) {
		String blockId = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(Patterns.BLOCK_BASE, blockId, blockId);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null) {
			if (tool.getItem().isIn(FabricToolTags.SHEARS)
					|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
				return Collections.singletonList(new ItemStack(this));
			}
			int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
			if (MHelper.RANDOM.nextInt(16) <= fortune) {
				return Lists.newArrayList(new ItemStack(sapling));
			}
			return Lists.newArrayList();
		}
		return MHelper.RANDOM.nextInt(16) == 0 ? Lists.newArrayList(new ItemStack(sapling)) : Lists.newArrayList();
	}
}
