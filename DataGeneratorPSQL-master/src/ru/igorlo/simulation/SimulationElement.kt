package ru.igorlo.simulation

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.random.Random

abstract class SimulationElement(
    private val minDelay: Long,
    private val maxDelay: Long,
    private val randomizer: Random = Random.Default,
    private val parent: Simulation
) {

    private val timer = Timer()

    init {
        if (minDelay > maxDelay || maxDelay <= 0L || minDelay <= 0L)
            throw IllegalArgumentException("Wrong delay")
    }

    fun start(){
        val delay = randomizer.nextLong(minDelay, maxDelay)
        timer.schedule(delay) {
            withStep()
        }
    }

    fun withStep() {
        if (!parent.running)
            return
        tick()
        val delay = randomizer.nextLong(minDelay, maxDelay)
        timer.schedule(delay) {
            withStep()
        }
    }

    abstract fun tick()

    fun kill(){
        timer.cancel()
    }
}