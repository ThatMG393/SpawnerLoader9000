package com.thatmg393.spawnerloader;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thatmg393.spawnerloader.block.impl.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.block.impl.blockitem.SpawnerLoaderBlockItem;
import com.thatmg393.spawnerloader.block.registry.BlockRegistry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class SpawnerLoader9000 implements ModInitializer {
	public static final String MOD_ID = "spawnerloader9000";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("what is this name T-T /\\");

		BlockRegistry.<SpawnerLoaderBlock, SpawnerLoaderBlockItem>register(
			new BlockRegistry.Entry<>(
				new SpawnerLoaderBlock(),
				(block) -> new SpawnerLoaderBlockItem(block)
			)
		);
		
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			LOGGER.info("Adding polymer resource pack.");
			PolymerResourcePackUtils.addModAssets(MOD_ID);
			PolymerResourcePackUtils.markAsRequired();
		});
	}
}
