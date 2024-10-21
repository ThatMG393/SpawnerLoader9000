package com.thatmg393.spawnerloader.recipe.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thatmg393.spawnerloader.SpawnerLoader9000;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ShapedRecipeJsonLoader {
    public static void loadJSONRecipe(String modId, String recipeName, RecipeExporter exporter) {
        try {
            String jsonString = IOUtils.toString(
                ShapedRecipeJsonLoader.class.getResourceAsStream("/data/" + modId + "/recipes/" + recipeName + ".json"),
                StandardCharsets.UTF_8);

            new Parser(jsonString).parse().offerTo(exporter);
        } catch (IOException e) {
            SpawnerLoader9000.LOGGER.error("Failed to create recipe for " + recipeName);
            e.printStackTrace(System.err);
        }
    }

    // just a simple parser
    private static class Parser {
        JsonElement elementRoot;

        public Parser(String json) {
            this.elementRoot = JsonParser.parseString(json);
        }

        public ShapedRecipeJsonBuilder parse() {
            RecipeCategory category = RecipeCategory.valueOf(elementRoot.getAsJsonObject().get("category").getAsString().toUpperCase());

            JsonObject objectResult = elementRoot.getAsJsonObject().get("result").getAsJsonObject();
            ShapedRecipeJsonBuilder srjb = ShapedRecipeJsonBuilder.create(category, () -> {
                String ns = objectResult.get("item").getAsString().split("[:]", 1)[0];
                String id = objectResult.get("item").getAsString().split("[:]", 1)[1];

                return Registries.ITEM.get(Identifier.of(ns, id));
            }, objectResult.get("count").getAsInt());

            JsonArray arrayPattern = elementRoot.getAsJsonObject().get("pattern").getAsJsonArray();
            arrayPattern.asList().forEach((e) -> {
                srjb.pattern(e.getAsString());
            });

            JsonObject objectKey = elementRoot.getAsJsonObject().get("key").getAsJsonObject();
            objectKey.asMap().forEach((k, v) -> {
                JsonObject objectChar = v.getAsJsonObject();

                String ns = objectChar.get("item").getAsString().split("[:]", 1)[0];
                String id = objectChar.get("item").getAsString().split("[:]", 1)[1];

                srjb.input(k.charAt(0), TagKey.of(Registries.ITEM.getKey(), Identifier.of(ns, id)));
            });

            return srjb;
        }
    }
}
