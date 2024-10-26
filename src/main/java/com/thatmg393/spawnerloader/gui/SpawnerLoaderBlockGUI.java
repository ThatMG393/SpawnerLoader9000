package com.thatmg393.spawnerloader.gui;

import com.thatmg393.spawnerloader.block.impl.SpawnerLoaderBlock;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class SpawnerLoaderBlockGUI extends SimpleGui {
    private final BlockState blockState;
    private final BlockPos blockPos;

    private boolean shouldLoadChunksAroundBlock;

    private boolean hasChanges = false;

    public SpawnerLoaderBlockGUI(ServerPlayerEntity player, BlockState blockState, BlockPos blockPos) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.blockState = blockState;
        this.blockPos = blockPos;
        
        this.shouldLoadChunksAroundBlock = blockState.get(SpawnerLoaderBlock.ENABLE_CHUNK_LOADING);

        updateGuiElements();
    }

    @Override
    public Text getTitle() {
        return Text.literal("Spawner Loader Settings");
    }

    private void updateGuiElements() {
        GuiElementBuilder loadChunksAroundToggleButton = new GuiElementBuilder()
            .setItem(shouldLoadChunksAroundBlock ? Items.LIME_STAINED_GLASS_PANE : Items.RED_STAINED_GLASS_PANE)
            .setName(Text.literal("Load chunks nearby"))
            .addLoreLine(
                Text.literal("Load chunks in a 3x3 area centered on this block")
                    .formatted(Formatting.ITALIC, Formatting.GRAY)
            ).addLoreLine(Text.literal(""))
             .addLoreLine(
                Text.literal(shouldLoadChunksAroundBlock ? "Enabled : YES" : "Enabled : NO")
                    .formatted(shouldLoadChunksAroundBlock ? Formatting.GREEN : Formatting.RED)
            ).addLoreLine(
                Text.literal("Click to toggle")
                    .formatted(Formatting.GRAY, Formatting.ITALIC)
            ).setCallback((index, type, action, gui) -> {
                shouldLoadChunksAroundBlock = !shouldLoadChunksAroundBlock;
                hasChanges = !hasChanges;

                updateGuiElements();
            }
        );

        GuiElementBuilder applyChangesButton = new GuiElementBuilder()
            .setItem(hasChanges ? Items.LIME_STAINED_GLASS_PANE : Items.GRAY_STAINED_GLASS_PANE)
            .setName(
                Text.literal("Apply Changes")
                    .formatted(hasChanges ? Formatting.GREEN : Formatting.GRAY, Formatting.BOLD)
            ).addLoreLine(
                Text.literal("Applies block setting changes")
                    .formatted(Formatting.ITALIC, Formatting.GRAY)
            ).addLoreLine(Text.literal(""))
             .addLoreLine(
                Text.literal("Click to apply")
                    .formatted(Formatting.GRAY, Formatting.ITALIC)
            ) .setCallback((index, type, action, gui) -> {
                if (!hasChanges) return;

                player.getWorld().setBlockState(
                    blockPos,
                    blockState.with(SpawnerLoaderBlock.ENABLE_CHUNK_LOADING, shouldLoadChunksAroundBlock)
                );

                SpawnerLoaderBlockGUI.this.close();
            }
        );

        setSlot(13, loadChunksAroundToggleButton);
        setSlot(26, applyChangesButton);
    }

    public static void openFor(ServerPlayerEntity player, BlockState state, BlockPos pos) {
        new SpawnerLoaderBlockGUI(player, state, pos).open();
    }
}
