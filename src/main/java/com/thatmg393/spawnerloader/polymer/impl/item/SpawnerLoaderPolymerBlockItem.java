package com.thatmg393.spawnerloader.polymer.impl.item;

import org.jetbrains.annotations.Nullable;

import com.thatmg393.spawnerloader.block.impl.block.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.block.impl.block.item.SpawnerLoaderBlockItem;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpawnerLoaderPolymerBlockItem extends SpawnerLoaderBlockItem implements PolymerItem {
    private final PolymerModelData polymerItemModelData = PolymerResourcePackUtils.requestModel(getPolymerItem(), IdentifierUtils.getItemIdentifier("spawner_loader_block"));

    public SpawnerLoaderPolymerBlockItem(SpawnerLoaderBlock block) {
        super(block);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, ServerPlayerEntity player) {
        return getPolymerItem();
    }

    public Item getPolymerItem() {
        return Items.LODESTONE;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.polymerItemModelData.value();
    }
}
