package com.thatmg393.spawnerloader.block.impl.block;

import java.util.ArrayList;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.block.getters.BlockIDGetter;
import com.thatmg393.spawnerloader.block.impl.block.entity.SpawnerLoaderBlockEntity;
import com.thatmg393.spawnerloader.gui.SpawnerLoaderBlockGUI;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

public class SpawnerLoaderBlock extends Block implements BlockIDGetter, BlockEntityProvider {
	public static final Identifier BLOCK_ID = IdentifierUtils.getIdentifier("spawner_loader_block");
	public static final BooleanProperty ENABLE_CHUNK_LOADING = BooleanProperty.of("enable_chunk_loading");

	public static final int CHUNK_LOAD_RANGE = 2; // Only even numbers!! (this does +1 cus the center chunk)
	public static final int CHUNK_DETECT_RANGE = 4; // Only even numbers!! (this does +1 cus the center chunk)

	private final ArrayList<ChunkPos> chunkLoadedByThis = new ArrayList<>((CHUNK_LOAD_RANGE + 1) * (CHUNK_LOAD_RANGE + 1)); 
	
	private boolean isLoadingChunks;

	public SpawnerLoaderBlock() {
        super(Settings.copy(Blocks.BEACON)
				.mapColor(MapColor.BRIGHT_RED)
				.allowsSpawning((state, world, centerPos, type) -> false)
				.solid()
				.strength(5f)
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
	protected void onStateReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(oldState, world, pos, newState, moved);
		
		SpawnerLoader9000.LOGGER.info("onStateReplaced! moved -> " + moved);
		if (!moved && !oldState.isOf(newState.getBlock())) {
			unloadLoadedChunks(world);
			return;
		}

		if (newState.get(ENABLE_CHUNK_LOADING)) loadNearbyChunks(world, pos);
		else unloadLoadedChunks(world);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (player instanceof ServerPlayerEntity serverPlayerEntity) {
			if (world.getBlockState(pos.down()).isOf(Blocks.SPAWNER)) {
				SpawnerLoaderBlockGUI.openFor(serverPlayerEntity, state, pos);
			} else {
				serverPlayerEntity.sendMessage(
					Text.literal("You need to place this block on top of any mob spawner to use the GUI!")
						.formatted(Formatting.RED, Formatting.BOLD)
				);

				serverPlayerEntity.playSoundToPlayer(
					SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(),
					SoundCategory.BLOCKS,
					1.0f,
					0.5f
				);
			}
		}
		
		return ActionResult.SUCCESS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpawnerLoaderBlockEntity(pos, state);
	}

	@Override
    public Identifier getBlockID() {
        return BLOCK_ID;
	}

	public void loadNearbyChunks(World world, BlockPos pos) {
		if (!((SpawnerLoaderBlockEntity) world.getBlockEntity(pos)).allowChunkLoading()) {
			world.setBlockState(pos, world.getBlockState(pos).with(ENABLE_CHUNK_LOADING, false));
			return;
		}

		ChunkPos center = world.getChunk(pos).getPos();
		int diff = SpawnerLoaderBlock.CHUNK_LOAD_RANGE / 2;

		isLoadingChunks = true;

        for (int dx = -diff; dx <= diff; dx++) {
            for (int dz = -diff; dz <= diff; dz++) {
				ChunkPos currentChunk = new ChunkPos(
					center.x + dx, 
					center.z + dz
				);

				world.getChunkManager().setChunkForced(currentChunk, true);
				chunkLoadedByThis.add(currentChunk);
			}
		}

		isLoadingChunks = false;
	}

	public void unloadLoadedChunks(World world) {
		if (chunkLoadedByThis.size() == 0 || isLoadingChunks) return;

		chunkLoadedByThis.forEach(e -> world.getChunkManager().setChunkForced(e, false));
		chunkLoadedByThis.removeAll(chunkLoadedByThis);
	}
}
