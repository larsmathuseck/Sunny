package de.comtec.uks.sunny.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import de.comtec.uks.sunny.core.model.WeatherUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val UNIT_KEY = stringPreferencesKey("unit")
    }

    val unitFlow: Flow<WeatherUnit> = dataStore.data.map { WeatherUnit.fromApiName(it[UNIT_KEY]) }

    suspend fun saveUnit(weatherUnit: WeatherUnit) {
        dataStore.edit { it[UNIT_KEY] = weatherUnit.apiName }
    }
}