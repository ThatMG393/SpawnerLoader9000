package com.thatmg393.spawnerloader.block.impl.block.item;

import java.util.List;

import com.thatmg393.spawnerloader.block.impl.block.SpawnerLoaderBlock;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

public class SpawnerLoaderBlockItem extends BlockItem {
	public SpawnerLoaderBlockItem(SpawnerLoaderBlock block) {
        super(
			block,
			new Settings()
			    .maxCount(16)
				.rarity(Rarity.UNCOMMON)
		);
    }

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.literal("Place this on top of a spawner and see the magic happen!").formatted(Formatting.ITALIC));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("The spawner loader.").formatted(Formatting.BOLD));
	}
}
