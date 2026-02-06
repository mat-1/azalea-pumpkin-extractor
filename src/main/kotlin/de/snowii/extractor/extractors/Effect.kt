package de.snowii.extractor.extractors

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import de.snowii.extractor.Extractor
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryOps
import net.minecraft.core.Holder
import net.minecraft.server.MinecraftServer
import net.minecraft.sounds.SoundEvent
import net.minecraft.resources.Identifier
import java.util.Optional


class Effect : Extractor.Extractor {
    override fun fileName(): String {
        return "effect.json"
    }


    override fun extract(server: MinecraftServer): JsonElement {
        val json = JsonObject()
        for (potion in server.registryAccess().lookupOrThrow(Registries.MOB_EFFECT).listElements().toList()) {
            val itemJson = JsonObject()
            val realPotion = potion.value()
            itemJson.addProperty("id", BuiltInRegistries.MOB_EFFECT.getId(realPotion))
            itemJson.addProperty("category", realPotion.category.toString())
            itemJson.addProperty("color", realPotion.color)
            if (realPotion.blendInDurationTicks != 0 || realPotion.blendOutDurationTicks != 0 || realPotion.blendOutAdvanceTicks != 0) {
                itemJson.addProperty("fade_in_ticks", realPotion.blendInDurationTicks)
                itemJson.addProperty("fade_out_ticks", realPotion.blendOutDurationTicks)
                itemJson.addProperty("fade_out_threshold_ticks", realPotion.blendOutAdvanceTicks)
            }
            itemJson.addProperty("translation_key", realPotion.descriptionId)

            val t3 = MobEffect::class.java.getDeclaredField("applySound")
            t3.isAccessible = true
            val applySound = t3.get(realPotion) as? Optional<SoundEvent>
            applySound?.ifPresent { soundEvent -> itemJson.addProperty("apply_sound", BuiltInRegistries.SOUND_EVENT.getKey(soundEvent)!!.path)}

            val attributesRegistry =
                server.registryAccess().lookupOrThrow(Registries.ATTRIBUTE)

            val attributeModifiersJson = JsonArray()
            realPotion.createModifiers(0) { reg, mod ->
                val potionJson = JsonObject()
                potionJson.addProperty("attribute", attributesRegistry.getKey(reg.value())!!.path)
                potionJson.addProperty("operation", mod.operation.toString())
                potionJson.addProperty("id", mod.id.toString())
                potionJson.addProperty("baseValue", mod.amount)
                attributeModifiersJson.add(potionJson)
            }
            itemJson.add("attribute_modifiers", attributeModifiersJson)

            BuiltInRegistries.MOB_EFFECT.getKey(realPotion)?.let { json.add(it.path, itemJson) }
        }
        return json
    }
}
