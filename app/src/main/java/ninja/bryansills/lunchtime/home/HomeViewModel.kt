package ninja.bryansills.lunchtime.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val uiState: StateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(
            isLoading = false,
            currentLocation = 0f to 0f,
            selectedRestaurant = null,
            searchResults = (0..20).map { index ->
                UiRestaurant(
                    id = index.toString(),
                    name = "Fake Restaurant",
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
    val currentLocation: Pair<Float, Float>,
    val selectedRestaurant: UiRestaurant?,
    val searchResults: List<UiRestaurant>
)

data class UiRestaurant(
    val id: String,
    val name: String,
    val rating: Float,
    val numOfRatings: Int,
    val city: String,
    val isBookmarked: Boolean,
)