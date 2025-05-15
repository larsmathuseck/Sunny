package de.comtec.uks.sunny.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.comtec.uks.sunny.core.data.LocationDataSource
import de.comtec.uks.sunny.core.data.SettingsRepository
import de.comtec.uks.sunny.core.data.WeatherRepository
import de.comtec.uks.sunny.core.db.model.CityData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository,
    private val locationDataSource: LocationDataSource
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                weatherRepository.weatherFlow,
                weatherRepository.isLoading,
                weatherRepository.cityFlow,
                settingsRepository.unitFlow,
            ) { weather, loading, cities, unit ->
                HomeUiState(weather = weather, loading = loading, cities = cities, unit = unit)
            }.collect { state ->
                _uiState.value = state
            }
        }
        viewModelScope.launch {
            locationDataSource.locationFlow().collect { location ->
                if (location == null) {
                    return@collect
                }
                updateHomeLocation(Pair(location.latitude, location.longitude))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        weatherRepository.onCleared()
    }

    private fun updateHomeLocation(latLng: Pair<Double, Double>) {
        val cityData = CityData("Current Location", true, latLng.first, latLng.second)
        viewModelScope.launch {
            weatherRepository.upsertLocation(cityData)
        }
    }

    fun addLocation(locationName: String) {
        viewModelScope.launch {
            val latLng = weatherRepository.fetchLatLon(locationName)
            if (latLng == null) {
                _errorFlow.emit("Error fetching location coordinates for $locationName")
                Timber.e("Error fetching lat/lon for $locationName")
                return@launch
            }
            val cityData = CityData(locationName, false, latLng.first, latLng.second)
            weatherRepository.upsertLocation(cityData)
        }
    }

    fun removeLocation(locationName: String) {
        viewModelScope.launch {
            weatherRepository.removeLocation(locationName)
        }
    }

    fun fetchWeather() {
        viewModelScope.launch {
            locationDataSource.locationFlow().firstOrNull()?.let { location ->
                updateHomeLocation(Pair(location.latitude, location.longitude))
            }
            weatherRepository.refreshWeather()
        }
    }

}