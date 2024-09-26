package ninja.bryansills.lunchtime.network

import kotlinx.serialization.Serializable

@Serializable
data class NearbySearch(
    val locationRestriction: LocationRestriction,
    val includedTypes: List<String> = listOf("restaurant")
)

@Serializable
data class TextSearch(
    val textQuery: String,
    val locationBias: LocationRestriction,
    val includedType: String = "restaurant"
)

@Serializable
data class LocationRestriction(
    val circle: LocationCircle
)

@Serializable
data class LocationCircle(
    val center: NetworkLocation,
    val radius: Double,
)