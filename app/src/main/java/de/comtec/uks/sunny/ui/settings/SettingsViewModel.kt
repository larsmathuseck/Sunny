package de.comtec.uks.sunny.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.comtec.uks.sunny.core.data.SettingsRepository
import de.comtec.uks.sunny.core.data.WeatherRepository
import de.comtec.uks.sunny.core.model.WeatherUnit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {


    val unit = settingsRepository.unitFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WeatherUnit.METRIC
    )

    fun updateUnit(unit: WeatherUnit) {
        viewModelScope.launch {
            settingsRepository.saveUnit(unit)
            weatherRepository.invalidateWeather()
        }
    }

    override fun onCleared() {
        super.onCleared()
        weatherRepository.onCleared()
    }


}