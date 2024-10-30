package com.thatmg393.spawnerloader.block.registry;

import java.util.HashMap;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.thatmg393.spawnerloader.block.base.BlockExt;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockRegistry {
    private static final HashMap<Identifier, BlockRegistry.Entry<?, ?>> registeredBlocks = new HashMap<>();

    public static record Entry<B extends BlockExt, I extends BlockItem>(
        @NotNull B block,
        @Nullable Function<B, I> blockItem
    ) { }

    @SuppressWarnings("unchecked")
    public static <B extends BlockExt, I extends BlockItem> B register(Entry<B, I> entry) {
        if (entry.blockItem != null) {
            BlockItem blockItem = entry.blockItem.apply(entry.block);
            if (blockItem != null)
                Registry.register(Registries.ITEM, entry.block.getBlockID(), blockItem);
        }

        Registry.register(Registries.BLOCK, entry.block.getBlockID(), entry.block);
        return (B) registeredBlocks.put(entry.block.getBlockID(), entry).block;
    }

    @SuppressWarnings("unchecked")
    public static <B extends BlockExt> B getBlock(Identifier blockId) {
        return (B) registeredBlocks.get(blockId).block;
    }
}
