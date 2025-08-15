package ru.domclick.mortgage.debug

import ru.dgis.sdk.platform.KeyFromAsset
import ru.dgis.sdk.platform.KeySource

val dGisKeySource = KeySource(KeyFromAsset("dgissdk - 2024-02-06T134529.788.key"))

data class Location(val lat: Double, val lon: Double)

val list = listOf(
    Location(55.760898, 37.620242), // москва
    Location(52.288747, 104.28078), // иркутск
    Location(56.007748, 92.853203), // красноярск
    Location(56.47411, 84.952346), // томск
    Location(55.016187, 82.947889), // новосибирск
    Location(54.955901, 73.388555), // омск
    Location(56.828702, 60.600096), // екатеринбург
    Location(56.327593, 44.00172), // нижний новгород
    Location(59.937476, 30.307622), // питер
    Location(48.483993, 135.085617), // хабаровск
)

data class Points(
    val location: List<Location>,
)

val points = listOf(
    Points(
        listOf(
            Location(55.759763, 37.62168),
            Location(55.761531, 37.61748),
            Location(55.758351, 37.61626),
            Location(55.761881, 37.621161),
        ),
    ),
    Points(
        listOf(
            Location(52.287708, 104.281445),
            Location(52.290403, 104.284123),
            Location(52.28825, 104.283216),
        ),
    )
)