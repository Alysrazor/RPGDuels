package com.sercapcab.rpgduels.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sercapcab.rpgduels.ui.screen.LoginScreen
import com.sercapcab.rpgduels.ui.screen.SplashScreen
import com.sercapcab.rpgduels.ui.screen.game.GameMenuScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavScreens.SplashScreen.route) {
        composable(route = NavScreens.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = NavScreens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = NavScreens.GameMenuScreen.route) {
            GameMenuScreen(navController = navController)
        }
    }
}
