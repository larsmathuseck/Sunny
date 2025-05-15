package de.comtec.uks.sunny.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.comtec.uks.sunny.core.db.model.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllWeather(weatherDataList: List<WeatherData>)

    @Query(
        """
    SELECT * FROM weatherdata 
    WHERE location IN (:cityNames) 
    AND timestamp >= :minTimestamp
    """
    )
    fun getWeatherForCitiesFlow(
        cityNames: List<String>, minTimestamp: Long
    ): Flow<List<WeatherData>>

    @Query("SELECT DISTINCT location FROM weatherdata")
    suspend fun getAllPersistedCityNames(): List<String>

    @Query("DELETE FROM weatherdata WHERE location IN (:cityNames)")
    suspend fun deleteWeatherForUntrackedCities(cityNames: List<String>)

    @Query("DELETE FROM weatherdata")
    suspend fun deleteAllWeatherData()


}