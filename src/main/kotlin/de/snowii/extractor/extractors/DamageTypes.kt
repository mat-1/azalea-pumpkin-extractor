package de.snowii.extractor.extractors

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import de.snowii.extractor.Extractor
import net.minecraft.world.damagesource.DamageType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryOps
import net.minecraft.server.MinecraftServer

class DamageTypes : Extractor.Extractor {
    override fun fileName(): String {
        return "damage_type.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val damageTypesJson = JsonObject()
        val damageTypeRegistry = server.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
        for (type in damageTypeRegistry) {
            val json = JsonObject()
            json.addProperty("id", damageTypeRegistry.getId(type))
            json.add(
                "components",
                DamageType.DIRECT_CODEC
                    .encodeStart(
                        RegistryOps.create(JsonOps.INSTANCE, server.registryAccess()),
                        type
                    )
                    .getOrThrow()
            )
            damageTypesJson.add(damageTypeRegistry.getKey(type)!!.path, json)
        }

        return damageTypesJson
    }
}
