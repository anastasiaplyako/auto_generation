package ru.igorlo.generation.entities

import ru.igorlo.generation.GenerationParameters
import java.sql.Timestamp
import kotlin.collections.HashMap
import kotlin.random.Random


data class NpcFight(
    val kill_time : Timestamp,
    val fk_killer : Int,
    val fk_victim : Int,
    val playerwon : Boolean,
    val player_losthp : Int,
    val npc_losthp : Int,
    val player_gotmoney : Int,
    val fk_location : Int
) : DBEntity {
    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["kill_time"] = kill_time
        map["fk_killer"] = fk_killer
        map["fk_victim"] = fk_victim
        map["fk_location"] = fk_location
        map["playerwon"] = playerwon
        map["player_losthp"] = player_losthp
        map["npc_losthp"] = npc_losthp
        map["player_gotmoney"] = player_gotmoney
        return map
    }

    companion object {
        fun generateNpcFights(
            npcSource: Collection<Int>,
            characterSource: Collection<Int>,
            locationSource: Collection<Int>,
            quantity: Int = GenerationParameters.GEN_NPC_FIGHTS_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<NpcFight> {
            val list = mutableListOf<NpcFight>()
            for (i in 0..quantity) {
                list.add(
                    generateNpcFight(
                        npcSource,
                        characterSource,
                        locationSource,
                        randomizer
                    )
                )
            }
            return list
        }

        fun generateNpcFight(
            npcSource: Collection<Int>,
            characterSource: Collection<Int>,
            locationSource: Collection<Int>,
            randomizer: Random = Random.Default
        ): NpcFight {
            val longMax = System.currentTimeMillis()
            val longMin = System.currentTimeMillis() - 63072000000
            val longTime = randomizer.nextLong(longMin, longMax)
            return NpcFight(
                Timestamp(longTime),
                characterSource.random(randomizer),
                npcSource.random(randomizer),
                randomizer.nextBoolean(),
                randomizer.nextInt(
                    GenerationParameters.GEN_NPC_FIGHTS_PLAYER_LOSTHP_MIN,
                    GenerationParameters.GEN_NPC_FIGHTS_PLAYER_LOSTHP_MAX
                ),
                randomizer.nextInt(
                    GenerationParameters.GEN_NPC_FIGHTS_NPC_LOSTHP_MIN,
                    GenerationParameters.GEN_NPC_FIGHTS_NPC_LOSTHP_MAX
                ),
                randomizer.nextInt(
                    GenerationParameters.GEN_NPC_FIGHTS_GOTMONEY_MIN,
                    GenerationParameters.GEN_NPC_FIGHTS_GOTMONEY_MAX
                ),
                locationSource.random(randomizer)
            )
        }
    }
}