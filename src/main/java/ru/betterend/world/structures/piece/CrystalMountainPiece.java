package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndStructures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;

public class CrystalMountainPiece extends MountainPiece {
	private BlockState top;

	public CrystalMountainPiece(BlockPos center, float radius, float height, Random random, Biome biome) {
		super(EndStructures.MOUNTAIN_PIECE, center, radius, height, random, biome);
		top = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
	}

	public CrystalMountainPiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.MOUNTAIN_PIECE, manager, tag);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		super.fromNbt(tag);
		top = EndBiomes.getBiome(biomeID).getBiome().getGenerationSettings().getSurfaceConfig().getTopMaterial();
	}

	@Override
	public boolean generate(StructureWorldAccess world, StructureAccessor arg, ChunkGenerator chunkGenerator,
			Random random, BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		int sx = chunkPos.getStartX();
		int sz = chunkPos.getStartZ();
		MutableBlockPos pos = new MutableBlockPos();
		Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
		Heightmap map = chunk.getHeightmap(Type.WORLD_SURFACE);
		Heightmap map2 = chunk.getHeightmap(Type.WORLD_SURFACE_WG);
		for (int x = 0; x < 16; x++) {
			int px = x + sx;
			int px2 = px - center.getX();
			px2 *= px2;
			pos.setX(x);
			for (int z = 0; z < 16; z++) {
				int pz = z + sz;
				int pz2 = pz - center.getZ();
				pz2 *= pz2;
				float dist = px2 + pz2;
				if (dist < r2) {
					pos.setZ(z);
					dist = 1 - (float) Math.pow(dist / r2, 0.3);
					int minY = map.get(x, z);
					if (minY < 10) {
						continue;
					}
					pos.setY(minY);
					while (!chunk.getBlockState(pos).isIn(EndTags.GEN_TERRAIN) && pos.getY() > 56
							&& !chunk.getBlockState(pos.below()).is(Blocks.CAVE_AIR)) {
						pos.setY(pos.getY() - 1);
					}
					minY = pos.getY();
					minY = Math.max(minY, map2.get(x, z));
					if (minY > center.getY() - 8) {
						float maxY = dist * height * getHeightClamp(world, 12, px, pz);
						if (maxY > 0) {
							maxY *= (float) noise1.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
							maxY *= (float) noise1.eval(px * 0.1, pz * 0.1) * 0.1F + 0.8F;
							maxY += center.getY();
							int maxYI = (int) (maxY);
							int cover = maxYI - 1;
							boolean needCover = (noise1.eval(px * 0.1, pz * 0.1) + MHelper.randRange(-0.4, 0.4, random)
									- (center.getY() + 14) * 0.1) > 0;
							for (int y = minY - 1; y < maxYI; y++) {
								pos.setY(y);
								chunk.setBlockAndUpdate(pos,
										needCover && y == cover ? top : Blocks.END_STONE.defaultBlockState(), false);
							}
						}
					}
				}
			}
		}

		map = chunk.getHeightmap(Type.WORLD_SURFACE);

		// Big crystals
		int count = (map.get(8, 8) - (center.getY() + 24)) / 7;
		count = Mth.clamp(count, 0, 8);
		for (int i = 0; i < count; i++) {
			int radius = MHelper.randRange(2, 3, random);
			float fill = MHelper.randRange(0F, 1F, random);
			int x = MHelper.randRange(radius, 15 - radius, random);
			int z = MHelper.randRange(radius, 15 - radius, random);
			int y = map.get(x, z);
			if (y > 80) {
				pos.set(x, y, z);
				if (chunk.getBlockState(pos.below()).is(Blocks.END_STONE)) {
					int height = MHelper.floor(radius * MHelper.randRange(1.5F, 3F, random) + (y - 80) * 0.3F);
					crystal(chunk, pos, radius, height, fill, random);
				}
			}
		}

		// Small crystals
		count = (map.get(8, 8) - (center.getY() + 24)) / 2;
		count = Mth.clamp(count, 4, 8);
		for (int i = 0; i < count; i++) {
			int radius = MHelper.randRange(1, 2, random);
			float fill = random.nextBoolean() ? 0 : 1;
			int x = MHelper.randRange(radius, 15 - radius, random);
			int z = MHelper.randRange(radius, 15 - radius, random);
			int y = map.get(x, z);
			if (y > 80) {
				pos.set(x, y, z);
				if (chunk.getBlockState(pos.below()).getBlock() == Blocks.END_STONE) {
					int height = MHelper.floor(radius * MHelper.randRange(1.5F, 3F, random) + (y - 80) * 0.3F);
					crystal(chunk, pos, radius, height, fill, random);
				}
			}
		}

		return true;
	}

	private void crystal(Chunk chunk, BlockPos pos, int radius, int height, float fill, Random random) {
		MutableBlockPos mut = new MutableBlockPos();
		int max = MHelper.floor(fill * radius + radius + 0.5F);
		height += pos.getY();
		Heightmap map = chunk.getHeightmap(Type.WORLD_SURFACE);
		int coefX = MHelper.randRange(-1, 1, random);
		int coefZ = MHelper.randRange(-1, 1, random);
		for (int x = -radius; x <= radius; x++) {
			mut.setX(x + pos.getX());
			if (mut.getX() >= 0 && mut.getX() < 16) {
				int ax = Math.abs(x);
				for (int z = -radius; z <= radius; z++) {
					mut.setZ(z + pos.getZ());
					if (mut.getZ() >= 0 && mut.getZ() < 16) {
						int az = Math.abs(z);
						if (ax + az < max) {
							int minY = map.get(mut.getX(), mut.getZ()) - MHelper.randRange(3, 7, random);
							if (pos.getY() - minY > 8) {
								minY = pos.getY() - 8;
							}
							int h = coefX * x + coefZ * z + height;
							for (int y = minY; y < h; y++) {
								mut.setY(y);
								chunk.setBlockAndUpdate(mut, EndBlocks.AURORA_CRYSTAL.defaultBlockState(), false);
							}
						}
					}
				}
			}
		}
	}
}
