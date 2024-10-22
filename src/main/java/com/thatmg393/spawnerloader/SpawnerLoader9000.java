package com.thatmg393.spawnerloader;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thatmg393.spawnerloader.block.impl.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.block.impl.blockitem.SpawnerLoaderBlockItem;
import com.thatmg393.spawnerloader.block.registry.BlockRegistry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Rarity;

public class SpawnerLoader9000 implements ModInitializer {
	public static final String MOD_ID = "spawnerloader9000";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("what is this name T-T /\\");

		BlockRegistry.<SpawnerLoaderBlock, SpawnerLoaderBlockItem>register(
			new BlockRegistry.Entry<>(
				new SpawnerLoaderBlock(
					AbstractBlock.Settings.copy(Blocks.COBBLESTONE)
						.allowsSpawning((state, world, pos, type) -> false)
						.hardness(8)
						.resistance(6)
						.solid()
						.sounds(BlockSoundGroup.ANVIL)
						.dropsLike(Blocks.COBBLESTONE)
				),
				(block) -> {
					return new SpawnerLoaderBlockItem(
						block,
						new Item.Settings()
							.rarity(Rarity.UNCOMMON)
					);
				}
			)
		);
		
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			LOGGER.info("Adding polymer resource pack!");
			PolymerResourcePackUtils.addModAssets(MOD_ID);
			PolymerResourcePackUtils.markAsRequired();
		});
	}
}
