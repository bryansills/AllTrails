package ninja.bryansills.lunchtime.network

import ninja.bryansills.lunchtime.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("X-Goog-Api-Key", BuildConfig.GooglePlacesApi)
            .build()
        return chain.proceed(request)
    }
}