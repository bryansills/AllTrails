package ninja.bryansills.lunchtime.network

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

val GooglePlacesBaseUrl = "https://places.googleapis.com/"

@ContributesTo(AppScope::class)
@BindingContainer
object NetworkModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideGooglePlacesApi(): GooglePlacesApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        val converterFactory = json.asConverterFactory(
            "application/json; charset=UTF8".toMediaType(),
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(GooglePlacesBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create()
    }
}