package ru.igorlo.generation.entities

import java.sql.Timestamp
import kotlin.collections.HashMap
import kotlin.random.Random
import ru.igorlo.generation.GenerationParameters


data class PlayerFight(
    val fight_time: Timestamp,
    val fk_killer: Int,
    val fk_victim: Int,
    val fk_location: Int,
    val killer_losthp: Int,
    val victim_losthp: Int
) : DBEntity {
    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["fight_time"] = fight_time
        map["fk_killer"] = fk_killer
        map["fk_victim"] = fk_victim
        map["fk_location"] = fk_location
        map["killer_losthp"] = killer_losthp
        map["victim_losthp"] = victim_losthp
        return map
    }

    companion object {
        fun generatePlayerFights(
            characterSource: Collection<Int>,
            locationSource: Collection<Int>,
            quantity: Int = GenerationParameters.GEN_PLAYER_FIGHTS_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<PlayerFight> {
            val list = mutableListOf<PlayerFight>()
            for (i in 0..quantity) {
                list.add(
                    generatePlayerFight(
                        characterSource,
                        locationSource,
                        randomizer
                    )
                )
            }
            return list
        }

        fun generatePlayerFight(
            characterSource: Collection<Int>,
            locationSource: Collection<Int>,
            randomizer: Random = Random.Default
        ): PlayerFight {
            val longMax = System.currentTimeMillis()
            val longMin = System.currentTimeMillis() - 63072000000
            val longTime = randomizer.nextLong(longMin, longMax)
            val charsShuffled = characterSource.shuffled(randomizer)
            return PlayerFight(
                Timestamp(longTime),
                charsShuffled[0],
                charsShuffled[1],
                locationSource.random(randomizer),
                randomizer.nextInt(
                    GenerationParameters.GEN_NPC_FIGHTS_PLAYER_LOSTHP_MIN,
                    GenerationParameters.GEN_NPC_FIGHTS_PLAYER_LOSTHP_MAX
                ),
                randomizer.nextInt(
                    GenerationParameters.GEN_NPC_FIGHTS_PLAYER_LOSTHP_MIN,
                    GenerationParameters.GEN_NPC_FIGHTS_PLAYER_LOSTHP_MAX
                )
            )
        }
    }
}