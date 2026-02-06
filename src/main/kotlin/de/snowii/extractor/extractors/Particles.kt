package de.snowii.extractor.extractors

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import de.snowii.extractor.Extractor
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.MinecraftServer


class Particles : Extractor.Extractor {
    override fun fileName(): String {
        return "particles.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val particlesJson = JsonArray()
        for (particle in BuiltInRegistries.PARTICLE_TYPE) {
            particlesJson.add(
                BuiltInRegistries.PARTICLE_TYPE.getKey(particle)!!.path,
            )
        }

        return particlesJson
    }
}
