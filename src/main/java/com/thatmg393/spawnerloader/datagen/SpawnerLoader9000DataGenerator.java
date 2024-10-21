package com.thatmg393.spawnerloader.datagen;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.recipe.SpawnerLoaderRecipeGenerator;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SpawnerLoader9000DataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		SpawnerLoader9000.LOGGER.info("my code is so cursed.");
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SpawnerLoaderRecipeGenerator::new);
	}
}
