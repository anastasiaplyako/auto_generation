package ru.igorlo

import org.slf4j.LoggerFactory
import ru.igorlo.generation.GenerationParameters
import ru.igorlo.generation.GenerationApplication.conScanner
import java.lang.StringBuilder
import java.sql.ResultSet
import java.util.*
import kotlin.random.Random

object Utilities {

    private val logger = LoggerFactory.getLogger(Utilities::class.java)

    fun printResultSet(resultSet: ResultSet, valueMaxSize: Int = 10) {
        logger.info("Printing ResultSet")
        val metaData = resultSet.metaData
        println("\n".padEnd(metaData.columnCount * (valueMaxSize + 1), '-'))
        for (i in 1..metaData.columnCount) {
            System.out.print(metaData.getColumnName(i).take(valueMaxSize).padEnd(valueMaxSize))
            if (i < metaData.columnCount)
                print('|')
        }
        println("\n".padEnd(metaData.columnCount * (valueMaxSize + 1), '-'))
        while (resultSet.next()) {
            for (i in 1..metaData.columnCount) {
                if (i > 1) print("|")
                val columnValue =
                    if (resultSet.getString(i) == null)
                        "".padEnd(valueMaxSize)
                    else
                        resultSet.getString(i).take(valueMaxSize).padEnd(valueMaxSize)
                print(columnValue)
            }
            println("")
        }
        println("\n".padEnd(metaData.columnCount * (valueMaxSize + 1), '-'))
    }

    fun getUserIntParameter(text: String): Int {
        logger.info("Getting INT from user")
        var parameter: Int? = null
        while (parameter == null) {
            println("$text (int) : ")
            try {
                parameter = conScanner.nextInt()
            } catch (e: InputMismatchException) {
                println("Wrong format")
                conScanner = Scanner(System.`in`)
            }
        }
        return parameter
    }

    fun getUserBooleanParameter(text: String): Boolean {
        while (true) {
            println("$text [y/n] : ")
            val string = conScanner.next().toLowerCase()
            if (string == "y" || string == "yes")
                return true
            else if (string == "n" || string == "no")
                return false
            println("Wrong format")
        }
    }

    fun getUserStringParameter(text: String): String {
        println("$text (String) : ")
        val str = conScanner.next()
        println()
        return str
    }

    fun generateRandomString(lenght: Int, randomizer: Random = Random.Default): String {
        val sequence = sequence {
            yieldAll(generateSequence { randomizer.nextInt(GenerationParameters.RANDOM_STRING_SOURCE.length) })
        }
        return sequence
            .take(lenght)
            .map(GenerationParameters.RANDOM_STRING_SOURCE::get)
            .joinToString("")
    }

    fun generateNpcName(shortName: Boolean = false, randomizer: Random = Random.Default): String {
        val stringBuilder = StringBuilder()

        //firstname
        stringBuilder
            .append(GenerationParameters.NAMES_FIRSTNAME_FIRSTHALF.random(randomizer))
            .append(GenerationParameters.NAMES_FIRSTNAME_SECONDHALF.random(randomizer))

        if (shortName)
            return stringBuilder.toString()

        //secondname
        if (randomizer.nextDouble() < 0.6)
            stringBuilder.append(" ").append(GenerationParameters.NAMES_SECOND_NAME.random(randomizer))

        //postfix
        if (randomizer.nextDouble() < 0.6)
            stringBuilder.append(" ").append(GenerationParameters.NAMES_AFTER_NAME.random(randomizer))

        return stringBuilder.toString()
    }

    fun generateItemName(randomizer: Random = Random.Default): String {
        return GenerationParameters.GEN_ITEMS_NAMES.random(randomizer)
    }

    fun generateSkillName(randomizer: Random = Random.Default): String {
        return GenerationParameters.GEN_SKILLS_NAMES.random(randomizer)
    }

    fun generateLocationName(randomizer: Random = Random.Default): String {
        return GenerationParameters.GEN_LOCATIONS_NAMES.random(randomizer)
    }

    fun generateCityName(randomizer: Random = Random.Default): String {
        return GenerationParameters.GEN_CITIES_NAMES.random(randomizer)
    }

    fun generateClanName(randomizer: Random): String {
        return (GenerationParameters.GEN_CLANS_NAMES.random(randomizer) + "_" + generateRandomString(5)).take(20)
    }

}