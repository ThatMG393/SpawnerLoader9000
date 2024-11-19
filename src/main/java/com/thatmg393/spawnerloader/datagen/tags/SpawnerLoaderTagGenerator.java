package com.thatmg393.spawnerloader.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.thatmg393.spawnerloader.block.registry.BlockRegistry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;

public class SpawnerLoaderTagGenerator extends FabricTagProvider.BlockTagProvider {
    public SpawnerLoaderTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                  .add(BlockRegistry.SPAWNER_LOADER_BLOCK.block())
                  .setReplace(false);
    }
}
