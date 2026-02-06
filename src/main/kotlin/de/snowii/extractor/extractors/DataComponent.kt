package de.snowii.extractor.extractors

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import de.snowii.extractor.Extractor
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.server.MinecraftServer


class DataComponent : Extractor.Extractor {
    override fun fileName(): String {
        return "data_component.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val dataComponentJson = JsonObject()
        val list = server.registryAccess().lookupOrThrow(Registries.DATA_COMPONENT_TYPE).listElements().toList();
        for (item in list) {
            dataComponentJson.addProperty(item.value().toString(), BuiltInRegistries.DATA_COMPONENT_TYPE.getId(item.value()));
        }
        return dataComponentJson
    }
}
