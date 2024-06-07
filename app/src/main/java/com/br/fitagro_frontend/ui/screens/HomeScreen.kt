package com.br.fitagro_frontend.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.br.fitagro_frontend.domain.navigation.Screen
import com.br.fitagro_frontend.util.Utils.ExpandableFAB

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    var showPermissionsDeniedDialog by remember { mutableStateOf(false) }
    var isCameraPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            isCameraPermissionGranted = isGranted
            if (isGranted) {
                navController.navigate(Screen.Camera.route)
            } else {
                showPermissionsDeniedDialog = true
            }
        }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExpandableFAB(
                onQrCodeClicked = {
                    if (isCameraPermissionGranted) {
                        navController.navigate(Screen.Camera.route)
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onBarcodeClicked = {
                    if (isCameraPermissionGranted) {
                        navController.navigate(Screen.Camera.route)
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )
        }
    )
    { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }
    }
}