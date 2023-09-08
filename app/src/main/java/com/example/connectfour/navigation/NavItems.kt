package com.example.connectfour.navigation

import com.example.connectfour.R

enum class NavItems(val route: String, val description: Int) {
    StartScreen(route = "start", description = R.string.start_screen),
    GameScreen(route = "game", description = R.string.game_screen)
}