package com.thatmg393.spawnerloader.polymer.impl.block;

import com.thatmg393.spawnerloader.block.impl.SpawnerLoaderBlock;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.BlockState;

public class SpawnerLoaderPolymerBlock extends SpawnerLoaderBlock implements PolymerTexturedBlock {
    private PolymerBlockModel polymerBlockModel = PolymerBlockModel.of(this.getBlockID());

    public SpawnerLoaderPolymerBlock() {
        super();
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, polymerBlockModel);
    }

}
