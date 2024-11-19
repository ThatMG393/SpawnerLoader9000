package com.thatmg393.spawnerloader.datagen.recipe;

import java.util.concurrent.CompletableFuture;

import com.thatmg393.spawnerloader.block.impl.block.SpawnerLoaderBlock;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class SpawnerLoaderRecipeGenerator extends FabricRecipeProvider {
    public SpawnerLoaderRecipeGenerator(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(generator, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(
            RecipeCategory.MISC,
            () -> Registries.ITEM.get(SpawnerLoaderBlock.BLOCK_ID),
            1
        ).criterion("has_any", InventoryChangedCriterion.Conditions.items(
            () -> Registries.ITEM.get(Identifier.of("minecraft", "diamond")),
            () -> Registries.ITEM.get(Identifier.of("minecraft", "crying_obsidian")),
            () -> Registries.ITEM.get(Identifier.of("minecraft", "beacon"))
        )).pattern("###")
          .pattern("#O#")
          .pattern("---")
          .input('#', () -> Registries.ITEM.get(Identifier.of("minecraft", "diamond")))
          .input('-', () -> Registries.ITEM.get(Identifier.of("minecraft", "crying_obsidian")))
          .input('O', () -> Registries.ITEM.get(Identifier.of("minecraft", "beacon")))
          .offerTo(exporter);
    }
}
