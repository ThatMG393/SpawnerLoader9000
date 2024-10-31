package com.thatmg393.spawnerloader.datagen.loot;

import java.util.concurrent.CompletableFuture;

import com.thatmg393.spawnerloader.block.registry.BlockRegistry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class SpawnerLoaderLootTableGenerator extends FabricBlockLootTableProvider {
    public SpawnerLoaderLootTableGenerator(FabricDataOutput generator, CompletableFuture<WrapperLookup> registryLookup) {
        super(generator, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(BlockRegistry.SPAWNER_LOADER.block());
            /* LootTable.builder().pool(
                LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0f))
                    .with(
                        ItemEntry.builder(BlockRegistry.SPAWNER_LOADER.block())
                            .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)))
                    )   
            )
        );*/
    }
}
