package com.sercapcab.rpgduels.game.map

import com.sercapcab.rpgduels.ui.navigation.NavScreens

sealed class MapScreens(val route: String) {
    data object FirstMap: MapScreens("first_map")
    data object SecondMap: MapScreens("second_map")
    data object ThirdMap: MapScreens("third_map")
}