package com.thatmg393.spawnerloader.block.impl;

import eu.pb4.polymer.core.api.block.PolymerBlock;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.block.base.BlockExt;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;

public class SpawnerLoaderBlock extends BlockExt implements PolymerBlock {
    public static final Identifier BLOCK_ID = Identifier.of(SpawnerLoader9000.MOD_ID, "spawner_loader_block");
    public SpawnerLoaderBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public Identifier getBlockID() {
        return BLOCK_ID;
    }

	@Override
	public Block getPolymerBlock(BlockState state) {
		return Blocks.STONE;
	}

	@Override
	public BlockState getPolymerBlockState(BlockState state) {
		return Blocks.STONE.getDefaultState();
	}

	@Override
	public Identifier getPolymerResourceLocation() {
		return new Identifier(SpawnerLoader9000.MOD_ID, "block/spawner_loader_block");
	}
}
