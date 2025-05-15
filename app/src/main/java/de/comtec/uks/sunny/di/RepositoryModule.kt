package de.comtec.uks.sunny.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import de.comtec.uks.sunny.core.data.SettingsRepository
import de.comtec.uks.sunny.core.data.WeatherRepository
import de.comtec.uks.sunny.core.db.WeatherDatabase
import de.comtec.uks.sunny.core.network.WeatherService

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun providesRepository(
        weatherService: WeatherService, db: WeatherDatabase, settingsRepository: SettingsRepository
    ): WeatherRepository {
        return WeatherRepository(weatherService, settingsRepository, db.weatherDao(), db.cityDao())
    }

}