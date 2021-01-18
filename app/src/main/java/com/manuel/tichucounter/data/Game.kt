package com.manuel.tichucounter.data

import androidx.compose.runtime.*
import androidx.room.*
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken


@Entity(tableName = "games")
@TypeConverters(Converters::class)
data class Game(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "game_name")
    var name: String,
    @ColumnInfo(name = "game_time")
    var time: Long = id,
    @ColumnInfo(name = "game_stats")
    var stats: String = "0 - 0",
    @ColumnInfo(name = "game_state")
    var state: GameState = GameState.RUNNING, //TODO: should probably be mutable
    @ColumnInfo(name = "game_points")
    var points: MutableList<Pair<Int, Int>> = mutableStateListOf()
)

//TODO: delete
//data class Game(val name: String) {
//    val id = System.currentTimeMillis()
//    var time by mutableStateOf(id)
//    var stats = "0 - 0"
//    var state by mutableStateOf(GameState.RUNNING)
//    var points: MutableList<Pair<Int, Int>> = mutableStateListOf()
//}

enum class GameState(val text: String) {
    RUNNING("läuft noch"),
    FINISHED("fertig"),
}

class Converters {
    @TypeConverter
    fun toGameState(value: String) = enumValueOf<GameState>(value)

    @TypeConverter
    fun fromGameState(value: GameState) = value.name

    @TypeConverter
    fun toPoints(value: String): MutableList<Pair<Int, Int>> {
//        if (value == null) {
//            return mutableStateListOf()
//        }
        val listType = object : TypeToken<MutableList<Pair<Int, Int>>>() {}.type
        return Gson().fromJson(value, listType) //FIXME: does not return mutableStateListOf()
    }

    @TypeConverter
    fun fromPoints(list: MutableList<Pair<Int, Int>>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}