package ru.domclick.mortgage.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.dgis.sdk.DGis
import ru.dgis.sdk.compose.map.MapComposable
import ru.dgis.sdk.compose.map.MapComposableState
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.map.CameraPosition
import ru.dgis.sdk.map.MapOptions
import ru.dgis.sdk.map.Zoom
import ru.dgis.sdk.platform.KeyFromAsset
import ru.dgis.sdk.platform.KeySource
import ru.domclick.mortgage.debug.ui.theme.MyApplicationTheme

val dGisKeySource = KeySource(KeyFromAsset("dgissdk - 2024-02-06T134529.788.key"))

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        DGis.initialize(
            appContext = this,
            keySource = dGisKeySource
        )

        setContent {
            MyApplicationTheme {
                val state by remember { mutableStateOf(MapComposableState(MapOptions())) }
                val map by state.map.collectAsState()
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
                    MapComposable(
                        state = state,
                    )
                }
            }
        }
    }
}