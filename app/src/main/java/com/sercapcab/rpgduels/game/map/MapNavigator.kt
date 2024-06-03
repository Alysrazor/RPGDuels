package com.sercapcab.rpgduels.game.map

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sercapcab.rpgduels.ui.navigation.NavScreens
import com.sercapcab.rpgduels.ui.screen.game.GameMenuScreen

@Composable
fun MapNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavScreens.GameMenuScreen.route) {
        composable(NavScreens.GameMenuScreen.route) {
            GameMenuScreen(navController = navController)
        }
    }
}
