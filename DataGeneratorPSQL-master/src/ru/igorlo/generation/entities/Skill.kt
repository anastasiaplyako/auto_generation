package ru.igorlo.generation.entities

import ru.igorlo.generation.GenerationParameters
import ru.igorlo.Utilities
import kotlin.math.floor
import kotlin.random.Random

data class Skill(val name: String, val mult: Double) : DBEntity {

    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["name"] = name
        map["mult"] = mult
        return map
    }

    companion object {
        fun generateSkills(
            quantity: Int = GenerationParameters.GEN_SKILLS_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<Skill> {
            val list = mutableListOf<Skill>()
            for (i in 0..quantity) {
                list.add(generateSkill(randomizer))
            }
            return list
        }

        private fun generateSkill(randomizer: Random = Random.Default): Skill {
            return Skill(
                Utilities.generateSkillName(),
                floor(
                    randomizer.nextDouble(
                        GenerationParameters.GEN_SKILLS_MIN_MULT,
                        GenerationParameters.GEN_SKILLS_MAX_MULT
                    ) * 100
                ) / 100
            )
        }
    }

}