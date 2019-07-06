package com.app.fptest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.fptest.ListModel

@Dao
abstract class ListModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(list: ListModel): Long

    @Query("SELECT * FROM listModel")
    abstract fun getAll(): List<ListModel>

    @Query("SELECT * FROM listModel ORDER BY uId ASC LIMIT 1")
    abstract fun getFirst(): ListModel

    @Query("SELECT COUNT(name) FROM listModel")
    abstract fun getCount(): Int

    @Query("DELETE FROM listModel")
    abstract fun clearTableData()

    @Query("UPDATE listModel SET isSelected = :isSelected WHERE tag =:tag")
    abstract fun updateSelection(isSelected: Boolean, tag: Int)

    @Query("UPDATE listModel SET isEnabled = :isEnabled WHERE facility_id =:fId AND object_id =:oId")
    abstract fun updateEnabled(isEnabled: Boolean, fId: String, oId: String)

    @Query("UPDATE listModel SET isSelected = :isSelected")
    abstract fun resetSelection(isSelected: Boolean)

    @Query("UPDATE listModel SET isEnabled = :isEnabled")
    abstract fun resetEnabled(isEnabled: Boolean)


}