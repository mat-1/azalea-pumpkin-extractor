package de.snowii.extractor.extractors

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import de.snowii.extractor.Extractor
import net.minecraft.tags.TagNetworkSerialization
import net.minecraft.server.MinecraftServer


class Tags : Extractor.Extractor {
    override fun fileName(): String {
        return "tags.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val tagsJson = JsonObject()

        val tags = TagNetworkSerialization.serializeTagsToNetwork(server.registries())

        for (tag in tags.entries) {
            val tagGroupTagsJson = JsonObject()
            val tagValues =
                tag.value.resolve(server.registries().compositeAccess().lookupOrThrow(tag.key))
            for (value in tagValues.tags) {
                val tagGroupTagsJsonArray = JsonArray()
                for (tagVal in value.value) {
                    tagGroupTagsJsonArray.add(tagVal.unwrapKey().orElseThrow().identifier().path)
                }
                tagGroupTagsJson.add(value.key.location.toString(), tagGroupTagsJsonArray)
            }
            tagsJson.add(tag.key.identifier().path, tagGroupTagsJson)
        }

        return tagsJson
    }


}
