package ninja.bryansills.lunchtime.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

val GooglePlacesBaseUrl = "https://places.googleapis.com/"

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    companion object {
        @Provides
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
}