package com.thatmg393.spawnerloader.entity.data;

import com.thatmg393.spawnerloader.entity.data.base.SpawnerPositionGetter;

import net.minecraft.entity.EntityData;
import net.minecraft.util.math.BlockPos;

public class SpawnerEntityData implements EntityData, SpawnerPositionGetter {
    private final BlockPos spawnerPos;

    public SpawnerEntityData(BlockPos spawnerPos) {
        this.spawnerPos = spawnerPos;
    }

    @Override
    public BlockPos getSpawnerPosition() {
        return spawnerPos;
    }
}
