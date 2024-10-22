package com.thatmg393.spawnerloader.block.impl.blockitem;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import eu.pb4.polymer.core.api.item.PolymerItem;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

public class SpawnerLoaderBlockItem extends BlockItem implements PolymerItem {
    public SpawnerLoaderBlockItem(Block block) {
        super(block, new Settings()
		                .maxCount(16)
						.rarity(Rarity.UNCOMMON));
    }

	@Override
	public Item getPolymerItem(ItemStack itemStack, ServerPlayerEntity player) {
		return Items.BEACON;
	}

	@Override
	public void modifyClientTooltip(List<Text> tooltip, ItemStack stack, @Nullable ServerPlayerEntity player) {
		tooltip.add(Text.literal("The spawner loader.").formatted(Formatting.BOLD));
	}

	/*
	@Override
	public Identifier getPolymerResourceLocation() {
		return Identifier.of(SpawnerLoader9000.MOD_ID, "block/spawner_loader_block");
	} */
}
