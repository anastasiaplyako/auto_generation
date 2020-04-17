package ru.igorlo

import ru.igorlo.simulation.RandomWritersSimulation

fun main(){
    val sim = RandomWritersSimulation(10, 1000, 3000)
    sim.runSimulation(10000)
}