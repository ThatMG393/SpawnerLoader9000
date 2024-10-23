package com.thatmg393.spawnerloader.block.impl;

import com.thatmg393.spawnerloader.block.base.BlockExt;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;

public class SpawnerLoaderBlock extends BlockExt {
    public static final Identifier BLOCK_ID = IdentifierUtils.getIdentifier("spawner_loader_block");

	public static final int CHUNK_LOAD_RANGE = 3; // Only odd numbers!!
	public static final int CHUNK_DETECT_RANGE = 5; // Only odd numbers as well!!

	private final ChunkPos[] chunkLoadedByThis = new ChunkPos[CHUNK_LOAD_RANGE * CHUNK_LOAD_RANGE]; 
	
	private boolean isLoadingChunks = false;
	private boolean didTryToLoadChunks = false;

	public SpawnerLoaderBlock() {
        super(AbstractBlock.Settings.copy(Blocks.BEACON)
				.mapColor(MapColor.BRIGHT_RED)
		        .allowsSpawning((state, world, centerPos, type) -> false)
				.solid()
				.resistance(8)
				.hardness(8)
				.requiresTool()
				.sounds(BlockSoundGroup.ANVIL)
				.luminance((state) -> 0)
				.pistonBehavior(PistonBehavior.NORMAL));
    }

    @Override
    public Identifier getBlockID() {
        return BLOCK_ID;
    }

	@Override
	public void onPlaced(World world, BlockPos center, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.getBlockState(center.down()).isOf(Blocks.SPAWNER)) return;
		if (amIPresentInNearbyChunks(world, center)) return;

		forceNearbyChunksToLoad(world, center);
	}

	@Override
	public BlockState onBreak(World world, BlockPos center, BlockState state, PlayerEntity player) {
		unloadLoadedChunks(world);
		return this.getDefaultState();
	}

	public void forceNearbyChunksToLoad(World world, BlockPos centerPos) {
		if (isLoadingChunks) return;

		isLoadingChunks = true;
		centerPos = new BlockPos(new Vec3i(centerPos.getX() - (CHUNK_LOAD_RANGE - 2), 0, centerPos.getZ() - (CHUNK_LOAD_RANGE - 2)));

		ChunkManager chunkManager = world.getChunkManager();
		for (int cX = 0; cX < CHUNK_LOAD_RANGE; cX++) {
			for (int cZ = 0; cZ < CHUNK_LOAD_RANGE; cZ++) {
				ChunkPos chunkPos = new ChunkPos(centerPos.getX() + cX, centerPos.getZ() + cZ);
				chunkLoadedByThis[cX + cZ] = chunkPos;

				chunkManager.setChunkForced(chunkPos, true);
			}
		}

		isLoadingChunks = false;
		didTryToLoadChunks = true;
	}

	public void unloadLoadedChunks(World world) {
		if (!didTryToLoadChunks) return;

		ChunkManager chunkManager = world.getChunkManager();
		for (ChunkPos chunkPos : chunkLoadedByThis) chunkManager.setChunkForced(chunkPos, false);

		didTryToLoadChunks = false; /* idk im going insane */
	}


	/* Most slowest code that i've written in a long time. 
	 * I don't know anything about this
	 */
	public boolean amIPresentInNearbyChunks(World world, BlockPos centerPos) {
		int startChunkX = centerPos.getX() >> 4, startChunkZ = centerPos.getZ() >> 4; 
		int chunkRange = CHUNK_DETECT_RANGE / 2;
		int amountOfTimesPresent = 0;
	
		for (int offsetX = -chunkRange; offsetX <= chunkRange; offsetX++) {
			for (int offsetZ = -chunkRange; offsetZ <= chunkRange; offsetZ++) {
				Chunk chunk = world.getChunk(startChunkX + offsetX, startChunkZ + offsetZ);
				for (int bX = 0; bX < 16; bX++) {
					for (int bY = world.getBottomY(); bY < world.getTopY(); bY++) {
						for (int bZ = 0; bZ < 16; bZ++) {
							if (chunk.getBlockState(new BlockPos((startChunkX + offsetX) << 4 | bX, bY, (startChunkZ + offsetZ) << 4 | bZ)).isOf(this))
								++amountOfTimesPresent;

							if (amountOfTimesPresent >= 2) return true;
						}
					}
				}
			}
		}
		return false;
	}	
}
