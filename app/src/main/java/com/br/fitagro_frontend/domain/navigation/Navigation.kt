package com.br.fitagro_frontend.domain.navigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "home")
    data object Camera : Screen("camera", "camera")
    data object Result : Screen("result", "camera")
    data object DigitBarcode : Screen("digit", "Confirme o código de barras")
}