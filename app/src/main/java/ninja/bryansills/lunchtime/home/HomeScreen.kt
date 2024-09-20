package ninja.bryansills.lunchtime.home

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ninja.bryansills.lunchtime.R

@Serializable
data object Home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    WithLocation {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                TopBar()

                val selectedView by rememberSaveable { mutableStateOf(HomeScreenView.List) }

                Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    BottomToggleButton(
                        selectedView = selectedView,
                        onClick = {},
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 48.dp) // TODO: validate
                    )
                }
            }
        }
    }
}

@Parcelize
enum class HomeScreenView(@DrawableRes val oppositeIcon: Int, @StringRes val oppositeTitle: Int) : Parcelable {
    List(oppositeIcon = R.drawable.icon_map, oppositeTitle = R.string.map_title),
    Map(oppositeIcon = R.drawable.icon_list, oppositeTitle = R.string.list_title),
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
        Box(modifier = Modifier.background(Color.Red).size(width = 250.dp, 48.dp))
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
            Text(text = stringResource(selectedView.oppositeTitle))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun WithLocation(content: @Composable () -> Unit) {
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    if (locationPermissionState.status.isGranted) {
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