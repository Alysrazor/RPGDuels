package com.sercapcab.rpgduels.ui.navigation

sealed class NavScreens(val route: String) {
    data object SplashScreen: NavScreens("splash_screen")
    data object LoginScreen: NavScreens("login_screen")
    data object MainMenuScreen: NavScreens("main_screen")
    data object GameMenuScreen: NavScreens("game_screen")
    data object SettingsScreen: NavScreens("settings_screen")
}