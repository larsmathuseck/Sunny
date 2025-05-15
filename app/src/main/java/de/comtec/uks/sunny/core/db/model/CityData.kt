package de.comtec.uks.sunny.core.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class CityData(
    @PrimaryKey val location: String,
    val isHomeLocation: Boolean = false,
    val latitude: Double,
    val longitude: Double,
)
