package ru.domclick.mortgage.debug

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.dgis.sdk.DGis
import ru.dgis.sdk.PersonalDataCollectionConsent
import ru.dgis.sdk.compose.map.MapComposable
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.getSystemMemoryManager
import ru.dgis.sdk.map.CameraPosition
import ru.dgis.sdk.map.Zoom
import ru.dgis.sdk.platform.HttpOptions
import ru.dgis.sdk.platform.KeyFromAsset
import ru.dgis.sdk.platform.KeySource
import ru.domclick.mortgage.debug.ui.theme.MyApplicationTheme

val dGisKeySource = KeySource(KeyFromAsset("dgissdk - 2024-02-06T134529.788.key"))

class MapActivity : ComponentActivity() {

    private val viewModel: MapVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val gisContext = DGis.initialize(
            appContext = this,
            keySource = dGisKeySource,
            dataCollectConsent = PersonalDataCollectionConsent.DENIED,
            httpOptions = HttpOptions(
                useCache = true,
                cacheMaxSize = 10,
                cacheStoragePath = "/cache"
            ),
        )

        setContent {
            MyApplicationTheme {
                val state by remember { mutableStateOf(viewModel.state) }
                val map by state!!.map.collectAsState()

                DisposableEffect(Unit) {
                    onDispose {
                        map?.close()
                        getSystemMemoryManager(gisContext).reduceMemoryUsage()
                    }
                }

                map?.camera?.move(
                    position = CameraPosition(
                        zoom = Zoom(16f),
                        point = GeoPoint(
                            latitude = 55.760898,
                            longitude = 37.620242,
                        )
                    )
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Button(
                            modifier = Modifier.padding(innerPadding),
                            onClick = {
                                this@MapActivity.startActivity(
                                    Intent(this@MapActivity, MapActivity::class.java)
                                )
                            }) {
                            Text("Карта")
                        }

                        MapComposable(
                            modifier = Modifier.padding(innerPadding).fillMaxSize(),
                            state = state!!,
                        )
                    }
                }
            }
        }
    }
}