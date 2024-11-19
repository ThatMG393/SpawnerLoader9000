package com.thatmg393.spawnerloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thatmg393.spawnerloader.block.registry.BlockEntityRegistry;
import com.thatmg393.spawnerloader.block.registry.BlockRegistry;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class SpawnerLoader9000 implements ModInitializer {
	public static final String MOD_ID = "spawnerloader9000";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean POLYMER_PRESENT = FabricLoader.getInstance().isModLoaded("polymer-bundled");/* || FabricLoader.getInstance().isDevelopmentEnvironment(); */

	@Override
	public void onInitialize() {
		LOGGER.info("what is this name T-T /\\");
		BlockRegistry.callForInit();
		BlockEntityRegistry.callForInit();

		if (POLYMER_PRESENT) {
			SpawnerLoader9000.LOGGER.info("Initializing with Vanilla support!");

			PolymerResourcePackUtils.addModAssets(MOD_ID);
			PolymerResourcePackUtils.markAsRequired();
		}
	}
}
