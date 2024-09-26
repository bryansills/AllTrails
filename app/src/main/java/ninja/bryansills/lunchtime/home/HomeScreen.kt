package ninja.bryansills.lunchtime.home

import android.content.Context
import android.os.Parcelable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ninja.bryansills.lunchtime.R
import ninja.bryansills.lunchtime.ui.theme.PaleGreen
import ninja.bryansills.lunchtime.ui.theme.StrongGreen

@Serializable
data object Home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    WithLocation(
        onPermissionAccepted = viewModel::loadNearby
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                TopBar()

                when (val state = uiState) {
                    is HomeUiState.Error -> ErrorView(
                        message = state.errorMessage,
                    )
                    is HomeUiState.Normal -> NormalView(
                        searchResults = state.searchResults,
                        currentLocation = state.currentLocation,
                        isLoading = state.isLoading,
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.ErrorView(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth().weight(1f)) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ColumnScope.NormalView(
    searchResults: List<UiRestaurant>,
    currentLocation: LatLng,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    var selectedView by rememberSaveable { mutableStateOf(HomeScreenView.List) }

    Box(modifier = modifier.fillMaxWidth().weight(1f)) {
        when (selectedView) {
            HomeScreenView.List -> HomeList(searchResults)
            HomeScreenView.Map -> MapList(searchResults, currentLocation)
        }

        BottomToggleButton(
            selectedView = selectedView,
            onClick = { selectedView = selectedView.toggle() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp) // TODO: validate
        )
    }
}

@Parcelize
enum class HomeScreenView(@DrawableRes val oppositeIcon: Int, @StringRes val oppositeTitle: Int) : Parcelable {
    List(oppositeIcon = R.drawable.icon_map, oppositeTitle = R.string.map_title),
    Map(oppositeIcon = R.drawable.icon_list, oppositeTitle = R.string.list_title),
    ;

    fun toggle(): HomeScreenView {
        return when (this) {
            List -> Map
            Map -> List
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    // TODO: get the header all the way up above the status bar
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp), // TODO: validate
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp) // TODO: validate
    ) {
        Image(
            painter = painterResource(R.drawable.header_logo),
            contentDescription = "AllTrails for lunch logo"
        )
        Box(modifier = Modifier.background(Color.Red).size(width = 250.dp, 48.dp)) // TODO: actual text
    }
}

@Composable
private fun BottomToggleButton(
    selectedView: HomeScreenView,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp), // TODO: validate
            modifier = Modifier.height(48.dp), // TODO: validate
        ) {
            Image(
                painter = painterResource(selectedView.oppositeIcon),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentDescription = stringResource(selectedView.oppositeTitle),
            )
            Text(
                text = stringResource(selectedView.oppositeTitle),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun HomeList(restaurants: List<UiRestaurant>, modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp), // TODO: validate
        verticalArrangement = Arrangement.spacedBy(24.dp), // TODO: validate
        modifier = modifier
    ) {
        items(restaurants) { item: UiRestaurant ->
            RestaurantItem(item)
        }
    }
}

@Composable
private fun MapList(
    restaurants: List<UiRestaurant>,
    currentLocation: LatLng,
    modifier: Modifier = Modifier
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val defaultCameraPosition = remember(currentLocation) { CameraPosition.fromLatLngZoom(currentLocation, 11f) }
    val cameraPositionState = rememberCameraPositionState { position = defaultCameraPosition }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        GoogleMap(
            cameraPositionState = cameraPositionState,
            onMapLoaded = { isMapLoaded = true },
        ) {
            val context = LocalContext.current
            val unselectedPin = remember { context.bitmapDescriptor(R.drawable.pin_resting) }
            val selectedPin = remember { context.bitmapDescriptor(R.drawable.pin_selected) }

            restaurants.forEachIndexed { index, restaurant ->
                MarkerInfoWindow(
                    state = rememberMarkerState(position = restaurant.coords),
                    icon = if (index == 5) selectedPin else unselectedPin,
                    onClick = { Log.d("BLARG", "Window clicked"); false },
                    onInfoWindowClose = { Log.d("BLARG", "Window close") }
                ) {
                    RestaurantItem(
                        restaurant,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier
                    .matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .wrapContentSize()
                )
            }
        }
    }
}

private fun Context.bitmapDescriptor(
    @DrawableRes id: Int,
): BitmapDescriptor? {
    val drawable = ResourcesCompat.getDrawable(this.resources, id, null)
    return drawable?.toBitmapOrNull()?.let { BitmapDescriptorFactory.fromBitmap(it) }
}

@Composable
private fun RestaurantItem(item: UiRestaurant, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp), // TODO: validate
        shadowElevation = 4.dp, // TODO: validate
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp), // TODO: validate
            horizontalArrangement = Arrangement.spacedBy(16.dp), // TODO: validate
        ) {
            if (item.photoUri != null) {
                AsyncImage(
                    model = item.photoUri,
                    placeholder = painterResource(R.drawable.placeholder_img),
                    contentDescription = "Preview image for ${item.name}",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.size(width = 64.dp, height = 72.dp),
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.placeholder_img),
                    contentDescription = "Preview image for ${item.name}",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.size(width = 64.dp, height = 72.dp),
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = StrongGreen,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )

                    val resId = if (item.isBookmarked) R.drawable.bookmark_saved else R.drawable.bookmark_resting
                    Image(
                        painter = painterResource(resId),
                        contentDescription = if (item.isBookmarked) "Bookmarked" else "Not bookmarked",
                        modifier = Modifier
                            .padding(start = 8.dp), // TODO: validate
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_star),
                        contentDescription = "Star rating",
                    )
                    Text(
                        text = "%.1f".format(Locale.current.platformLocale, item.rating),
                        style = MaterialTheme.typography.labelSmall,
                        color = StrongGreen,
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = StrongGreen,
                    )
                    Text(
                        text = "(%,d)".format(Locale.current.platformLocale, item.numOfRatings),
                        style = MaterialTheme.typography.labelSmall,
                        color = PaleGreen,
                    )
                }
                Text(
                    text = item.city,
                    style = MaterialTheme.typography.labelSmall,
                    color = PaleGreen,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun WithLocation(
    onPermissionAccepted: () -> Unit,
    content: @Composable () -> Unit
) {
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    if (locationPermissionState.status.isGranted) {
        LaunchedEffect(Unit) { onPermissionAccepted() }
        content()
    } else {
        LaunchedEffect(Unit) { locationPermissionState.launchPermissionRequest() }

        Box(modifier = Modifier.fillMaxSize().padding(32.dp)) {
            Text(
                text = stringResource(R.string.location_permission_fail),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}