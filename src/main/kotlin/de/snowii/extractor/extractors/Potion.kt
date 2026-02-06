package de.snowii.extractor.extractors

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import de.snowii.extractor.Extractor
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryOps
import net.minecraft.server.MinecraftServer


class Potion : Extractor.Extractor {
    override fun fileName(): String {
        return "potion.json"
    }


    override fun extract(server: MinecraftServer): JsonElement {
        val json = JsonObject()
        for (potion in server.registryAccess().lookupOrThrow(Registries.POTION).listElements().toList()) {
            val itemJson = JsonObject()
            val realPotion = potion.value()
            val array = JsonArray()
            itemJson.addProperty("id", BuiltInRegistries.POTION.getId(realPotion))
            itemJson.addProperty("base_name", realPotion.name())
            for (effect in realPotion.effects) {
                val obj = JsonObject()
                obj.addProperty("effect_type", effect.effect.unwrapKey().get().identifier().toString())
                obj.addProperty("duration", effect.duration)
                obj.addProperty("amplifier", effect.amplifier)
                obj.addProperty("ambient", effect.isAmbient)
                obj.addProperty("show_particles", effect.isVisible)
                obj.addProperty("show_icon", effect.showIcon())
                array.add(obj)
            }
            itemJson.add("effects", array)
            BuiltInRegistries.POTION.getKey(realPotion)?.let { json.add(it.path, itemJson) }
        }
        return json
    }
}
