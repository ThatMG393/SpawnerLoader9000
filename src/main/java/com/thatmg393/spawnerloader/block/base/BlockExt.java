package com.thatmg393.spawnerloader.block.base;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public abstract class BlockExt extends Block {
    public BlockExt(AbstractBlock.Settings settings) {
        super(settings);
    }

    public abstract Identifier getBlockID();
}
