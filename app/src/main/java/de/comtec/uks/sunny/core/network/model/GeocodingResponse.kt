package de.comtec.uks.sunny.core.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingResponse(
    val name: String,
    val lat: Double,
    val lon: Double
)