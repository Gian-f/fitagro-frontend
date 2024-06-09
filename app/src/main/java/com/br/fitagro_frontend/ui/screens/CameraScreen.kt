package com.br.fitagro_frontend.ui.screens

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.fitagro_frontend.BarcodeAnalyzer
import com.br.fitagro_frontend.R
import com.br.fitagro_frontend.domain.navigation.Screen
import com.br.fitagro_frontend.domain.viewmodel.MainViewModel
import com.br.fitagro_frontend.util.CutOutShape
import com.br.fitagro_frontend.util.Utils.LoadingDialog
import com.br.fitagro_frontend.util.Utils.ShowErrorSheet

@Composable
fun CameraScreen(
    navController: NavController,
    viewModel: MainViewModel,
) {
    val context = LocalContext.current as ComponentActivity
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isResultLoading by viewModel.isResultLoading.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    LaunchedEffect(Unit) {
        viewModel.isResultLoading.value = false
        viewModel.setIsErrorDialogVisible(false)
        viewModel.clearBarcodeInput()
        viewModel.clearErrorMessage()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.isResultLoading.value = false
            viewModel.setIsErrorDialogVisible(false)
        }
    }

    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    Box {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                preview.setSurfaceProvider(previewView.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder().build()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    BarcodeAnalyzer(context, navController, viewModel)
                )

                runCatching {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                }.onFailure {
                    Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
                }
                previewView
            }
        )

        Surface(
            shape = CutOutShape(),
            color = Color.Black.copy(alpha = 0.45f),
            modifier = Modifier.fillMaxSize()
        ) { }

        Column(
            modifier = Modifier
                .padding(32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    contentDescription = "Câmera",
                    tint = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Aponte a câmera para o código da fruta que você deseja scannear.",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(50.dp),
                onClick = { navController.navigate(Screen.DigitBarcode.route) }) {
                Text(
                    text =
                    "Digitar nome da fruta",
                    color = Color.White,
                    fontSize = TextUnit(18F, type = TextUnitType.Sp)
                )
            }
        }
    }

    if (isResultLoading) {
        LoadingDialog()
    }

    if (!errorMessage.isNullOrEmpty()) {
        viewModel.setIsErrorDialogVisible(true)
        ShowErrorSheet(message = "erro!",
            onDismiss = {
                navController.popBackStack()
            }
        )
    }
}