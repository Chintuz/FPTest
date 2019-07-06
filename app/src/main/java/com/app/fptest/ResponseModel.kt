package com.app.fptest

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.Collections.emptyList


data class ResponseData(
    val exclusions: List<List<Exclusion>>,
    val facilities: List<Facility>
)

data class Facility(
    val facility_id: String,
    val name: String,
    val options: List<Option>
)

data class Option(
    val icon: String,
    val id: String,
    val name: String
)

@Entity(tableName = "exclusion")
data class Exclusion(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uId")
    var uid: Int = 0,

    @ColumnInfo(name = "facility_id")
    val facility_id: String,

    @ColumnInfo(name = "options_id")
    val options_id: String
)

@Entity(tableName = "listModel")
class ListModel : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uId")
    var uid: Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "tag")
    var tag: Int = -1

    @ColumnInfo(name = "position")
    var position: Int = -1

    @ColumnInfo(name = "icon")
    var icon: String = ""

    @ColumnInfo(name = "facility_id")
    var facility_id: String = ""

    @ColumnInfo(name = "object_id")
    var object_id: String = ""

    @ColumnInfo(name = "isSelected")
    var isSelected: Boolean = false

    @ColumnInfo(name = "isEnabled")
    var isEnabled: Boolean = true

    @ColumnInfo(name = "exclusions")
    @TypeConverters(Converters::class)
    var exclusions: List<Exclusion> = ArrayList()

    @ColumnInfo(name = "timestamp")
    var timeStamp: Long = 0

}

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun someObjectListToString(someObjects: List<Exclusion>): String {
            return Gson().toJson(someObjects)
        }

        @TypeConverter
        @JvmStatic
        fun stringToSomeObjectList(data: String?): List<Exclusion> {
            if (data == null) {
                return emptyList()
            }
            val listType = object : TypeToken<List<Exclusion>>() {}.type

            return Gson().fromJson(data, listType)
        }
    }
}

data class ErrorData(var code: Int, var message: String)