package ninja.bryansills.lunchtime.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ninja.bryansills.lunchtime.Dispatcher
import ninja.bryansills.lunchtime.LunchtimeDispatcher
import ninja.bryansills.lunchtime.location.LocationManager
import ninja.bryansills.lunchtime.network.GooglePlacesApi
import ninja.bryansills.lunchtime.network.LocationCircle
import ninja.bryansills.lunchtime.network.LocationRestriction
import ninja.bryansills.lunchtime.network.NearbySearch
import ninja.bryansills.lunchtime.network.NetworkLocation
import ninja.bryansills.lunchtime.network.NetworkPlace
import ninja.bryansills.lunchtime.network.TextSearch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val googlePlacesApi: GooglePlacesApi,
    private val locationManager: LocationManager,
    @Dispatcher(LunchtimeDispatcher.Io) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Normal(
        isLoading = true,
        searchResults = listOf()
    ))

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var nearbyInitJob: Job? = null

    fun loadNearby() {
        if (nearbyInitJob != null) return

        nearbyInitJob = viewModelScope.launch(ioDispatcher) {
            _uiState.value = try {
                _uiState.update { old ->
                    when (old) {
                        is HomeUiState.Error -> HomeUiState.Normal(isLoading = true, searchResults = listOf())
                        is HomeUiState.Normal -> old.copy(isLoading = true)
                    }
                }
                val location = locationManager.getLatestLocation()
                val nearbySearch = NearbySearch(
                    locationRestriction = location.toLocationRestriction()
                )
                val response = googlePlacesApi.getNearbyLocations(nearbySearch)
                HomeUiState.Normal(
                    isLoading = false,
                    currentLocation = location,
                    searchResults = response.places?.toUiRestaurants() ?: listOf()
                )
            } catch (ex: Exception) {
                HomeUiState.Error(
                    errorMessage = ex.message ?: "Something unknown went wrong"
                )
            }
        }
    }

    fun queryNearby(query: String) {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = try {
                _uiState.update { old ->
                    when (old) {
                        is HomeUiState.Error -> HomeUiState.Normal(isLoading = true, searchResults = listOf())
                        is HomeUiState.Normal -> old.copy(isLoading = true)
                    }
                }
                val location = locationManager.getLatestLocation()
                val nearbySearch = TextSearch(
                    textQuery = query,
                    locationBias = location.toLocationRestriction()
                )
                val response = googlePlacesApi.getLocationsByQuery(nearbySearch)
                HomeUiState.Normal(
                    isLoading = false,
                    currentLocation = location,
                    searchResults = response.places?.toUiRestaurants() ?: listOf()
                )
            } catch (ex: Exception) {
                HomeUiState.Error(
                    errorMessage = ex.message ?: "Something unknown went wrong"
                )
            }
        }
    }
}

private val sanFrancisco = LatLng(37.7749, -122.4194)

sealed interface HomeUiState {
    data class Normal(
        val isLoading: Boolean,
        val currentLocation: LatLng = sanFrancisco,
        val searchResults: List<UiRestaurant>
    ) : HomeUiState

    data class Error(
        val errorMessage: String
    ) : HomeUiState
}

data class UiRestaurant(
    val id: String,
    val name: String,
    val coords: LatLng,
    val rating: Double,
    val numOfRatings: Int,
    val city: String,
    val isBookmarked: Boolean,
    val photoUri: String?,
)

private fun LatLng.toLocationRestriction() = LocationRestriction(
    circle = LocationCircle(
        center = NetworkLocation(
            latitude = this.latitude,
            longitude = this.longitude,
        ),
        radius = 5000.0
    )
)

private fun List<NetworkPlace>.toUiRestaurants(): List<UiRestaurant> {
    return this.map { network ->
        UiRestaurant(
            id = network.id,
            name = network.displayName.text,
            coords = network.location.toLatLng(),
            rating = network.rating,
            numOfRatings = network.userRatingCount,
            city = network
                .addressComponents
                .find { component -> component.types.contains("locality") }
                ?.longText
                ?: "Unknown",
            isBookmarked = false,
            photoUri = network.photos?.firstOrNull()?.authorAttributions?.firstOrNull()?.photoUri
        )
    }
}

private fun NetworkLocation.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}
