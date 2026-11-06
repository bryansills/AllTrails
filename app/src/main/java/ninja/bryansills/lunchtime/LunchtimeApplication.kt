package ninja.bryansills.lunchtime

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication

class LunchtimeApplication : Application(), MetroApplication {
    private lateinit var androidAppGraph: AndroidAppGraph

    override val appComponentProviders: MetroAppComponentProviders
        get() = androidAppGraph

    override fun onCreate() {
        super.onCreate()
        androidAppGraph = createGraphFactory<AndroidAppGraph.Factory>().create(this)
    }
}