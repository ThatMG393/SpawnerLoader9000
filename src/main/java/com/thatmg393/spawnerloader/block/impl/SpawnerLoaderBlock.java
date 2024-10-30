package com.thatmg393.spawnerloader.block.impl;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.block.base.BlockExt;
import com.thatmg393.spawnerloader.block.impl.blockitem.SpawnerLoaderBlockItem;
import com.thatmg393.spawnerloader.gui.SpawnerLoaderBlockGUI;
import com.thatmg393.spawnerloader.utils.BlockSearcher;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
import net.minecraft.world.chunk.ChunkManager;

public class SpawnerLoaderBlock extends BlockExt {
	public static final Identifier BLOCK_ID = IdentifierUtils.getIdentifier("spawner_loader_block");
	public static final BooleanProperty ENABLE_CHUNK_LOADING = BooleanProperty.of("enable_chunk_loading");

	public static final int CHUNK_LOAD_RANGE = 3; // Only odd numbers!!
	public static final int CHUNK_DETECT_RANGE = 5; // Only odd numbers as well!!

	private final ArrayList<ChunkPos> chunkLoadedByThis = new ArrayList<>(CHUNK_LOAD_RANGE * CHUNK_LOAD_RANGE); 
	private final AtomicBoolean isLoadingChunks = new AtomicBoolean();

	private CompletableFuture<Boolean> blockSearchFuture = null;

	public SpawnerLoaderBlock() {
        super(AbstractBlock.Settings.copy(Blocks.BEACON)
				.mapColor(MapColor.BRIGHT_RED)
		        .allowsSpawning((state, world, centerPos, type) -> false)
				.solid()
				.strength(8f)
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
		SpawnerLoader9000.LOGGER.info("onStateReplaced! moved->" + moved);
		if (!moved && !oldState.isOf(newState.getBlock())) {
			unloadLoadedChunks(world);
			return;
		}

		if (newState.get(ENABLE_CHUNK_LOADING)) forceNearbyChunksToLoad(world, centerPos);
		else unloadLoadedChunks(world);

		super.onStateReplaced(oldState, world, centerPos, newState, moved);
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
					Text.literal("You need to place this block on top of any mob spawner to use the GUI!")
						.formatted(Formatting.RED, Formatting.BOLD)
				);
				return ActionResult.FAIL;
			}
		}
		return super.onUse(state, world, pos, player, hit);
	}

	@Override
	public Item asItem() {
		return new SpawnerLoaderBlockItem(this);
	}

	public void forceNearbyChunksToLoad(World world, BlockPos centerPos) {
		if (!world.getBlockState(centerPos.down()).isOf(Blocks.SPAWNER)) return;
		blockSearchFuture = BlockSearcher.isBlockPresent(world, centerPos, this, CHUNK_DETECT_RANGE);
		blockSearchFuture.thenAccept((b) -> {
				if (isLoadingChunks.get()) return;
				blockSearchFuture = null;

				SpawnerLoader9000.LOGGER.info("will now load chunks around.");
				isLoadingChunks.set(true);

				unloadLoadedChunks(world);

				BlockPos startPos = centerPos.add(-(CHUNK_LOAD_RANGE - 2), 0, -(CHUNK_LOAD_RANGE - 2));
				ChunkManager chunkManager = world.getChunkManager();
				for (int cX = 0; cX < CHUNK_LOAD_RANGE; cX++) {
					for (int cZ = 0; cZ < CHUNK_LOAD_RANGE; cZ++) {
						ChunkPos chunkPos = new ChunkPos((startPos.getX() + (cX << 4)) >> 4, (startPos.getZ() + (cZ << 4)) >> 4);
						chunkLoadedByThis.add(chunkPos);

						chunkManager.setChunkForced(chunkPos, true);
					}
				}

				isLoadingChunks.set(false);
			}).exceptionally(e -> {
				SpawnerLoader9000.LOGGER.info("failed to check myself around. disabling chunk loading.");

				world.setBlockState(
					centerPos,
					world.getBlockState(centerPos).with(SpawnerLoaderBlock.ENABLE_CHUNK_LOADING, false)
				);

				return null;
			});
	}

	public void unloadLoadedChunks(World world) {
		if (chunkLoadedByThis.size() == 0 || isLoadingChunks.get()) return;
		if (blockSearchFuture != null) blockSearchFuture.cancel(false);

		ChunkManager chunkManager = world.getChunkManager();
		chunkLoadedByThis.forEach(e -> chunkManager.setChunkForced(e, false));
	}

	@Override
    public Identifier getBlockID() {
        return BLOCK_ID;
    }
}
