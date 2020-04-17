package ru.igorlo.simulation

import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

abstract class Simulation(
    private val numberOfElements: Int,
    private val minDelay: Long,
    private val maxDelay: Long,
    private val randomizer: Random = Random.Default
) {

    var running = true
    private val elements = mutableListOf<SimulationElement>()
    private val timer = Timer()
//    val scheduler: Scheduler = Scheduler(numberOfElements)
    private var startTime = System.currentTimeMillis()

    fun runSimulation(duration: Long) {
        println("Starting simulation.")
        startTime = System.currentTimeMillis()
        timer.schedule(duration) {
            endSimulation()
        }
        initElements()
        for (elem in elements)
            elem.start()
    }

    abstract fun initElements()

    fun addSimulationElement(elem : SimulationElement){
        elements.add(elem)
    }

    private fun endSimulation() {
        running = false
        timer.cancel()
        for (elem in elements)
            elem.kill()
//        scheduler.shutdown()
        println("Simulation ended after ${(System.currentTimeMillis() - startTime) / 1000} seconds.")
    }

}