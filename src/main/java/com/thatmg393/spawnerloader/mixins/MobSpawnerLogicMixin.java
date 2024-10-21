package com.thatmg393.spawnerloader.mixins;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.thatmg393.spawnerloader.block.base.BlockExt;
import com.thatmg393.spawnerloader.block.impl.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.entity.data.SpawnerEntityData;

import net.minecraft.block.BlockState;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

@Mixin(MobSpawnerLogic.class)
public class MobSpawnerLogicMixin {
    @Inject(
        method = "isPlayerInRange",
        at = @At(value = "RETURN"),
        cancellable = true
    )
    public void isPlayerInRangeInj(World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) { // only check if player is actually not present
            try {
                BlockState blockState = world.getBlockState(pos.up());

                if (!blockState.isAir()) { // if the cast fails then 100% its not our block
                    BlockExt block = (BlockExt) blockState.getBlock();
                    if (block.getBlockID().equals(SpawnerLoaderBlock.BLOCK_ID)) {
                        cir.setReturnValue(true);
                    }
                }
            } catch (ClassCastException e) { }
        }
    }

    /*
    @Inject(method = "serverTick", at = @At(value = "INVOKE", 
            target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void serverTickInj(ServerWorld world, BlockPos pos, CallbackInfo ci, double d, double e, int i, Entity entity) {
        if (entity instanceof MobEntity mobEntity) {
            SpawnerEntityData spawnerData = new SpawnerEntityData(pos);
            mobEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.SPAWNER, spawnerData);
        }
    } */

    @Redirect(
        method = "serverTick", 
        at = @At(value = "INVOKE", 
        target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;"))
    private EntityData initializeRedir(
        MobEntity mobEntity, ServerWorldAccess world, 
        LocalDifficulty difficulty, SpawnReason spawnReason, 
        @Nullable EntityData entityData, ServerWorld world2, BlockPos pos
    ) {
        SpawnerEntityData spawnerData = new SpawnerEntityData(pos);
        mobEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.SPAWNER, spawnerData);

        return spawnerData;
    }
}
