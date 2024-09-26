package ninja.bryansills.lunchtime.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ninja.bryansills.lunchtime.network.GooglePlacesApi
import ninja.bryansills.lunchtime.network.LocationCircle
import ninja.bryansills.lunchtime.network.LocationRestriction
import ninja.bryansills.lunchtime.network.NetworkLocation
import ninja.bryansills.lunchtime.network.TextSearch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val googlePlacesApi: GooglePlacesApi,
) : ViewModel() {
    init {
        viewModelScope.launch {
            val nearbySearch = TextSearch(
                textQuery = "The Bird",
                locationBias = LocationRestriction(
                    circle = LocationCircle(
                        center = NetworkLocation(
                            latitude = 37.7749,
                            longitude = -122.4194,
                        ),
                        radius = 1000.0
                    )
                )
            )
            val response = googlePlacesApi.getLocationsByQuery(nearbySearch)
            Log.d("BLARG", response.toString())
        }
    }

    val uiState: StateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(
            isLoading = false,
            searchResults = (0..20).map { index ->
                UiRestaurant(
                    id = index.toString(),
                    name = "Fake Restaurant",
                    coords = LatLng(
                        sanFrancisco.latitude + Random.nextDouble(-0.05, 0.05),
                        sanFrancisco.longitude + Random.nextDouble(-0.05, 0.05),
                    ),
                    rating = 5.0f,
                    numOfRatings = 1000,
                    city = "Chicago",
                    isBookmarked = false,
                )
            }
        )
    )
}

data class HomeUiState(
    val isLoading: Boolean,
    val searchResults: List<UiRestaurant>
)

data class UiRestaurant(
    val id: String,
    val name: String,
    val coords: LatLng,
    val rating: Float,
    val numOfRatings: Int,
    val city: String,
    val isBookmarked: Boolean,
)

private val sanFrancisco = LatLng(37.7749, -122.4194)
