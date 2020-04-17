package ru.igorlo.generation.entities

import ru.igorlo.generation.GenerationParameters
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

data class MapConnection(val fk_from_location : Int, val fk_to_location : Int) :
    DBEntity {
    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["fk_from_location"] = fk_from_location
        map["fk_to_location"] = fk_to_location
        return map
    }

    companion object {
        fun generateConnections(
            source: Collection<Int>,
            quantity: Int = GenerationParameters.GEN_CONNECTIONS_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<MapConnection> {
            if (source.size < 2)
                return Collections.emptyList()
            val list = mutableListOf<MapConnection>()
            for (i in 0..quantity) {
                val connection =
                    generateConnection(source, randomizer)
                if (!list.contains(connection))
                    list.add(connection)
            }
            return list
        }

        private fun generateConnection(source : Collection<Int>, randomizer: Random = Random.Default): MapConnection {
            val shuffled = source.shuffled(randomizer)
            return MapConnection(
                shuffled[0],
                shuffled[1]
            )
        }
    }
}