package ru.igorlo.generation.entities

import ru.igorlo.generation.GenerationParameters
import ru.igorlo.Utilities
import kotlin.random.Random

data class NPC(val name: String, val experience: Int, val description: String, val fk_location: Int) :
    DBEntity {
    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["name"] = name
        map["experience"] = experience
        map["description"] = description
        map["fk_location"] = fk_location
        return map
    }

    companion object {
        fun generateNpcs(
            locationSource : Collection<Int>,
            quantity: Int = GenerationParameters.GEN_NPCS_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<NPC> {
            val list = mutableListOf<NPC>()
            for (i in 0..quantity) {
                list.add(generateNpc(locationSource, randomizer))
            }
            return list
        }

        private fun generateNpc(locationSource : Collection<Int>, randomizer: Random = Random.Default): NPC {
            return NPC(
                Utilities.generateNpcName(true, randomizer).take(15),
                randomizer.nextInt(
                    GenerationParameters.GEN_NPCS_MIN_EXP,
                    GenerationParameters.GEN_NPCS_MAX_EXP
                ),
                Utilities.generateRandomString(50, randomizer),
                locationSource.random(randomizer)
            )
        }
    }
}