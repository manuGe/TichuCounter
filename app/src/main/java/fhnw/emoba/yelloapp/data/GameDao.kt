package fhnw.emoba.yelloapp.data

import androidx.room.*

@Dao
interface GameDao {
    @Query("SELECT * FROM games WHERE id LIKE :id ")
    fun get(id: Long): Game

    @Query("SELECT * FROM games")
    fun getAll(): MutableList<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(game: Game)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<Game>)

    @Update
    fun update(game: Game)

    @Delete
    fun delete(game: Game)
}