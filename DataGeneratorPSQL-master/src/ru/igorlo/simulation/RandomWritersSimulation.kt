package ru.igorlo.simulation

import kotlin.random.Random

class RandomWritersSimulation(
    private val numberOfPlayers: Int,
    private val minDelay: Long,
    private val maxDelay: Long,
    private val randomizer: Random = Random.Default
) : Simulation(numberOfPlayers, minDelay, maxDelay, randomizer) {

    override fun initElements() {
        for (n in 1..numberOfPlayers){
            addSimulationElement(RandomWriter(minDelay, maxDelay, randomizer, n, this))
        }
    }

}