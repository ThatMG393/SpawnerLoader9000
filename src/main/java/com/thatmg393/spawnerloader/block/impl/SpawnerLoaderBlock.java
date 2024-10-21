package com.thatmg393.spawnerloader.block.impl;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.block.base.BlockExt;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public class SpawnerLoaderBlock extends BlockExt {
    public SpawnerLoaderBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public Identifier getBlockID() {
        return Identifier.of(SpawnerLoader9000.MOD_ID, "spawner_loader_block");
    }
}
