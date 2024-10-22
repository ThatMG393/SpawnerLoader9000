package com.thatmg393.spawnerloader.polymer;

import com.thatmg393.spawnerloader.SpawnerLoader9000;
import com.thatmg393.spawnerloader.block.registry.BlockRegistry;
import com.thatmg393.spawnerloader.polymer.impl.block.SpawnerLoaderPolymerBlock;
import com.thatmg393.spawnerloader.polymer.impl.item.SpawnerLoaderPolymerBlockItem;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;

public class PolymerIntegrationEntrypoint {
    public static void initializePolymer() {
        SpawnerLoader9000.LOGGER.info("Initializing with Vanilla support!");

        PolymerResourcePackUtils.addModAssets(SpawnerLoader9000.MOD_ID);
		PolymerResourcePackUtils.markAsRequired();

        BlockRegistry.<SpawnerLoaderPolymerBlock, SpawnerLoaderPolymerBlockItem>register(
			new BlockRegistry.Entry<>(
				new SpawnerLoaderPolymerBlock(),
				(block) -> new SpawnerLoaderPolymerBlockItem(block)
			)
		);
    }
}
