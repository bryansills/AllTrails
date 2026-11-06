package ninja.bryansills.lunchtime

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import ninja.bryansills.lunchtime.di.LunchtimeViewModelFactory
import ninja.bryansills.lunchtime.home.Home
import ninja.bryansills.lunchtime.home.HomeScreen
import ninja.bryansills.lunchtime.ui.theme.LunchtimeTheme

@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey
class MainActivity(
    private val viewModelFactory: LunchtimeViewModelFactory
) : ComponentActivity() {
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalMetroViewModelFactory provides viewModelFactory) {
                LunchtimeTheme {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Home) {
                        composable<Home> {
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}
