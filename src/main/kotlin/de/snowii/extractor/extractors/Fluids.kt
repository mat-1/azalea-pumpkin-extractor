package de.snowii.extractor.extractors

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import de.snowii.extractor.Extractor
import net.minecraft.world.level.block.Block
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.MinecraftServer

class Fluids : Extractor.Extractor {
    override fun fileName(): String {
        return "fluids.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val topLevelJson = JsonArray()

        for (fluid in BuiltInRegistries.FLUID) {
            val fluidJson = JsonObject()
            fluidJson.addProperty("id", BuiltInRegistries.FLUID.getId(fluid))
            fluidJson.addProperty("name", BuiltInRegistries.FLUID.getKey(fluid).path)

            val propsJson = JsonArray()
            for (prop in fluid.stateDefinition.properties) {
                val propJson = JsonObject()

                propJson.addProperty("name", prop.name)

                val valuesJson = JsonArray()
                for (value in prop.possibleValues) {
                    valuesJson.add(value.toString().lowercase())
                }
                propJson.add("values", valuesJson)

                propsJson.add(propJson)
            }
            fluidJson.add("properties", propsJson)

            val statesJson = JsonArray()
            for ((index, state) in fluid.stateDefinition.possibleStates.withIndex()) {
                val stateJson = JsonObject()
                stateJson.addProperty("height", state.ownHeight)
                stateJson.addProperty("level", state.amount)
                stateJson.addProperty("is_empty", state.isEmpty)
                stateJson.addProperty("blast_resistance", state.explosionResistance)
                stateJson.addProperty("block_state_id", Block.getId(state.createLegacyBlock()))
                stateJson.addProperty("is_still", state.isSource)
                // TODO: Particle effects

                if (fluid.defaultFluidState() == state) {
                    fluidJson.addProperty("default_state_index", index)
                }

                statesJson.add(stateJson)
            }
            fluidJson.add("states", statesJson)

            topLevelJson.add(fluidJson)
        }

        return topLevelJson
    }
}