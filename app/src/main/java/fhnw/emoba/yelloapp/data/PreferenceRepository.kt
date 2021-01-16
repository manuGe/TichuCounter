package fhnw.emoba.yelloapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class PreferenceRepository(private val preferencesDao: PreferencesDao) {

//    var enableDarkMode by mutableStateOf(false)
//    var gameList: MutableList<Game> = mutableListOf()

    fun getPreference(preference: String) =
        preferencesDao.getPreference(preference)

    fun createPreference(preference: String, value: String) =
        preferencesDao.insertPreference(Preference(preference, value))

    fun setPreference(preference: String, value: Boolean) =
        preferencesDao.updatePreference(Preference(preference, value.toString()))

    fun deletePreference(preference: String) =
        preferencesDao.deletePreference(Preference(preference, true.toString())) //TODO: only String
}
