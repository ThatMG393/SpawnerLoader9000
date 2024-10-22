package com.thatmg393.spawnerloader.polymer.impl.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.thatmg393.spawnerloader.block.impl.blockitem.SpawnerLoaderBlockItem;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SpawnerLoaderPolymerBlockItem extends SpawnerLoaderBlockItem implements PolymerItem {
    public SpawnerLoaderPolymerBlockItem(Block block) {
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
        return PolymerResourcePackUtils.requestModel(getPolymerItem(), getCorrelatingBlock().getBlockID()).value();
    }

    @Override
    public void modifyClientTooltip(List<Text> tooltip, ItemStack stack, @Nullable ServerPlayerEntity player) {
        tooltip.add(Text.literal("The spawner loader.").formatted(Formatting.BOLD));
    }
}
