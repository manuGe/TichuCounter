package com.manuel.tichucounter.data


class PreferenceRepository(private val preferencesDao: PreferencesDao) {

    fun getPreference(preference: String) =
        preferencesDao.getPreference(preference)

    fun createPreference(preference: String, value: String) =
        preferencesDao.insertPreference(Preference(preference, value))

    fun setPreference(preference: String, value: Boolean) =
        preferencesDao.updatePreference(Preference(preference, value.toString()))

    fun deletePreference(preference: String) =
        preferencesDao.deletePreference(Preference(preference, true.toString())) //TODO: only String

    fun countPreferences() = preferencesDao.count()
}
