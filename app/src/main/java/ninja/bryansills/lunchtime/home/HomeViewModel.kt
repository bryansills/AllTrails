package ninja.bryansills.lunchtime.home

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
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
