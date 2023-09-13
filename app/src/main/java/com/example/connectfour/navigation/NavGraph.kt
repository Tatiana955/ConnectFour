package com.example.connectfour.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.connectfour.ui.screens.AppViewModel
import com.example.connectfour.ui.screens.game.GameScreen
import com.example.connectfour.ui.screens.start.StartScreen

@Composable
fun NavGraph(
    paddingValues: PaddingValues,
    viewModel: AppViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavItems.StartScreen.route
    ) {
        composable(NavItems.StartScreen.route) {
            StartScreen(
                paddingValues = paddingValues,
                viewModel = viewModel,
                startGame = { navController.navigate(NavItems.GameScreen.route) }
            )
        }

        composable(NavItems.GameScreen.route) {
            GameScreen(
                paddingValues = paddingValues,
                viewModel = viewModel,
                returnToStartScreen = { navController.popBackStack() }
            )
        }
    }
}