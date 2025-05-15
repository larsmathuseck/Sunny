package de.comtec.uks.sunny.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.comtec.uks.sunny.core.db.WeatherDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext applicationContext: Context): WeatherDatabase {
        val db = Room.databaseBuilder(
            applicationContext, WeatherDatabase::class.java, "weather-db"
        ).build()
        return db
    }

}