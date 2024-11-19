package com.thatmg393.spawnerloader.polymer.impl.block;

import com.thatmg393.spawnerloader.block.impl.block.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpawnerLoaderPolymerBlock extends SpawnerLoaderBlock implements PolymerTexturedBlock {
    private final BlockState polymerBlock = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(IdentifierUtils.getBlockIdentifier("spawner_loader_block")));
    
    public SpawnerLoaderPolymerBlock() {
        super();
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return this.polymerBlock;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, ServerPlayerEntity player) {
        return this.getPolymerBlockState(state);
    }
}
