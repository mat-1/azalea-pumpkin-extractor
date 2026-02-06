package de.snowii.extractor.extractors

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import de.snowii.extractor.Extractor
import net.minecraft.core.registries.Registries
import net.minecraft.server.MinecraftServer

class GameEvent : Extractor.Extractor {
    override fun fileName(): String {
        return "game_event.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val gameEventJson = JsonArray()
        val gameEventTypeRegistry =
            server.registryAccess().lookupOrThrow(Registries.GAME_EVENT)
        for (event in gameEventTypeRegistry) {
            gameEventJson.add(
                gameEventTypeRegistry.getKey(event)!!.path,
            )
        }

        return gameEventJson
    }
}