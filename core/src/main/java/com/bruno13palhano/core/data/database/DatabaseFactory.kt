package com.bruno13palhano.core.data.database

import app.cash.sqldelight.ColumnAdapter
import cache.ItemTable
import com.bruno13palhano.cache.MYFarmDatabase

internal class DatabaseFactory(private val driverFactory: DriverFactory) {
    fun createDriver(): MYFarmDatabase {
        return MYFarmDatabase(
            driver = driverFactory.createDriver(),
            ItemTableAdapter = ItemTable.Adapter(successorsAdapter = listOfSuccessorsAdapter)
        )
    }
}

private val listOfSuccessorsAdapter = object : ColumnAdapter<List<Int>, String> {
    override fun decode(databaseValue: String): List<Int> {
        return if (databaseValue.isEmpty()) emptyList()
        else databaseValue.split(",").map { it.toInt() }
    }

    override fun encode(value: List<Int>) = value.joinToString(",")
}