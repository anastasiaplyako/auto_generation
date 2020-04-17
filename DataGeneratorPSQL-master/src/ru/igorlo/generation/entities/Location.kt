package ru.igorlo.generation.entities

import ru.igorlo.generation.GenerationParameters
import ru.igorlo.Utilities
import kotlin.random.Random

data class Location(val id: Int, val name: String, val x_coord: Int, val y_coord: Int) : DBEntity {
    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["id"] = id
        map["name"] = name
        map["x_coord"] = x_coord
        map["y_coord"] = y_coord
        return map
    }

    companion object {
        fun generateLocations(
            quantity: Int = GenerationParameters.GEN_LOCATIONS_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<Location> {
            val list = mutableListOf<Location>()
            for (i in 1..quantity) {
                list.add(generateLocation(i, randomizer))
            }
            return list
        }

        private fun generateLocation(id: Int, randomizer: Random = Random.Default): Location {
            return Location(
                id,
                Utilities.generateLocationName(randomizer),
                randomizer.nextInt(
                    GenerationParameters.GEN_LOCATIONS_MIN_X,
                    GenerationParameters.GEN_LOCATIONS_MAX_X
                ),
                randomizer.nextInt(
                    GenerationParameters.GEN_LOCATIONS_MIN_Y,
                    GenerationParameters.GEN_LOCATIONS_MAX_Y
                )
            )
        }
    }
}