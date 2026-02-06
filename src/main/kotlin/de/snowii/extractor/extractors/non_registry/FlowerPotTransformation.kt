package de.snowii.extractor.extractors.non_registry

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import de.snowii.extractor.Extractor
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.MinecraftServer

class FlowerPotTransformation : Extractor.Extractor {
    override fun fileName(): String {
        return "flower_pot_transformations.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val flowerPotsJson = JsonObject()
        for ((block, pottedBlock) in FlowerPotBlock.POTTED_BY_CONTENT){
            if (BuiltInRegistries.BLOCK.getId(block) == 0) continue
            flowerPotsJson.add(
                BuiltInRegistries.ITEM.getId(block.asItem()!!).toString(),
                JsonPrimitive(BuiltInRegistries.BLOCK.getId(pottedBlock)))
        }

        return flowerPotsJson
    }
}