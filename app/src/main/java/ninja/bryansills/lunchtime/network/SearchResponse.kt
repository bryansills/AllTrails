package ninja.bryansills.lunchtime.network

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val places: List<NetworkPlace>
)

@Serializable
data class NetworkPlace(
    val id: String,
    val displayName: NetworkDisplayName,
    val location: NetworkLocation,
    val rating: Double,
    val userRatingCount: Int,
    val addressComponents: List<NetworkAddressComponent>,
    val photos: List<NetworkPhotos>
)

@Serializable
data class NetworkDisplayName(
    val text: String,
    val languageCode: String,
)

@Serializable
data class NetworkLocation(
    val latitude: Double,
    val longitude: Double,
)

@Serializable
data class NetworkAddressComponent(
    val longText: String,
    val shortText: String,
    val types: List<String>,
    val languageCode: String
)

@Serializable
data class NetworkPhotos(
    val name: String,
    val authorAttributions: List<NetworkAuthorAttribution>
)

@Serializable
data class NetworkAuthorAttribution(
    val photoUri: String
)