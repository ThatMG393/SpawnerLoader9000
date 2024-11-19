package com.thatmg393.spawnerloader.mixins;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.thatmg393.spawnerloader.block.impl.block.SpawnerLoaderBlock;
import com.thatmg393.spawnerloader.entity.data.PassiveEntitySpawnerData;
import com.thatmg393.spawnerloader.entity.data.SpawnerEntityData;

import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.registry.Registries;
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
        if (!cir.getReturnValue()) // only check if player is actually not present
            cir.setReturnValue(
                world.getBlockState(pos.up()).isOf(Registries.BLOCK.get(SpawnerLoaderBlock.BLOCK_ID))
            );
    }

    @Redirect(
        method = "serverTick", 
        at = @At(value = "INVOKE", 
        target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;"))
    private EntityData initializeRedir(
        MobEntity mobEntity, ServerWorldAccess world, 
        LocalDifficulty difficulty, SpawnReason spawnReason, 
        @Nullable EntityData entityData, ServerWorld world2, BlockPos pos
    ) {
        if (mobEntity instanceof PassiveEntity passiveEntity) {
            PassiveEntitySpawnerData d = new PassiveEntitySpawnerData(false);
            d.setSpawnerPosition(pos);

            return passiveEntity.initialize(world, difficulty, spawnReason, d);
        } else {
            return mobEntity.initialize(world, difficulty, spawnReason, new SpawnerEntityData(pos));
        }
    }
}
