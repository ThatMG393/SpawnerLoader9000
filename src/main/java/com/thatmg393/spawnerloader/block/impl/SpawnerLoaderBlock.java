package com.thatmg393.spawnerloader.block.impl;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.StatelessPolymerBlock;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.block.base.BlockExt;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class SpawnerLoaderBlock extends BlockExt implements StatelessPolymerBlock, PolymerTexturedBlock {
    public static final Identifier BLOCK_ID = Identifier.of(SpawnerLoader9000.MOD_ID, "spawner_loader_block");
    public SpawnerLoaderBlock() {
        super(AbstractBlock.Settings.copy(Blocks.BEACON)
		        .allowsSpawning((state, world, pos, type) -> false)
				.solid()
				.resistance(8)
				.hardness(8)
				.requiresTool()
				.sounds(BlockSoundGroup.ANVIL));
    }

    @Override
    public Identifier getBlockID() {
        return BLOCK_ID;
    }

	@Override
	public Block getPolymerBlock(BlockState state) {
		return getPolymerBlock(state, null);
	}

	@Override
	public Block getPolymerBlock(BlockState state, ServerPlayerEntity player) {
		return Blocks.COBBLESTONE;
	}

	/*
	@Override
	public Identifier getPolymerResourceLocation() {
		return Identifier.of(SpawnerLoader9000.MOD_ID, "block/spawner_loader_block");
	} */
}
