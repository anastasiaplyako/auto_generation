package ru.igorlo.visualisation

import org.graphstream.algorithm.generator.*
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph
import kotlin.math.sqrt

object GraphWorks {

    fun generateLobsterMap(nodesQ : Int) : Graph {
        val graph = SingleGraph("GameMap")
        val generator = LobsterGenerator(7)
        generator.addSink(graph)
        generator.begin()
        for (i in 1..nodesQ){
            generator.nextEvents()
        }
        return graph
    }

    fun generateBarabasiAlbertMap(nodesQ : Int): Graph {
        val graph = SingleGraph("GameMap")
        val generator = BarabasiAlbertGenerator(5)
        generator.addSink(graph)
        generator.begin()
        for (i in 1..nodesQ){
            generator.nextEvents()
        }
        return graph
    }

    fun generateChvatalMap(nodesQ : Int): Graph {
        val graph = SingleGraph("GameMap")
        val generator = ChvatalGenerator()
        generator.addSink(graph)
        generator.begin()
        for (i in 1..nodesQ){
            generator.nextEvents()
        }
        return graph
    }

    fun generateDorogovtsevMendesMap(nodesQ : Int): Graph {
        val graph = SingleGraph("GameMap")
        val generator = DorogovtsevMendesGenerator()
        generator.addSink(graph)
        generator.begin()
        for (i in 1..nodesQ){
            generator.nextEvents()
        }
        return graph
    }

    fun generateFlowerSnarkMap(nodesQ : Int): Graph {
        val graph = SingleGraph("GameMap")
        val generator = FlowerSnarkGenerator()
        generator.addSink(graph)
        generator.begin()
        for (i in 1..nodesQ){
            generator.nextEvents()
        }
        return graph
    }

    fun generateGridMap(nodesQ : Int): Graph {
        val graph = SingleGraph("GameMap")
        val generator = GridGenerator(false, true)
        generator.addSink(graph)
        generator.begin()
        for (i in 1..sqrt(nodesQ.toDouble()).toInt()){
            generator.nextEvents()
        }
        return graph
    }

    fun generateIncompleteGridMap(nodesQ : Int): Graph {
        val graph = SingleGraph("GameMap")
        val generator = IncompleteGridGenerator(false, 0.65f, 3, 1)
        generator.addSink(graph)
        generator.begin()
        for (i in 1..sqrt(nodesQ.toDouble()).toInt()){
            generator.nextEvents()
        }
        return graph
    }

    fun generatePetersenMap(nodesQ : Int): Graph {
        val graph = SingleGraph("GameMap")
        val generator = PetersenGraphGenerator()
        generator.addSink(graph)
        generator.begin()
        for (i in 1..nodesQ){
            generator.nextEvents()
        }
        return graph
    }

    fun generateWattsStrogatzMap(nodesQ : Int): Graph {
        val graph = SingleGraph("GameMap")
        val generator =  WattsStrogatzGenerator(nodesQ, Math.sqrt(nodesQ.toDouble()).toInt(), 0.5)
        generator.addSink(graph)
        generator.begin()
        for (i in 1..nodesQ){
            generator.nextEvents()
        }
        return graph
    }

}