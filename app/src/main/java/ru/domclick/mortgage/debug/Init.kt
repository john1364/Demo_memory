package ru.domclick.mortgage.debug

import android.content.Context
import ru.dgis.sdk.compose.map.MapComposableState
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.map.CameraPosition
import ru.dgis.sdk.map.GraphicsPreset
import ru.dgis.sdk.map.Map
import ru.dgis.sdk.map.MapObjectManager
import ru.dgis.sdk.map.MapOptions
import ru.dgis.sdk.map.Zoom

class Init {

    companion object {
        private const val DEFAULT_LAT = 55.760898
        private const val DEFAULT_LON = 37.620242
        const val DEFAULT_ZOOM = 16f

        @Volatile
        private var MapComposableStateInstance: MapComposableState? = null

        @Volatile
        private var MapObjectManager: MapObjectManager? = null

        @Volatile
        private var MapOptions: MapOptions? = null

        fun getOrCreateMapComposableState(
            context: Context,
            mapOptions: MapOptions? = null
        ): MapComposableState {
            return MapComposableStateInstance ?: synchronized(context) {
                MapComposableStateInstance ?: MapComposableState(
                    mapOptions = mapOptions ?: createMapOptions(),
                ).also { MapComposableStateInstance = it }
            }
        }

        fun getOrCreateMapObjectManager(
            context: Context,
            map: Map,
        ): MapObjectManager {
            return MapObjectManager ?: synchronized(context) {
                MapObjectManager ?: MapObjectManager(map).also {
                    MapObjectManager = it
                }
            }
        }

        private fun createMapOptions(
            lat: Double? = null,
            lon: Double? = null,
            zoom: Float? = null,
        ): MapOptions {
            return MapOptions ?: MapOptions().apply {
                position = CameraPosition(
                    point = GeoPoint(
                        latitude = lat ?: DEFAULT_LAT,
                        longitude = lon ?: DEFAULT_LON
                    ),
                    zoom = Zoom(zoom ?: DEFAULT_ZOOM)
                )
                graphicsPreset = GraphicsPreset.LITE
                MapOptions = this
            }
        }
    }
}