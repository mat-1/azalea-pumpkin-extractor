package de.snowii.extractor.extractors.non_registry

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import de.snowii.extractor.Extractor
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.block.LevelEvent

class WorldEvent : Extractor.Extractor {
    override fun fileName(): String {
        return "world_event.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val jsonObject = JsonObject()
        val fields = LevelEvent::class.java.declaredFields

        for (field in fields) {
            if (field.type == Int::class.javaPrimitiveType || field.type == Int::class.java) {
                if (field.name.startsWith("field")) continue
                field.isAccessible = true
                val intValue = field.get(null) as Int
                jsonObject.addProperty(field.name, intValue)
            }
        }

        return jsonObject
    }
}