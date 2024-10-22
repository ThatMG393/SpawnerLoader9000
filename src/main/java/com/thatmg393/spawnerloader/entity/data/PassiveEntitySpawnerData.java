package com.thatmg393.spawnerloader.entity.data;

import com.thatmg393.spawnerloader.entity.data.base.SpawnerPositionGetter;

import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.math.BlockPos;

public class PassiveEntitySpawnerData extends PassiveEntity.PassiveData implements SpawnerPositionGetter {
    private BlockPos spawnerPos;

    public PassiveEntitySpawnerData(boolean babyAllowed) {
        super(babyAllowed);
    }

    public PassiveEntitySpawnerData(float babyChance) {
        super(babyChance);
    }

    public void setSpawnerPosition(BlockPos pos) {
        this.spawnerPos = pos;
    }

    @Override
    public BlockPos getSpawnerPosition() {
        return spawnerPos;
    }
}
