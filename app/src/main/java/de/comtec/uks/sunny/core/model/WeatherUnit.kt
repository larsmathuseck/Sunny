package de.comtec.uks.sunny.core.model

enum class WeatherUnit(
    val apiName: String
) {
    METRIC("metric"), IMPERIAL("imperial");

    companion object {
        fun fromApiName(name: String?): WeatherUnit {
            return entries.find { it.apiName == name } ?: METRIC
        }
    }


}
