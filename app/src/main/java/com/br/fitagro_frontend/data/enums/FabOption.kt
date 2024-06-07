package com.br.fitagro_frontend.data.enums

sealed class FabOption(val value: String) {
    data object EMPTY : FabOption("")
    data object QRCODE : FabOption("QrCode")
    data object BARCODE : FabOption("Barcode")
}