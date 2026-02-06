package de.snowii.extractor.extractors

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import de.snowii.extractor.Extractor
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.MinecraftServer

class Screens : Extractor.Extractor {
    override fun fileName(): String {
        return "screens.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val screensJson = JsonArray()
        for (screen in BuiltInRegistries.MENU) {
            screensJson.add(
                BuiltInRegistries.MENU.getKey(screen)!!.path,
            )
        }

        return screensJson
    }
}
