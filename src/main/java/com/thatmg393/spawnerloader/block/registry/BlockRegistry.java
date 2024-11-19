package com.thatmg393.spawnerloader.block.registry;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.block.getters.BlockIDGetter;
import com.thatmg393.spawnerloader.block.impl.block.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.block.impl.block.item.SpawnerLoaderBlockItem;
import com.thatmg393.spawnerloader.polymer.impl.block.SpawnerLoaderPolymerBlock;
import com.thatmg393.spawnerloader.polymer.impl.item.SpawnerLoaderPolymerBlockItem;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockRegistry {
    public static final Entry<? extends SpawnerLoaderBlock, ? extends SpawnerLoaderBlockItem> SPAWNER_LOADER_BLOCK;

    static {
        if (SpawnerLoader9000.POLYMER_PRESENT) {
            SPAWNER_LOADER_BLOCK = BlockRegistry.register(
                new Entry<>(
                    new SpawnerLoaderPolymerBlock(),
                    (block) -> new SpawnerLoaderPolymerBlockItem(block)
                )
            );
        } else {
            SPAWNER_LOADER_BLOCK = BlockRegistry.register(
                new Entry<>(
                    new SpawnerLoaderBlock(),
                    (block) -> new SpawnerLoaderBlockItem(block)
                )
            );
        }
    }

    public static void callForInit() {
        SpawnerLoader9000.LOGGER.info("BlockRegistry init with " +
             ((SpawnerLoader9000.POLYMER_PRESENT) ? "vanilla client support!" : "nothing, nothing special."));
    }

    public static record Entry<B extends Block, I extends BlockItem>(
        @NotNull B block,
        @Nullable Function<B, I> blockItem
    ) { }

    public static <B extends Block, I extends BlockItem> Entry<B, I> register(Entry<B, I> entry) {
        if (entry.block instanceof BlockIDGetter castedBlock) {
            if (entry.blockItem != null) {
                I blockItem = entry.blockItem.apply(entry.block);
                if (blockItem != null)
                    Registry.register(Registries.ITEM, castedBlock.getBlockID(), blockItem);
            }

            Registry.register(Registries.BLOCK, castedBlock.getBlockID(), entry.block);

            return entry;
        }

        throw new IllegalStateException("Block should extend BlockIDGetter!");
    }
}
