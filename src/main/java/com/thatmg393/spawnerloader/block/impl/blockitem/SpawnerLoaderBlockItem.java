package com.thatmg393.spawnerloader.block.impl.blockitem;

import com.thatmg393.spawnerloader.block.base.BlockExt;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Rarity;

public class SpawnerLoaderBlockItem extends BlockItem {
	private final BlockExt correlatingBlock;

	public SpawnerLoaderBlockItem(Block block) {
        super(block,
			new Settings()
				.maxCount(16)
				.rarity(Rarity.UNCOMMON));
		this.correlatingBlock = (BlockExt) block;
    }

	public BlockExt getCorrelatingBlock() {
		return correlatingBlock;
	}
}
