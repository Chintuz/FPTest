package com.app.fptest.dao

import android.content.Context
import androidx.room.Room
import com.app.fptest.ListModel

class DataRepository(context: Context) {

    private val DB_NAME = "list-database"

    private var database: Database

    init {
        database = Room.databaseBuilder(context, Database::class.java, DB_NAME).build()
    }

    fun insertList(list: List<ListModel>) {
        for (model in list)
            database.listDao().insertAll(model)
    }

    fun getList(): List<ListModel> {
        return database.listDao().getAll()
    }

    fun getCount(): Int {
        return database.listDao().getCount()
    }

    fun getFirst(): ListModel {
        return database.listDao().getFirst()
    }

    fun clear() {
        return database.listDao().clearTableData()
    }

    fun updateSelection(isSelected: Boolean, tag: Int) {
        database.listDao().updateSelection(isSelected, tag)
    }

    fun updateEnabled(isEnabled: Boolean, facility_id: String, object_id: String) {
        database.listDao().updateEnabled(isEnabled, facility_id, object_id)
    }

    fun resetSelection(isSelected: Boolean) {
        database.listDao().resetSelection(isSelected)
    }

    fun resetEnabled(isEnabled: Boolean) {
        database.listDao().resetEnabled(isEnabled)
    }

}