package com.thatmg393.spawnerloader.block.impl;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
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
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class SpawnerLoaderBlock extends BlockExt {
    public static final Identifier BLOCK_ID = IdentifierUtils.getIdentifier("spawner_loader_block");

	public SpawnerLoaderBlock() {
        super(AbstractBlock.Settings.copy(Blocks.BEACON)
				.mapColor(MapColor.BRIGHT_RED)
		        .allowsSpawning((state, world, pos, type) -> false)
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
		SpawnerLoader9000.LOGGER.info("LOADING CHUNKS IN A 3X3!!");
		for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                ChunkPos chunkPos = new ChunkPos(center.getX() >> 4, center.getZ() >> 4);
                world.getChunkManager().setChunkForced(chunkPos, true);
            }
        }
	}

	@Override
	public BlockState onBreak(World world, BlockPos center, BlockState state, PlayerEntity player) {
		SpawnerLoader9000.LOGGER.info("UNLOADING CHUNKS IN A 3X3!!");
		for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                ChunkPos chunkPos = new ChunkPos(center.getX() >> 4, center.getZ() >> 4);
                world.getChunkManager().setChunkForced(chunkPos, false);
            }
        }

		return getDefaultState();
	}
}
