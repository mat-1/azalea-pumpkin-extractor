package de.snowii.extractor.extractors

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import de.snowii.extractor.Extractor
import net.minecraft.world.level.block.ComposterBlock
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.MinecraftServer

class ComposterIncreaseChance : Extractor.Extractor {
    override fun fileName(): String {
        return "composter_increase_chance.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val composterChancesJson = JsonObject()
        for ((item, chance) in ComposterBlock.COMPOSTABLES) {
            composterChancesJson.add(BuiltInRegistries.ITEM.getId(item.asItem()!!).toString(), JsonPrimitive(chance))
        }
        return composterChancesJson
    }
}
