package com.thatmg393.spawnerloader.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.thatmg393.spawnerloader.block.base.BlockExt;
import com.thatmg393.spawnerloader.block.impl.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.entity.data.SpawnerEntityData;
import com.thatmg393.spawnerloader.mixins.accessor.SpawnReasonAccessor;
import com.thatmg393.spawnerloader.mixins.accessor.SpawnerPosAccessor;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

@Mixin(MobEntity.class)
public class MobEntityMixin implements SpawnReasonAccessor, SpawnerPosAccessor {
    @Unique
    private SpawnReason spawnReason = null;

    @Unique
    public BlockPos spawnerPos = null;

    @Inject(method = "initialize", at = @At("HEAD"))
    private void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfo ci) {
        this.spawnReason = spawnReason;
        if (spawnReason == SpawnReason.SPAWNER
         && entityData instanceof SpawnerEntityData) {
            this.spawnerPos = ((SpawnerEntityData) entityData).getSpawnerPos();
        }
    }

    @Inject(method = "canDespawn", at = @At("HEAD"), cancellable = true)
    private void preventDespawn(CallbackInfoReturnable<Boolean> cir) {
        World world = ((MobEntity) (Object) this).getEntityWorld();

        if (!world.isClient && spawnReason == SpawnReason.SPAWNER) {
            try {
                BlockState blockState = world.getBlockState(spawnerPos.up());

                if (!blockState.isAir()) { // if the cast fails then 100% its not our block
                    BlockExt block = (BlockExt) blockState.getBlock();
                    if (block.getBlockID().equals(SpawnerLoaderBlock.BLOCK_ID)) {
                        cir.setReturnValue(false);
                    }
                }
            } catch (ClassCastException e) { }
        }
    }

    @Override
    @Unique
    public SpawnReason getSpawnReason() {
        return this.spawnReason;
    }

    @Override
    @Unique
    public BlockPos getSpawnerPos() {
        return this.spawnerPos;
    }
}
