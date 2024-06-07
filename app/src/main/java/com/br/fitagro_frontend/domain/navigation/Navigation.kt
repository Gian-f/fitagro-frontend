package com.br.fitagro_frontend.domain.navigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Pedidos")
    data object Camera : Screen("home", "Pedidos")
    data object DigitBarcode : Screen("digitBarcode", "Confirme o código de barras")
}