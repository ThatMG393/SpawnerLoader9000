package com.thatmg393.spawnerloader.entity.data;

import net.minecraft.entity.EntityData;
import net.minecraft.util.math.BlockPos;

public class SpawnerEntityData implements EntityData {
    private final BlockPos spawnerPos;

    public SpawnerEntityData(BlockPos spawnerPos) {
        this.spawnerPos = spawnerPos;
    }

    public BlockPos getSpawnerPos() {
        return spawnerPos;
    }
}
