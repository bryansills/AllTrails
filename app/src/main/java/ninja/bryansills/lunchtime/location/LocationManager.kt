package ninja.bryansills.lunchtime.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ninja.bryansills.lunchtime.Dispatcher
import ninja.bryansills.lunchtime.LunchtimeDispatcher
import javax.inject.Inject

interface LocationManager {
    suspend fun getLatestLocation(): LatLng
}

class DefaultLocationManager @Inject constructor(
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