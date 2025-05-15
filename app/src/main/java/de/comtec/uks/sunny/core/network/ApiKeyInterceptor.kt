package de.comtec.uks.sunny.core.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val url = originalUrl.newBuilder()
            .addQueryParameter("appid", apiKey)
            .build()

        val request = original.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}