package com.thatmg393.spawnerloader.utils;

import com.thatmg393.spawnerloader.SpawnerLoader9000;

import net.minecraft.util.Identifier;

public class IdentifierUtils {
    public static Identifier getIdentifier(String id) {
        return Identifier.of(SpawnerLoader9000.MOD_ID, id);
    }

    public static Identifier getItemIdentifier(String id) {
        return getIdentifier("item/" + id);
    }

    public static Identifier getBlockIdentifier(String id) {
        return getIdentifier("block/" + id);
    }

    public static Identifier getIdentifierFromVanilla(String id) {
        return Identifier.of("minecraft", id);
    }
}
