package de.comtec.uks.sunny.ui.home

import de.comtec.uks.sunny.core.db.model.CityData
import de.comtec.uks.sunny.core.db.model.WeatherData
import de.comtec.uks.sunny.core.model.WeatherUnit

data class HomeUiState(
    val weather: List<WeatherData> = emptyList(),
    val cities: List<CityData> = emptyList(),
    val loading: Boolean = false,
    val unit: WeatherUnit = WeatherUnit.METRIC,
)