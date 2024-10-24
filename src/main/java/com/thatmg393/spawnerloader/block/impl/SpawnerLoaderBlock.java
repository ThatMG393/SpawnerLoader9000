package com.thatmg393.spawnerloader.block.impl;

import java.util.ArrayList;

import com.thatmg393.spawnerloader.block.base.BlockExt;
import com.thatmg393.spawnerloader.gui.SpawnerLoaderBlockGUI;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;

public class SpawnerLoaderBlock extends BlockExt {
	public static final Identifier BLOCK_ID = IdentifierUtils.getIdentifier("spawner_loader_block");
	public static final BooleanProperty ENABLE_CHUNK_LOADING = BooleanProperty.of("enable_chunk_loading");

	public static final int CHUNK_LOAD_RANGE = 3; // Only odd numbers!!
	public static final int CHUNK_DETECT_RANGE = 5; // Only odd numbers as well!!

	private final ArrayList<ChunkPos> chunkLoadedByThis = new ArrayList<>(CHUNK_LOAD_RANGE * CHUNK_LOAD_RANGE); 
	private boolean isLoadingChunks = false;

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

		this.setDefaultState(
			this.stateManager.getDefaultState()
				.with(ENABLE_CHUNK_LOADING, false)
		);
    }

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(ENABLE_CHUNK_LOADING);
	}

	@Override
	protected void onStateReplaced(BlockState oldState, World world, BlockPos centerPos, BlockState newState, boolean moved) {
		if (!oldState.isOf(newState.getBlock())) return;

		if (newState.get(ENABLE_CHUNK_LOADING)) forceNearbyChunksToLoad(world, centerPos);
		else unloadLoadedChunks(world);

		System.out.println("will load chunk? " + newState.get(ENABLE_CHUNK_LOADING));
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (player instanceof ServerPlayerEntity serverPlayerEntity) {
			if (world.getBlockState(pos.down()).isOf(Blocks.SPAWNER)) {
				SpawnerLoaderBlockGUI.openFor(serverPlayerEntity, state, pos);
				return ActionResult.SUCCESS;
			} else {
				serverPlayerEntity.playSound(
					SoundEvent.of(IdentifierUtils.getIdentifierFromVanilla("block.note_block.bass")), 1, 1
				);
				serverPlayerEntity.sendMessage(
					Text.literal("You need to place this block on top of any mob spawner!")
						.formatted(Formatting.RED)
						.formatted(Formatting.BOLD)
				);
				return ActionResult.FAIL;
			}
		}
		return super.onUse(state, world, pos, player, hit);
	}

	public void forceNearbyChunksToLoad(World world, BlockPos centerPos) {
		if (!world.getBlockState(centerPos.down()).isOf(Blocks.SPAWNER)) return;
		if (amIPresentInNearbyChunks(world, centerPos)) return;
		if (isLoadingChunks) return;

		isLoadingChunks = true;

		BlockPos startPos = centerPos.add(-(CHUNK_LOAD_RANGE - 2), 0, -(CHUNK_LOAD_RANGE - 2));
		ChunkManager chunkManager = world.getChunkManager();
		for (int cX = 0; cX < CHUNK_LOAD_RANGE; cX++) {
			for (int cZ = 0; cZ < CHUNK_LOAD_RANGE; cZ++) {
				ChunkPos chunkPos = new ChunkPos((startPos.getX() + (cX << 4)) >> 4, (startPos.getZ() + (cZ << 4)) >> 4);
				chunkLoadedByThis.add(chunkPos);

				chunkManager.setChunkForced(chunkPos, true);
			}
		}

		isLoadingChunks = false;
	}

	public void unloadLoadedChunks(World world) {
		if (chunkLoadedByThis.size() == 0) return;

		ChunkManager chunkManager = world.getChunkManager();
		chunkLoadedByThis.forEach(e -> chunkManager.setChunkForced(e, false));
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

	@Override
    public Identifier getBlockID() {
        return BLOCK_ID;
    }
}
