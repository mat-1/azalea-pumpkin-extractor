package de.snowii.extractor.extractors

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import de.snowii.extractor.Extractor
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.MinecraftServer

class EntityAttributes : Extractor.Extractor {
    override fun fileName(): String {
        return "attributes.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val finalJson = JsonObject()
        for (attribute in BuiltInRegistries.ATTRIBUTE) {
            var subObject = JsonObject()
            subObject.addProperty("id", BuiltInRegistries.ATTRIBUTE.getId(attribute))
            subObject.addProperty("default_value", attribute.defaultValue)

            finalJson.add(
                BuiltInRegistries.ATTRIBUTE.getKey(attribute)!!.path,
                subObject
            )
        }

        return finalJson
    }
}