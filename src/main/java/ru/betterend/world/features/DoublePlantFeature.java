package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class DoublePlantFeature extends ScatterFeature {
	private final Block smallPlant;
	private final Block largePlant;
	private Block plant;

	public DoublePlantFeature(Block smallPlant, Block largePlant, int radius) {
		super(radius);
		this.smallPlant = smallPlant;
		this.largePlant = largePlant;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos,
			float radius) {
		float d = MHelper.length(center.getX() - blockPos.getX(), center.getZ() - blockPos.getZ()) / radius * 0.6F
				+ random.nextFloat() * 0.4F;
		plant = d < 0.5F ? largePlant : smallPlant;
		return plant.canPlaceAt(plant.defaultBlockState(), world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		if (plant instanceof DoublePlantBlock) {
			int rot = random.nextInt(4);
			BlockState state = plant.defaultBlockState().with(DoublePlantBlock.ROTATION, rot);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
			BlocksHelper.setWithoutUpdate(world, blockPos.up(), state.with(DoublePlantBlock.TOP, true));
		} else {
			BlocksHelper.setWithoutUpdate(world, blockPos, plant);
		}
	}
}
