package com.sercapcab.rpgduels.game.map

sealed class MapScreens(val route: String) {
    data object BattleScenario: MapScreens("battle_scenario")
}