package com.thatmg393.spawnerloader.recipe;

import java.util.concurrent.CompletableFuture;
import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.recipe.util.ShapedRecipeJsonLoader;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.registry.RegistryWrapper;

public class SpawnerLoaderRecipeGenerator extends FabricRecipeProvider {
    public SpawnerLoaderRecipeGenerator(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(generator, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonLoader.loadJSONRecipe(
            SpawnerLoader9000.MOD_ID, 
            "spawner_loader_block",
            exporter
        );
    }
}
