package com.thatmg393.spawnerloader.block.base;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet.Builder;
import net.minecraft.util.Identifier;

public abstract class BlockExt extends Block {
    public BlockExt(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, Builder builder) {
        return Collections.singletonList(new ItemStack((this)));
    }

    public abstract Identifier getBlockID();
}
