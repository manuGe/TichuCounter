package fhnw.emoba.yelloapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDao {
    @Query("SELECT * FROM preferences WHERE preference LIKE :preference")
    fun getPreference(preference: String): Preference

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPreference(preference: Preference)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(preferences: List<Preference>)

    @Update
    fun updatePreference(preference: Preference)

    @Delete
    fun deletePreference(preference: Preference)

    @Query("SELECT COUNT(*) from preferences")
    fun count() : Int
}