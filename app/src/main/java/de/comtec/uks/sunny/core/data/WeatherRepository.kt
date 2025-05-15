package de.comtec.uks.sunny.core.data

import de.comtec.uks.sunny.core.db.CityDao
import de.comtec.uks.sunny.core.db.WeatherDao
import de.comtec.uks.sunny.core.db.model.CityData
import de.comtec.uks.sunny.core.db.model.WeatherData
import de.comtec.uks.sunny.core.db.model.toWeatherData
import de.comtec.uks.sunny.core.model.WeatherUnit
import de.comtec.uks.sunny.core.network.WeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber

// We don't accept weather data older than 30 minutes.
private const val STALE_WEATHER_THRESHOLD_SECONDS = 30 * 60

class WeatherRepository(
    private val weatherService: WeatherService,
    private val settingsRepository: SettingsRepository,
    private val weatherDao: WeatherDao,
    private val cityDao: CityDao
) {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherFlow: Flow<List<WeatherData>> =
        cityDao.getTrackedCitiesFlow().distinctUntilChanged().flatMapLatest { trackedCity ->
            if (trackedCity.isEmpty()) {
                flowOf(emptyList())
            } else {
                val oneHourAgo =
                    (System.currentTimeMillis() / 1000) - STALE_WEATHER_THRESHOLD_SECONDS
                weatherDao.getWeatherForCitiesFlow(trackedCity.map { it.location }, oneHourAgo)
            }
        }.shareIn(
            scope = repositoryScope, started = SharingStarted.WhileSubscribed(5000L), replay = 1
        )

    val cityFlow: Flow<List<CityData>> =
        cityDao.getTrackedCitiesFlow().distinctUntilChanged().shareIn(
            scope = repositoryScope, started = SharingStarted.WhileSubscribed(5000L), replay = 1
        )

    init {
        repositoryScope.launch {
            cityDao.getTrackedCitiesFlow().distinctUntilChanged().collect { cities ->
                // Clean up: Remove cities from weather that are no longer tracked
                val allPersistedCities = weatherDao.getAllPersistedCityNames()
                val citiesToDelete =
                    allPersistedCities.filterNot { it in cities.map { it.location } }
                if (citiesToDelete.isNotEmpty()) {
                    weatherDao.deleteWeatherForUntrackedCities(citiesToDelete)
                }
                // Fetch weather for the current list of tracked cities.
                refreshWeather(
                    cities = cities
                )
            }
        }
        repositoryScope.launch {
            while (true) {
                delay(30_000)
                refreshWeather()
            }
        }
    }

    fun onCleared() {
        repositoryScope.cancel()
    }


    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            val currentCities = cityDao.getTrackedCitiesFlow().firstOrNull() ?: emptyList()
            if (currentCities.isNotEmpty()) {
                refreshWeather(
                    cities = currentCities
                )
            }
        }
    }

    // TODO: This could arguably be in a CityRepository
    suspend fun upsertLocation(city: CityData) {
        withContext(Dispatchers.IO) {
            cityDao.upsertCity(city)
            refreshWeather()
        }
    }

    // TODO: This could arguably be in a CityRepository
    suspend fun removeLocation(cityName: String) {
        withContext(Dispatchers.IO) {
            cityDao.removeCity(cityName)
            refreshWeather()
        }
    }

    suspend fun invalidateWeather() {
        withContext(Dispatchers.IO) {
            weatherDao.deleteAllWeatherData()
        }
    }

    suspend fun fetchLatLon(cityName: String): Pair<Double, Double>? {
        return try {
            val result = weatherService.geocodeCity(cityName)
            result.firstOrNull()?.let { it.lat to it.lon }
        } catch (e: Exception) {
            Timber.e("Error fetching lat/lon for $cityName: ${e.message}")
            null
        }
    }

    private suspend fun refreshWeather(
        cities: List<CityData>
    ) {
        if (cities.isEmpty()) {
            _isLoading.value = false
            return
        }
        _isLoading.value = true
        val unit = settingsRepository.unitFlow.firstOrNull() ?: WeatherUnit.METRIC
        try {
            supervisorScope {
                var newWeatherData = cities.map { city ->
                    async {
                        try {
                            Pair(
                                city, weatherService.getWeather(
                                    city.latitude, city.longitude, unit.apiName
                                )
                            )
                        } catch (e: Exception) {
                            Timber.e("Error fetching weather for ${city.location}: ${e.message}")
                            null
                        }
                    }
                }.awaitAll().filterNotNull().toMutableSet()

                if (newWeatherData.isNotEmpty()) {
                    val weatherDataList = newWeatherData.mapNotNull {
                        it.second.toWeatherData(
                            it.first.location
                        )
                    }
                    weatherDao.upsertAllWeather(weatherDataList)
                }
            }
        } catch (e: Exception) {
            Timber.e("Error during weather refresh: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }


}