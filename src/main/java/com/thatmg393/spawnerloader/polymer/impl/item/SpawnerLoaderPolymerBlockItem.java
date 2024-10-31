package com.thatmg393.spawnerloader.polymer.impl.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.thatmg393.spawnerloader.block.impl.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.block.impl.blockitem.SpawnerLoaderBlockItem;
import com.thatmg393.spawnerloader.polymer.impl.block.SpawnerLoaderPolymerBlock;
import com.thatmg393.spawnerloader.utils.IdentifierUtils;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SpawnerLoaderPolymerBlockItem extends SpawnerLoaderBlockItem implements PolymerItem {
    private final PolymerModelData polymerItemModelData;

    public SpawnerLoaderPolymerBlockItem(SpawnerLoaderBlock block) {
        super((SpawnerLoaderPolymerBlock) block);
        this.polymerItemModelData = PolymerResourcePackUtils.requestModel(getPolymerItem(), IdentifierUtils.getIdentifier("item/spawner_loader_block"));
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

    @Override
    public void modifyClientTooltip(List<Text> tooltip, ItemStack stack, @Nullable ServerPlayerEntity player) {
        this.appendTooltip(stack, null, tooltip, null);
    }
}
