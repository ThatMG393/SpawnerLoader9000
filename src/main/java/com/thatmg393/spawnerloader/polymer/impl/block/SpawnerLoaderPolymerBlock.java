package com.thatmg393.spawnerloader.polymer.impl.block;

import com.thatmg393.spawnerloader.block.impl.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.polymer.impl.item.SpawnerLoaderPolymerBlockItem;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpawnerLoaderPolymerBlock extends SpawnerLoaderBlock implements PolymerTexturedBlock {
    private final BlockState polymerBlock;
    
    public SpawnerLoaderPolymerBlock() {
        super();
        this.polymerBlock = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(IdentifierUtils.getIdentifier("block/spawner_loader_block")));
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
