package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;

public class CavePumpkinFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.up()).isIn(EndTags.GEN_TERRAIN) || !world.isAir(pos)
				|| !world.isAir(pos.below())) {
			return false;
		}

		int age = random.nextInt(4);
		BlocksHelper.setWithoutUpdate(world, pos,
				EndBlocks.CAVE_PUMPKIN_SEED.defaultBlockState().with(BlockProperties.AGE, age));
		if (age > 1) {
			BlocksHelper.setWithoutUpdate(world, pos.below(),
					EndBlocks.CAVE_PUMPKIN.defaultBlockState().with(BlockProperties.SMALL, age < 3));
		}

		return true;
	}
}
