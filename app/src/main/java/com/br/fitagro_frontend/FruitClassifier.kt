package com.br.fitagro_frontend

import android.content.Context
import android.widget.Toast
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.navigation.NavController
import com.br.fitagro_frontend.data.model.Classification
import com.br.fitagro_frontend.domain.FruitClassifier
import com.br.fitagro_frontend.domain.navigation.Screen
import com.br.fitagro_frontend.domain.viewmodel.MainViewModel
import com.br.fitagro_frontend.presentation.centerCrop
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class BarcodeAnalyzer(
    private val context: Context,
    private val navController: NavController,
    private val viewModel: MainViewModel,
) : ImageAnalysis.Analyzer {

    private var isProcessing = false
    private var lastProcessedTicketResult = ArrayList<String>()

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_ALL_FORMATS
        )
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    init {
        MainScope().launch {
            viewModel.isResultLoading.collect { isLoading ->
                if (isLoading) {
                    isProcessing = false
                }
            }
        }
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (viewModel.isResultLoading.value || viewModel.isErrorDialogVisible.value) return
        if (isProcessing) return
        isProcessing = true

        imageProxy.image?.let { image ->
            scanner.process(
                InputImage.fromMediaImage(
                    image, imageProxy.imageInfo.rotationDegrees
                )
            ).addOnSuccessListener { barcode ->
                imageProxy.close()
                barcode?.takeIf { it.isNotEmpty() }
                    ?.mapNotNull { it.rawValue }
                    ?.joinToString(",")
                    ?.let { result ->
                        lastProcessedTicketResult.find { it == result } ?: run {
                            lastProcessedTicketResult.add(result)
                            if (result.isNotEmpty()) {
                                viewModel.sendName(
                                    result,
                                    onSuccess = {
                                        lastProcessedTicketResult.clear()
                                        viewModel.clearBarcodeInput()
                                        viewModel.clearErrorMessage()
                                        navController.navigate(Screen.Result.route)
                                    }
                                ) {
                                    lastProcessedTicketResult.clear()
                                }
                            }
                        }
                    }
            }.addOnCompleteListener {
                isProcessing = false
            }
        }
    }
}