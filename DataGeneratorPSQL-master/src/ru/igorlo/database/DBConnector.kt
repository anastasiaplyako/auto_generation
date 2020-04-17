package ru.igorlo.database

import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import ru.igorlo.database.DBParameters.DB_HOST_DEFAULT
import ru.igorlo.database.DBParameters.DB_NAME_DEFAULT
import ru.igorlo.database.DBParameters.DB_PASS_DEFAULT
import ru.igorlo.database.DBParameters.DB_PORT_DEFAULT
import ru.igorlo.database.DBParameters.DB_USER_DEFAULT
import ru.igorlo.generation.entities.DBEntity
import java.sql.*
import kotlin.system.exitProcess

class DBConnector {
    private val logger = LoggerFactory.getLogger(DBConnector::class.java)
    private var connection: Connection? = null

    fun newConnection(
        db_name: String = DB_NAME_DEFAULT,
        db_user: String = DB_USER_DEFAULT,
        db_pass: String = DB_PASS_DEFAULT,
        host: String = DB_HOST_DEFAULT,
        port: Int = DB_PORT_DEFAULT
    ) {
        logger.info("Creating new connection to $db_name via $db_user user")
        if (connection != null) {
            try {
                logger.info("Closing old connection if present")
                connection?.close()
            } catch (e: PSQLException) {
                logger.warn("Couldn't carefully close old DB connection, but didn't crash")
            }
        } else {
            logger.info("Old connection was NULL, not even trying to close it...")
        }
        connection = DriverManager.getConnection("jdbc:postgresql://$host:$port/$db_name", db_user, db_pass)
    }

    fun getResultSetOfSelect(
        tableName: String,
        limit: Int = 0,
        orderBy: String = "",
        vararg fields: String
    ): ResultSet {
        logger.info("Executing SELECT from table $tableName by fields")
        if (connection == null) {
            logger.error("Cannot execute SELECT, because there's no connection to DB")
            exitProcess(1)
        }
        logger.info("Creating statement")
        val statement = connection!!.createStatement()
        try {
            logger.info("Executing query")
            return statement.executeQuery(
                StatementBuilder.selectFieldsFromTable(
                    tableName,
                    limit,
                    orderBy,
                    *fields
                )
            )
        } catch (e: PSQLException) {
            logger.error("Error while executing SELECT!")
            e.printStackTrace()
            exitProcess(1)
        }
    }

    fun insertDataInTable(tableName: String, data: Collection<DBEntity>) {
        logger.info("Trying to insert data in table")
        val listOfFields = data.elementAt(0).getValuesMap().keys
        val statement = StatementBuilder.insertColumnsInTable(tableName, listOfFields)
        val preparedStatement: PreparedStatement
        try {
            logger.info("Preparing statement")
            preparedStatement = connection!!.prepareStatement(statement)
            logger.info("Prepared successfully!")
        } catch (e: PSQLException) {
            logger.error("Oops! Could not prepare statement - something wrong with connection!")
            e.printStackTrace()
            exitProcess(1)
        }

        var count: Int
        logger.info("Adding data to statement...")
        for (element in data) {
            count = 1
            for (entry in element.getValuesMap().entries) {
                when (entry.value) {
                    is String -> preparedStatement.setString(count, entry.value as String)
                    is Int -> preparedStatement.setInt(count, entry.value as Int)
                    is Boolean -> preparedStatement.setBoolean(count, entry.value as Boolean)
                    is Timestamp -> preparedStatement.setTimestamp(count, entry.value as Timestamp)
                    is Double -> preparedStatement.setDouble(count, entry.value as Double)
                }
                count++
            }
            preparedStatement.addBatch()
        }
        logger.info("Successfully added " + data.size + " elements to statement")
        try {
            logger.info("Executing prepared statement")
            preparedStatement.executeBatch()
            logger.info("Success!")
        } catch (e: PSQLException) {
            logger.error("Oops! Could not execute statement! Something went wrong!")
            e.printStackTrace()
            exitProcess(1)
        }
    }

    fun getListOfIds(tableName: String): Collection<Int> {
        logger.info("Getting IDs of table $tableName")
        val resultSet = getResultSetOfSelect(tableName, 0, "id")
        val list = mutableListOf<Int>()
        while (resultSet.next()) {
            list.add(resultSet.getInt("id"))
        }
        logger.info("Getting IDs - Success")
        return list
    }

    fun cleanTable(tableName: String, isCascade: Boolean = false) {
        logger.info("Cleaning table $tableName")
        try {
            val statement = connection!!.createStatement()
//            val statementText = StatementBuilder.deleteAll(tableName)
            val statementText = StatementBuilder.truncateTable(tableName, isCascade)
            statement.executeUpdate(statementText)
        } catch (e: PSQLException) {
            logger.error("Oops! Cannot clean table $tableName! Something went wrong!")
            e.printStackTrace()
            exitProcess(1)
        }
        logger.info("Cleaned table successfully!")
    }

    fun setFieldToRandomId(tableName: String, tableFrom: String, fieldFk: String, whereCondition: String = "") {
        logger.info("Setting field $fieldFk in table $tableName to random id from $tableFrom")
        try {
            val statement = connection!!.createStatement()
            val statementText =
                StatementBuilder.setFieldToRandomId(tableName, tableFrom, fieldFk, whereCondition)
            statement.executeUpdate(statementText)
        } catch (e: PSQLException) {
            logger.error("Oops! Cannot do that! Something went wrong!")
            e.printStackTrace()
            exitProcess(1)
        }
    }

    fun addLinks(
        table: String,
        fieldFirst: String,
        fieldSecond: String,
        tableFromFirst: String,
        tableFromSecond: String,
        quantity: Int
    ) {
        logger.info("Adding linking rows for $tableFromFirst and $tableFromSecond in table $table")
        try {
            val statementText = StatementBuilder.addLinks(
                table,
                fieldFirst,
                fieldSecond,
                tableFromFirst,
                tableFromSecond,
                quantity
            )
            val statement = connection!!.createStatement()
            statement.executeUpdate(statementText)
        } catch (e: PSQLException) {
            logger.error("Oops! Cannot do that! Something went wrong!")
            e.printStackTrace()
            exitProcess(1)
        }
        logger.info("Success!")
    }

}