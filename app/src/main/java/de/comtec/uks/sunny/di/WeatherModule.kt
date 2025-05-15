package de.comtec.uks.sunny.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import de.comtec.uks.sunny.BuildConfig
import de.comtec.uks.sunny.core.network.ApiKeyInterceptor
import de.comtec.uks.sunny.core.network.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val BASE_URL = "https://api.openweathermap.org/"

@Module
@InstallIn(ViewModelComponent::class)
object WeatherModule {

    @Provides
    fun providesRetrofitClient(): WeatherService {
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(BuildConfig.OPENWEATHER_API_KEY))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }).build()

        return Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(MoshiConverterFactory.create()).build()
            .create(WeatherService::class.java)
    }
}

