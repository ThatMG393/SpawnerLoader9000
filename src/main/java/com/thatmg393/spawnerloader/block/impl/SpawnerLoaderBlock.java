package com.thatmg393.spawnerloader.block.impl;

import com.thatmg393.spawnerloader.block.base.BlockExt;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

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
}
