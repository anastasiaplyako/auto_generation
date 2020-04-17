package ru.igorlo.generation.entities

import ru.igorlo.generation.GenerationParameters
import ru.igorlo.Utilities
import kotlin.random.Random

data class Clan(val name: String, val rating: Int) : DBEntity {
    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["name"] = name
        map["rating"] = rating
        return map
    }

    companion object {
        fun generateClans(
            quantity: Int = GenerationParameters.GEN_CLANS_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<Clan> {
            val list = mutableListOf<Clan>()
            for (i in 0..quantity) {
                list.add(generateClan(randomizer))
            }
            return list
        }

        private fun generateClan(randomizer: Random = Random.Default): Clan {
            return Clan(
                Utilities.generateClanName(randomizer),
                randomizer.nextInt(
                    GenerationParameters.GEN_CLANS_MIN_RATING,
                    GenerationParameters.GEN_CLANS_MAX_RATING
                )
            )
        }
    }
}