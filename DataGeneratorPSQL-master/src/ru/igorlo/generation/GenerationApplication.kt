package ru.igorlo.generation

import org.graphstream.graph.Edge
import org.graphstream.graph.Node
import ru.igorlo.generation.entities.*
import ru.igorlo.Utilities
import ru.igorlo.Utilities.getUserIntParameter
import ru.igorlo.Utilities.printResultSet
import ru.igorlo.database.DBConnector
import ru.igorlo.visualisation.GraphWorks
import java.util.*
import kotlin.collections.HashMap

object GenerationApplication {

    var conScanner = Scanner(System.`in`)
    private val connector: DBConnector = DBConnector()

    fun main() {
        println(GenerationParameters.TEXT_INTRO)

        val customHost: String
        val customPort: Int
        val customDBName: String
        val customUser: String
        val customPass: String

//        val shouldCleanUp = Utilities.getUserBooleanParameter("You want to clean up present data?")
        val shouldCleanUp = true
        val proceed = Utilities.getUserBooleanParameter("Application will clean up current data. Proceed?")
        if (!proceed)
            return

        val mapType =
            getUserIntParameter(
                "Map generation type\n" +
                        "(1 (or any other) - Random\n" +
                        "2 - Lobster\n" +
                        "3 - Barabasi-Albert\n" +
                        "4 - Chvatal\n" +
                        "5 - Dorogovtsev-Mendes\n" +
                        "6 - Flower Snark\n" +
                        "7 - Grid\n" +
                        "8 - Incomplete Grid\n" +
                        "9 - Petersen graph\n" +
                        "10 - Watts-Strogatz" +
                        ")"
            )

        val customConnectionParams =
            Utilities.getUserBooleanParameter("Want to customize connection parameters? (No for default)")
        if (customConnectionParams) {
            customHost = Utilities.getUserStringParameter("DB host?")
            customPort = getUserIntParameter("DB port?")
            customDBName = Utilities.getUserStringParameter("DB name?")
            customUser = Utilities.getUserStringParameter("Username?")
            customPass = Utilities.getUserStringParameter("Password?")
            connector.newConnection(customDBName, customUser, customPass, customHost, customPort)
        } else {
            connector.newConnection()
        }

        val isCustomParameters =
            Utilities.getUserBooleanParameter("You want to set generation parameters? (No for default)")
        if (isCustomParameters) {
            GenerationParameters.GEN_ITEMS_QUANTITY = getUserIntParameter("Items quantity?")
            GenerationParameters.GEN_SKILLS_QUANTITY = getUserIntParameter("Skills quantity?")
            GenerationParameters.GEN_LOCATIONS_QUANTITY = getUserIntParameter("Locations quantity?")
            GenerationParameters.GEN_CITIES_QUANTITY = getUserIntParameter("Cities quantity?")
            GenerationParameters.GEN_CLANS_QUANTITY = getUserIntParameter("Clans quantity?")
            GenerationParameters.GEN_NPCS_QUANTITY = getUserIntParameter("Npcs quantity?")
            GenerationParameters.GEN_CONNECTIONS_QUANTITY = getUserIntParameter("Map connections quantity?")
            GenerationParameters.GEN_CHARACTERS_QUANTITY = getUserIntParameter("Characters quantity?")
            GenerationParameters.GEN_NPC_FIGHTS_QUANTITY = getUserIntParameter("Npc fights quantity?")
            GenerationParameters.GEN_PLAYER_FIGHTS_QUANTITY = getUserIntParameter("Player fights quantity?")
            GenerationParameters.GEN_ITEMS_PLAYERS = getUserIntParameter("Items in inventories? (chars)")
            GenerationParameters.GEN_ITEMS_NPC = getUserIntParameter("Items in inventories? (npc loot)")
            GenerationParameters.GEN_SKILLS_PLAYERS = getUserIntParameter("Skills for characters?")
            GenerationParameters.GEN_SKILLS_NPC = getUserIntParameter("Skills for NPCs?")
        }

        val limit = getUserIntParameter("Limit display for tables? (0 for all data)")

        val startTime = System.currentTimeMillis()

        if (shouldCleanUp) {
            connector.cleanTable("items", true)
            connector.cleanTable("npcs", true)
            connector.cleanTable("skills", true)
            connector.cleanTable("locations", true)
            connector.cleanTable("cities", true)
            connector.cleanTable("clans", true)
            connector.cleanTable("connections", true)
            connector.cleanTable("characters", true)
            connector.cleanTable("link_char_item", true)
            connector.cleanTable("link_npc_item", true)
            connector.cleanTable("link_char_skill", true)
            connector.cleanTable("link_npc_skill", true)
            connector.cleanTable("npc_fights", true)
            connector.cleanTable("player_fight", true)
        }

        println("Generating items")
        connector.insertDataInTable("items", Item.generateItems())
        printResultSet(connector.getResultSetOfSelect("items", limit))
        println("-".repeat(20))

        println("Generating skills")
        connector.insertDataInTable("skills", Skill.generateSkills())
        printResultSet(connector.getResultSetOfSelect("skills", limit))
        println("-".repeat(20))

        when (mapType) {
            1 -> generateMapRandom(limit)
            2, 3, 4, 5, 6, 7, 8, 9, 10 -> generateCustomMap(limit, mapType)
            else -> generateMapRandom(limit)
        }


        println("Generating cities")
        connector.insertDataInTable("cities", City.generateCities())
        printResultSet(connector.getResultSetOfSelect("cities", limit))
        println("-".repeat(20))

        println("Generating clans")
        connector.insertDataInTable("clans", Clan.generateClans())
        printResultSet(connector.getResultSetOfSelect("clans", limit), 20)
        println("-".repeat(20))

        println("Generating npcs")
        connector.insertDataInTable(
            "npcs", NPC.generateNpcs(
                connector.getListOfIds("locations")
            )
        )
        printResultSet(connector.getResultSetOfSelect("npcs", limit), 20)
        println("-".repeat(20))

        println("Generating characters")
        connector.insertDataInTable(
            "characters", PlayerCharacter.generateCharacters(
                connector.getListOfIds("clans"),
                connector.getListOfIds("npcs"),
                connector.getListOfIds("locations")
            )
        )
        printResultSet(connector.getResultSetOfSelect("characters", limit), 20)
        println("-".repeat(20))

        println("Randomizing foreign keys")
        connector.setFieldToRandomId("locations", "cities", "fk_city", "locations.name != cities.name")
        connector.setFieldToRandomId("cities", "characters", "fk_mayor", "cities.name != characters.name")
        connector.setFieldToRandomId("cities", "clans", "fk_leading_clan", "cities.name != clans.name")
        connector.setFieldToRandomId("characters", "npcs", "fk_worst_enemy", "characters.name != npcs.name")
        connector.setFieldToRandomId("characters", "locations", "fk_location", "characters.name != locations.name")
        connector.setFieldToRandomId("characters", "clans", "fk_clan", "characters.name != clans.name")
        connector.setFieldToRandomId("clans", "characters", "fk_leader", "fk_clan = clans.id")

        println("Generating link tables")
        connector.addLinks("link_char_item", "fk_char", "fk_item", "characters", "items", GenerationParameters.GEN_ITEMS_PLAYERS)
        printResultSet(connector.getResultSetOfSelect("link_char_item", limit))
        connector.addLinks("link_npc_item", "fk_npc", "fk_item", "npcs", "items", GenerationParameters.GEN_ITEMS_NPC)
        printResultSet(connector.getResultSetOfSelect("link_npc_item", limit))
        connector.addLinks("link_char_skill", "fk_char", "fk_skill", "characters", "skills", GenerationParameters.GEN_SKILLS_PLAYERS)
        printResultSet(connector.getResultSetOfSelect("link_char_skill", limit))
        connector.addLinks("link_npc_skill", "fk_npc", "fk_skill", "npcs", "skills", GenerationParameters.GEN_SKILLS_NPC)
        printResultSet(connector.getResultSetOfSelect("link_npc_skill", limit))
        println("-".repeat(20))

        println("Generating fights with NPCs")
        connector.insertDataInTable(
            "npc_fights", NpcFight.generateNpcFights(
                connector.getListOfIds("npcs"),
                connector.getListOfIds("characters"),
                connector.getListOfIds("locations")
            )
        )
        printResultSet(connector.getResultSetOfSelect("npc_fights", limit, orderBy = "kill_time"), 20)
        println("-".repeat(20))

        println("Generating fights between players")
        connector.insertDataInTable(
            "player_fight", PlayerFight.generatePlayerFights(
                connector.getListOfIds("characters"),
                connector.getListOfIds("locations")
            )
        )
        printResultSet(connector.getResultSetOfSelect("player_fight", limit, orderBy = "fight_time"), 20)
        println("-".repeat(20))

        val timeSeconds: Double = (System.currentTimeMillis() - startTime).toDouble() / 1000

        println("generation took: $timeSeconds seconds")
        println("generation ended successfully!")
        println("~~~Congratulations!~~~\n\n")

    }

    private fun generateCustomMap(limit: Int = 0, mapType: Int) {

        val graph = when (mapType){
            2 -> GraphWorks.generateLobsterMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            3 -> GraphWorks.generateBarabasiAlbertMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            4 -> GraphWorks.generateChvatalMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            5 -> GraphWorks.generateDorogovtsevMendesMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            6 -> GraphWorks.generateFlowerSnarkMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            7 -> GraphWorks.generateGridMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            8 -> GraphWorks.generateIncompleteGridMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            9 -> GraphWorks.generatePetersenMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            10 -> GraphWorks.generateWattsStrogatzMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
            else -> GraphWorks.generateLobsterMap(GenerationParameters.GEN_LOCATIONS_QUANTITY)
        }

        val locationMap = HashMap<Node, Int>()
        val locationList = mutableListOf<Location>()
        val connectionList = mutableListOf<MapConnection>()

        var idCounter = 1
        for (node in graph.getNodeSet<Node>()) {
            locationMap[node] = idCounter
            val location = Location(
                idCounter,
                Utilities.generateLocationName(),
                1,
                1
            )
//            print("$idCounter - ${location.id}, ${location.name}")
            locationList.add(location)
            idCounter++
        }
        for (edge in graph.getEdgeSet<Edge>()) {
            val firstNode = edge.getSourceNode<Node>()
            val secondNode = edge.getTargetNode<Node>()
            connectionList.add(
                MapConnection(
                    locationMap[firstNode]!!,
                    locationMap[secondNode]!!
                )
            )
        }

        println("Generating locations")
        connector.insertDataInTable(
            "locations",
            locationList
        )
        printResultSet(connector.getResultSetOfSelect("locations", limit), 20)
        println("-".repeat(20))

        println("Waiting to make sure, that locations are ok")
        Thread.sleep(1000)

        println("Generating connections")
        connector.insertDataInTable(
            "connections",
            connectionList
        )
        printResultSet(connector.getResultSetOfSelect("connections", limit), 20)
        println("-".repeat(20))
    }

    private fun generateMapRandom(limit: Int = 0) {
        println("Generating locations")
        connector.insertDataInTable("locations", Location.generateLocations())
        printResultSet(connector.getResultSetOfSelect("locations", limit))
        println("-".repeat(20))

        println("Generating connections")
        connector.insertDataInTable(
            "connections",
            MapConnection.generateConnections(
                connector.getListOfIds("locations")
            )
        )
        printResultSet(connector.getResultSetOfSelect("connections", limit), 20)
        println("-".repeat(20))
    }

}
