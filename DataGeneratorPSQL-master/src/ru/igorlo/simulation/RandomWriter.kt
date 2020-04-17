package ru.igorlo.simulation

import ru.igorlo.Utilities
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class RandomWriter(
    private val minDelay: Long,
    private val maxDelay: Long,
    private val randomizer: Random = Random.Default,
    private val id: Int = randomizer.nextInt(10000, 99999),
    private val parent: Simulation
) : SimulationElement(minDelay, maxDelay, randomizer, parent) {

    override fun tick() {
        println("Writer $id says: ${Utilities.generateRandomString(randomizer.nextInt(1, 20), randomizer)}")
    }

}