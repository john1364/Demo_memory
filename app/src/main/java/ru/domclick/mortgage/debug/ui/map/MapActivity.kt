package ru.domclick.mortgage.debug.ui.map

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import ru.dgis.sdk.compose.map.MapComposable
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.getSystemMemoryManager
import ru.dgis.sdk.map.CameraPosition
import ru.dgis.sdk.map.Zoom
import ru.dgis.sdk.platform.KeyFromAsset
import ru.dgis.sdk.platform.KeySource
import ru.domclick.mortgage.debug.App
import ru.domclick.mortgage.debug.MAP_SIZE
import ru.domclick.mortgage.debug.ui.theme.MyApplicationTheme

val dGisKeySource = KeySource(KeyFromAsset("dgissdk - 2024-02-06T134529.788.key"))

class MapActivity : ComponentActivity() {

    private val viewModel: MapVM by viewModels()

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MAP_SIZE = ++MAP_SIZE

        setContent {
            val screen by remember { mutableIntStateOf(MAP_SIZE) }
            MyApplicationTheme {
                val state by remember { mutableStateOf(viewModel.state) }
                val map by state!!.map.collectAsState()
                val lifecycle = LocalLifecycleOwner.current

                DisposableEffect(lifecycle.lifecycle.currentStateAsState().value.ordinal) {
                    when (lifecycle.lifecycle.currentState) {
                        Lifecycle.State.STARTED -> {
                            map?.close()
                            getSystemMemoryManager((applicationContext as App).gisContext!!).reduceMemoryUsage()
                            System.gc()
                        }

                        else -> Unit
                    }
                    onDispose {
                        map?.close()
                        getSystemMemoryManager((applicationContext as App).gisContext!!).reduceMemoryUsage()
                    }
                }

                DisposableEffect(state, map) {
                    map?.camera?.move(
                        position = CameraPosition(
                            zoom = Zoom(16f),
                            point = GeoPoint(
                                latitude = 55.760898,
                                longitude = 37.620242,
                            )
                        )
                    )
                    onDispose {}
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        FlowRow(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Button(
                                onClick = {
                                    this@MapActivity.finish()
                                }) {
                                Text("Назад")
                            }
                            Button(
                                onClick = {
                                    this@MapActivity.startActivity(
                                        Intent(this@MapActivity, MapActivity::class.java)
                                    )
                                }) {
                                Text("Карта")
                            }

                            Text(text = "Открыто экранов с картой: $screen")
                        }

                        MapComposable(
                            modifier = Modifier
                                .fillMaxSize(),
                            state = state!!,
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MAP_SIZE = --MAP_SIZE
    }
}