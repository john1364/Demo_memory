package ru.domclick.mortgage.debug

import androidx.lifecycle.ViewModel
import ru.dgis.sdk.compose.map.MapComposableState
import ru.dgis.sdk.map.MapOptions

class MapVM : ViewModel() {

    var state: MapComposableState? = null

    init {
        state = MapComposableState(MapOptions())
    }

    override fun onCleared() {
        super.onCleared()
        state = null
    }
}