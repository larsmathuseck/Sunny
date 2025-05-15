package de.comtec.uks.sunny.core.network

import de.comtec.uks.sunny.core.network.model.CurrentWeatherResponse
import de.comtec.uks.sunny.core.network.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
    ): CurrentWeatherResponse?

    @GET("geo/1.0/direct")
    suspend fun geocodeCity(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1,
    ): List<GeocodingResponse>

}