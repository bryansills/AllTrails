package ninja.bryansills.lunchtime.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.serialization.Serializable
import ninja.bryansills.lunchtime.R

@Serializable
data object Home

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    WithLocation {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Text("We good!")
            }
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