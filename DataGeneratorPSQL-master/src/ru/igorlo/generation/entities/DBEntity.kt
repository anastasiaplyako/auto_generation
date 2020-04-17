package ru.igorlo.generation.entities

interface DBEntity {

    fun getValuesMap(): Map<String, Any>

}