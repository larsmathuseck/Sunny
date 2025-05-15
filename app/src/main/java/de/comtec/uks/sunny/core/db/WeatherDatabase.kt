package de.comtec.uks.sunny.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.comtec.uks.sunny.core.db.model.CityData
import de.comtec.uks.sunny.core.db.model.WeatherData

@Database(entities = [WeatherData::class, CityData::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
}