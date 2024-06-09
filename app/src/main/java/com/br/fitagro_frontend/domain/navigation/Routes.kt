package com.br.fitagro_frontend.domain.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.br.fitagro_frontend.domain.viewmodel.MainViewModel
import com.br.fitagro_frontend.ui.screens.CameraScreen
import com.br.fitagro_frontend.ui.screens.DigitScreen
import com.br.fitagro_frontend.ui.screens.HomeScreen
import com.br.fitagro_frontend.ui.screens.ResultScreen

@Composable
fun Navigation(navController: NavHostController) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, mainViewModel)
        }
        composable(Screen.Camera.route) {
            CameraScreen(navController, mainViewModel)
        }
        composable(Screen.DigitBarcode.route) {
            DigitScreen(navController, mainViewModel)
        }
        composable(Screen.Result.route) {
            ResultScreen(navController, mainViewModel)
        }
    }
}