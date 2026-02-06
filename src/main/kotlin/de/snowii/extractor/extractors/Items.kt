package de.snowii.extractor.extractors

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import de.snowii.extractor.Extractor
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.item.Item
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryOps
import net.minecraft.server.MinecraftServer


class Items : Extractor.Extractor {
    override fun fileName(): String {
        return "items.json"
    }


    override fun extract(server: MinecraftServer): JsonElement {
        val itemsJson = JsonObject()

        for (item in server.registryAccess().lookupOrThrow(Registries.ITEM).listElements().toList()) {
            val itemJson = JsonObject()
            val realItem: Item = item.value()

            itemJson.addProperty("id", BuiltInRegistries.ITEM.getId(realItem))
            itemJson.add(
                "components",
                DataComponentMap.CODEC.encodeStart(
                    RegistryOps.create(JsonOps.INSTANCE, server.registryAccess()),
                    realItem.components()
                ).getOrThrow()
            )

            itemsJson.add(BuiltInRegistries.ITEM.getKey(realItem).path, itemJson)
        }
        return itemsJson
    }
}
