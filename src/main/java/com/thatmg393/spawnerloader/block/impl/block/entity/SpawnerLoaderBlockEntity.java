package com.thatmg393.spawnerloader.block.impl.block.entity;

import com.thatmg393.spawnerloader.block.getters.BlockEntityIDGetter;
import com.thatmg393.spawnerloader.block.impl.block.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.block.registry.BlockEntityRegistry;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class SpawnerLoaderBlockEntity extends BlockEntity implements BlockEntityIDGetter {
    public static final Identifier BLOCK_ENTITY_ID = IdentifierUtils.getIdentifier("spawner_loader_block_entity");

    public SpawnerLoaderBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.getBlockEntityType(BLOCK_ENTITY_ID), pos, state);
    }

    public boolean allowChunkLoading() {
        ChunkPos center = new ChunkPos(getPos());
        int diff = SpawnerLoaderBlock.CHUNK_DETECT_RANGE / 2;

        for (int dx = -diff; dx <= diff; dx++) {
            for (int dz = -diff; dz <= diff; dz++) {
                ChunkPos currentChunk = new ChunkPos(
                    center.x + dx,
                    center.z + dz
                );

                if (world.getChunk(currentChunk.x, currentChunk.z).getBlockEntities().values()
                    .parallelStream()
                    .anyMatch(blockEntity -> blockEntity.getType() == this.getType()
                                && !blockEntity.getPos().equals(getPos()))
                ) return true; // early return
            }
        }

        return false;
    }

    @Override
    public Identifier getBlockEntityID() {
        return BLOCK_ENTITY_ID;
    }
}
