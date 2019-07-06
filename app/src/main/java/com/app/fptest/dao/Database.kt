package com.app.fptest.dao

import androidx.room.RoomDatabase
import com.app.fptest.Exclusion
import com.app.fptest.ListModel

@androidx.room.Database(entities = [ListModel::class , Exclusion::class], version = 2, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun listDao(): ListModelDao
}