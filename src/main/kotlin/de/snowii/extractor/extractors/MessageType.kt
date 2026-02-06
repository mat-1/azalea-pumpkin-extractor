package de.snowii.extractor.extractors

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import de.snowii.extractor.Extractor
import net.minecraft.network.chat.ChatType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryOps
import net.minecraft.server.MinecraftServer

class MessageType : Extractor.Extractor {
    override fun fileName(): String {
        return "message_type.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val messagesJson = JsonObject()
        val messageTypeRegistry =
            server.registryAccess().lookupOrThrow(Registries.CHAT_TYPE)
        for (type in messageTypeRegistry) {
            val json = JsonObject()
            json.addProperty("id", messageTypeRegistry.getId(type))
            json.add(
                "components", ChatType.DIRECT_CODEC.encodeStart(
                    RegistryOps.create(JsonOps.INSTANCE, server.registryAccess()), type
                ).getOrThrow()
            )
            messagesJson.add(
                messageTypeRegistry.getKey(type)!!.path,
                json
            )
        }

        return messagesJson
    }
}