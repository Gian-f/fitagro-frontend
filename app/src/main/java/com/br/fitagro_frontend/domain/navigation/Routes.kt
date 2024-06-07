package com.br.fitagro_frontend.domain.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.br.fitagro_frontend.ui.screens.HomeScreen

@Composable
fun Navigation(navController: NavHostController) {
//    val ticketViewModel = hiltViewModel<TicketViewModel>()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Camera.route) {
            HomeScreen(navController)
        }
        composable(Screen.DigitBarcode.route) {
            HomeScreen(navController)
        }
    }
}