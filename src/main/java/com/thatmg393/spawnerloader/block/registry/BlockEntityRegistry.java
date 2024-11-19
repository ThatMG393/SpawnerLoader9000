package com.thatmg393.spawnerloader.block.registry;

import java.util.HashMap;

import org.jetbrains.annotations.NotNull;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.block.impl.block.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.block.impl.block.entity.SpawnerLoaderBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class BlockEntityRegistry {
    public static final Entry<BlockEntityFactory<? extends SpawnerLoaderBlockEntity>, ? extends SpawnerLoaderBlock> SPAWNER_LOADER_BLOCK_ENTITY;

    static {
        if (SpawnerLoader9000.POLYMER_PRESENT) {
            SPAWNER_LOADER_BLOCK_ENTITY = null;
        } else {
            SPAWNER_LOADER_BLOCK_ENTITY = BlockEntityRegistry.register(
                new Entry<>(
                    SpawnerLoaderBlockEntity.BLOCK_ENTITY_ID,
                    SpawnerLoaderBlockEntity::new,
                    BlockRegistry.SPAWNER_LOADER_BLOCK.block()
                )
            );
        }
    }

    public static void callForInit() {
        SpawnerLoader9000.LOGGER.info("BlockEntityRegistry init with " +
             ((SpawnerLoader9000.POLYMER_PRESENT) ? "vanilla client support!" : "nothing, nothing special."));
    }

    private static final HashMap<Identifier, BlockEntityType<? extends BlockEntity>> BE_TYPES = new HashMap<>();

    public static record Entry<F extends BlockEntityFactory<? extends BlockEntity>, B extends Block>(
        @NotNull Identifier blockEntityId,
        @NotNull F blockEntityFactory,
        @NotNull B block
    ) { }

    public static interface BlockEntityFactory<T extends BlockEntity> {
        public T create(BlockPos pos, BlockState state);
    }

    public static <F extends BlockEntityFactory<? extends BlockEntity>, B extends Block> Entry<F, B> register(Entry<F, B> entry) {
        BlockEntityType<? extends BlockEntity> blockEntityType = BlockEntityType.Builder.create(
            (pos, state) -> entry.blockEntityFactory.create(pos, state),
            entry.block
        ).build();

        Registry.register(Registries.BLOCK_ENTITY_TYPE, entry.blockEntityId, blockEntityType);

        BE_TYPES.put(
            entry.blockEntityId,
            blockEntityType
        );

        return entry;
    }

    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity> BlockEntityType<E> getBlockEntityType(Identifier blockEntityId) {
        return (BlockEntityType<E>) BE_TYPES.get(blockEntityId);
    }
}
