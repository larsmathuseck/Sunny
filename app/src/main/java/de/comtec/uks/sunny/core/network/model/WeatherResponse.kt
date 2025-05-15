package de.comtec.uks.sunny.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    @Json(name = "coord") val coordinates: Coordinates,
    @Json(name = "weather") val conditions: List<WeatherCondition>,
    @Json(name = "base") val baseStation: String,
    @Json(name = "main") val metrics: WeatherMetrics,
    @Json(name = "visibility") val visibilityMeters: Int,
    @Json(name = "wind") val wind: WindInfo,
    @Json(name = "clouds") val clouds: CloudCoverage,
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "sys") val systemInfo: SystemInfo,
    @Json(name = "timezone") val timezoneOffset: Int,
    @Json(name = "id") val cityId: Int,
    @Json(name = "name") val cityName: String,
    @Json(name = "cod") val statusCode: Int
)

@JsonClass(generateAdapter = true)
data class Coordinates(
    @Json(name = "lon") val longitude: Double,
    @Json(name = "lat") val latitude: Double
)

@JsonClass(generateAdapter = true)
data class WeatherCondition(
    @Json(name = "id") val conditionId: Int,
    @Json(name = "main") val group: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val iconCode: String
)

@JsonClass(generateAdapter = true)
data class WeatherMetrics(
    @Json(name = "temp") val temperature: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "sea_level") val seaLevel: Int?,
    @Json(name = "grnd_level") val groundLevel: Int?
)

@JsonClass(generateAdapter = true)
data class WindInfo(
    @Json(name = "speed") val speed: Double,
    @Json(name = "deg") val degrees: Int,
    @Json(name = "gust") val gustSpeed: Double?
)

@JsonClass(generateAdapter = true)
data class CloudCoverage(
    @Json(name = "all") val percentage: Int
)

@JsonClass(generateAdapter = true)
data class SystemInfo(
    @Json(name = "country") val countryCode: String,
    @Json(name = "sunrise") val sunriseUnix: Long,
    @Json(name = "sunset") val sunsetUnix: Long
)