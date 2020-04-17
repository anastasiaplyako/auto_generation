package ru.igorlo.generation.entities

import ru.igorlo.generation.GenerationParameters
import ru.igorlo.Utilities
import kotlin.random.Random

data class Item(val name: String, val damage: Int, val weight: Int, val price: Int) :
    DBEntity {

    override fun getValuesMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["name"] = name
        map["damage"] = damage
        map["weight"] = weight
        map["price"] = price
        return map
    }

    companion object {
        fun generateItems(
            quantity: Int = GenerationParameters.GEN_ITEMS_QUANTITY,
            randomizer: Random = Random.Default
        ): Collection<Item> {
            val list = mutableListOf<Item>()
            for (i in 0..quantity) {
                list.add(generateItem(randomizer))
            }
            return list
        }

        fun generateItem(randomizer: Random = Random.Default): Item {
            return Item(
                Utilities.generateItemName(randomizer),
                randomizer.nextInt(
                    GenerationParameters.GEN_ITEMS_MIN_DAMAGE,
                    GenerationParameters.GEN_ITEMS_MAX_DAMAGE
                ),
                randomizer.nextInt(
                    GenerationParameters.GEN_ITEMS_MIN_WEIGHT,
                    GenerationParameters.GEN_ITEMS_MAX_WEIGHT
                ),
                randomizer.nextInt(
                    GenerationParameters.GEN_ITEMS_MIN_PRICE,
                    GenerationParameters.GEN_ITEMS_MAX_PRICE
                )
            )
        }
    }
}