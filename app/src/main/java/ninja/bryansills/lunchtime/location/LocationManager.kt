package ninja.bryansills.lunchtime.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ninja.bryansills.lunchtime.di.Dispatcher
import ninja.bryansills.lunchtime.di.LunchtimeDispatcher

interface LocationManager {
    suspend fun getLatestLocation(): LatLng
}

@ContributesBinding(AppScope::class)
@Inject
class DefaultLocationManager(
    private val context: Context,
    @Dispatcher(LunchtimeDispatcher.Io) private val ioDispatcher: CoroutineDispatcher
): LocationManager {
    private val playServices by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLatestLocation(): LatLng {
        return try {
            val lastLocation = withContext(ioDispatcher) { playServices.lastLocation.await() }
            LatLng(lastLocation.latitude, lastLocation.longitude)
        } catch (ex: Exception) {
            Log.e("BAD", ex.localizedMessage, ex)
            throw IllegalStateException("Cannot find location")
        }
    }
}