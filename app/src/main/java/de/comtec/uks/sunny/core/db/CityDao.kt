package de.comtec.uks.sunny.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.comtec.uks.sunny.core.db.model.CityData
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCity(city: CityData)

    @Query("DELETE FROM CityData WHERE location = :name")
    suspend fun removeCity(name: String)

    @Query("SELECT * FROM CityData")
    fun getTrackedCitiesFlow(): Flow<List<CityData>>


}