package ninja.bryansills.lunchtime.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GooglePlacesApi {
    @Headers("X-Goog-FieldMask: places.id,places.displayName,places.location,places.rating,places.userRatingCount,places.addressComponents,places.photos")
    @POST("/v1/places:searchNearby")
    suspend fun getNearbyLocations(@Body nearbySearch: NearbySearch): SearchResponse

    @Headers("X-Goog-FieldMask: places.id,places.displayName,places.location,places.rating,places.userRatingCount,places.addressComponents,places.photos")
    @POST("/v1/places:searchText")
    suspend fun getLocationsByQuery(@Body textSearch: TextSearch): SearchResponse
}