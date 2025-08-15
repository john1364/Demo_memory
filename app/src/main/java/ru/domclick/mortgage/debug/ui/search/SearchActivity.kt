package ru.domclick.mortgage.debug.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.currentStateAsState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.dgis.sdk.DGis
import ru.dgis.sdk.compose.map.MapComposable
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.geometry.GeoPointWithElevation
import ru.dgis.sdk.map.CameraPosition
import ru.dgis.sdk.map.Map
import ru.dgis.sdk.map.MapObjectManager
import ru.dgis.sdk.map.Marker
import ru.dgis.sdk.map.MarkerOptions
import ru.dgis.sdk.map.Zoom
import ru.dgis.sdk.map.imageFromBitmap
import ru.domclick.mortgage.debug.Init.Companion.DEFAULT_ZOOM
import ru.domclick.mortgage.debug.Init.Companion.getOrCreateMapComposableState
import ru.domclick.mortgage.debug.Init.Companion.getOrCreateMapObjectManager
import ru.domclick.mortgage.debug.Location
import ru.domclick.mortgage.debug.MAP_SIZE
import ru.domclick.mortgage.debug.R
import ru.domclick.mortgage.debug.list
import ru.domclick.mortgage.debug.ui.map.MapActivity
import ru.domclick.mortgage.debug.ui.theme.MyApplicationTheme

class SearchActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MAP_SIZE = ++MAP_SIZE

        setContent {
            val screen by remember { mutableIntStateOf(MAP_SIZE) }

            MyApplicationTheme {
                val state by remember { mutableStateOf(getOrCreateMapComposableState(this@SearchActivity)) }

                var map: Map? = null
                GlobalScope.launch {
                    state.map.collect {
                        if (it != null) {
                            map = it

                            val res = getOrCreateMapObjectManager(this@SearchActivity, it)
                            addPoints(
                                context = this@SearchActivity,
                                map = map,
                                mapObjectManager = res,
                            )

                            state.objectTappedCallback = { renderedObjectInfo ->
                                val point = renderedObjectInfo.item.item
                                if (point is Marker) {
                                    this@SearchActivity.startActivity(
                                        Intent(
                                            this@SearchActivity,
                                            MapActivity::class.java
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                LaunchedEffect(lifecycle.currentStateAsState().value.ordinal) {
                    when (lifecycle.currentState) {
                        Lifecycle.State.RESUMED -> {
                            map?.also { map ->
                                val res = getOrCreateMapObjectManager(this@SearchActivity, map)
                                addPoints(
                                    context = this@SearchActivity,
                                    map = map,
                                    mapObjectManager = res,
                                )
                            }
                        }

                        else -> Unit
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Button(
                                onClick = {
                                    this@SearchActivity.finish()
                                }) {
                                Text("Назад")
                            }

                            Text(text = "Открыто экранов с картой: $screen")

                            Text(text = "Экран поиска объекта")
                        }

                        MapComposable(
                            modifier = Modifier.fillMaxSize(),
                            state = state,
                        )
                    }
                }
            }
        }
    }

    private fun addPoints(
        context: Context,
        map: Map?,
        mapObjectManager: MapObjectManager,
    ) {
        move(map, list[0])
        addPoi(context, mapObjectManager)
    }

    private fun move(map: Map?, location: Location) {
        map?.camera?.move(
            position = CameraPosition(
                zoom = Zoom(DEFAULT_ZOOM),
                point = GeoPoint(
                    latitude = location.lat,
                    longitude = location.lon,
                )
            )
        )
    }

    private fun addPoi(
        context: Context,
        mapObjectManager: MapObjectManager,
    ) {
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_poi_pin_fill)!!.toBitmap()

        listOf(
            Location(55.759763, 37.62168),
            Location(55.761531, 37.61748),
            Location(55.758351, 37.61626),
            Location(55.761881, 37.62116),
            Location(55.760898, 37.62024),
        ).forEach { point ->
            val options = MarkerOptions(
                position = GeoPointWithElevation(
                    point = GeoPoint(
                        latitude = point.lat,
                        longitude = point.lon
                    )
                ),
                icon = imageFromBitmap(context = DGis.context(), bitmap = icon)
            )

            mapObjectManager.addObject(Marker(options))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MAP_SIZE = --MAP_SIZE
    }
}