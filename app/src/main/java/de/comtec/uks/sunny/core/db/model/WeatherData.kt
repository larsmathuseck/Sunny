package de.comtec.uks.sunny.core.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.comtec.uks.sunny.core.network.model.CurrentWeatherResponse

@Entity
data class WeatherData(
    @PrimaryKey val location: String,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val description: String,
    val iconUrl: String,
    val timestamp: Long,
    val sunrise: Long,
    val sunset: Long,
    val windSpeed: Double,
    val windDeg: Int,
    val cloudCoverage: Int
)

fun CurrentWeatherResponse?.toWeatherData(location: String): WeatherData? {
    if (this == null || conditions.isEmpty()) return null

    return WeatherData(
        location = location,
        temperature = metrics.temperature,
        feelsLike = metrics.feelsLike,
        humidity = metrics.humidity,
        pressure = metrics.pressure,
        description = conditions.first().description,
        iconUrl = "https://openweathermap.org/img/wn/${conditions.first().iconCode}@4x.png",
        timestamp = timestamp,
        sunrise = systemInfo.sunriseUnix,
        sunset = systemInfo.sunsetUnix,
        windSpeed = wind.speed,
        windDeg = wind.degrees,
        cloudCoverage = clouds.percentage
    )
}