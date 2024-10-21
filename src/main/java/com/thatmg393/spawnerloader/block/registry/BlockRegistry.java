package com.thatmg393.spawnerloader.block.registry;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.thatmg393.spawnerloader.block.base.BlockExt;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockRegistry {
    public static record Entry<B extends BlockExt, I extends BlockItem>(
        @NotNull B block,
        @Nullable Function<B, I> blockItem
    ) { }

    public static <B extends BlockExt, I extends BlockItem> Entry<B, I> register(Entry<B, I> entry) {
        if (entry.blockItem != null) {
            BlockItem blockItem = entry.blockItem.apply(entry.block);
            if (blockItem != null)
                Registry.register(Registries.ITEM, entry.block.getBlockID(), blockItem);
        }

        Registry.register(Registries.BLOCK, entry.block.getBlockID(), entry.block);

        return entry;
    }
}
