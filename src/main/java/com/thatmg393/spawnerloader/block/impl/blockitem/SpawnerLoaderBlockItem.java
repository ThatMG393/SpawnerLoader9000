package com.thatmg393.spawnerloader.block.impl.blockitem;

import eu.pb4.polymer.core.api.item.PolymerItem;

import com.thatmg393.spawnerloader.SpawnerLoader9000;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

public class SpawnerLoaderBlockItem extends BlockItem implements PolymerItem {
    public SpawnerLoaderBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

	@Override
	public Item getPolymerItem(ItemStack itemStack, ServerPlayerEntity player) {
		return Registries.ITEM.get(Identifier.of("minecraft", "stone"));
	}

	/*
	@Override
	public Identifier getPolymerResourceLocation() {
		return Identifier.of(SpawnerLoader9000.MOD_ID, "block/spawner_loader_block");
	} */
}
