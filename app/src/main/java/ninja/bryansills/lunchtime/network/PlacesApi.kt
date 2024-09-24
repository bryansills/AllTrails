package ninja.bryansills.lunchtime.network

import com.google.android.gms.maps.model.LatLng

interface PlacesApi {
    suspend fun getNearbyLocations(location: LatLng): List<Location>

    suspend fun getLocation(searchTerm: String): List<Location>
}

data class Location(val id: String)