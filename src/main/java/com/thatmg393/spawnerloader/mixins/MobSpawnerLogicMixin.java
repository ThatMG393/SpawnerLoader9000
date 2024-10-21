package com.thatmg393.spawnerloader.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobSpawnerLogic.class)
public class MobSpawnerLogicMixin {
    @Inject(
        method = "isPlayerInRange",
        at = @At(value = "RETURN"),
        cancellable = true
    )
    public void isPlayerInRange(World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        boolean playerInRange = cir.getReturnValue();
        boolean isSL9kPresent = world.getBlockState(pos.up()).getBlock().equals(null);

        if (!playerInRange && isSL9kPresent) cir.cancel();
    }
}
