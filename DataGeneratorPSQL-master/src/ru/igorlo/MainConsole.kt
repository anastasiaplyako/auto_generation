package ru.igorlo

import ru.igorlo.generation.GenerationApplication
import ru.igorlo.visualisation.GraphVisualiser
import java.util.*
import kotlin.system.exitProcess

fun main() {

    val scanner = Scanner(System.`in`)
    while (true) {
        println("Input a command: ")
        val text = scanner.next()
        when (text.toLowerCase()) {
            "generate" -> GenerationApplication.main()
            "graph" -> GraphVisualiser.main()
            "exit" -> exitProcess(0)
            "help" -> printHelp()
            "echo" -> println(scanner.next())
            "hello" -> println("well... Hello")
        }
    }
}

fun printHelp() {
    println(
        "Avalable commands:\n" +
                "graph\n" +
                "generate\n" +
                "exit\n"
    )
}
