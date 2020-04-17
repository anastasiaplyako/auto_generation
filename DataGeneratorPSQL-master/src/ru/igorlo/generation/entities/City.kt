package ru.igorlo.generation.entities

import ru.igorlo.generation.GenerationParameters
import ru.igorlo.Utilities
import kotlin.random.Random

data class City(val name : String) : DBEntity {
    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["name"] = name
        return map
    }

    companion object {
        fun generateCities(
            quantity: Int = GenerationParameters.GEN_CITIES_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<City> {
            val list = mutableListOf<City>()
            for (i in 0..quantity) {
                val city = generateCity(randomizer)
                if (!list.contains(city))
                    list.add(city)
            }
            return list
        }

        private fun generateCity(randomizer: Random = Random.Default): City {
            return City(
                (Utilities.generateCityName(randomizer) + "_" + Utilities.generateRandomString(20, randomizer)).take(20)
            )
        }
    }
}