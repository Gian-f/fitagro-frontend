package com.br.fitagro_frontend.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.br.fitagro_frontend.R
import com.br.fitagro_frontend.domain.navigation.Screen
import com.br.fitagro_frontend.domain.viewmodel.MainViewModel
import com.br.fitagro_frontend.util.Utils.ExpandableFAB
import com.br.fitagro_frontend.util.Utils.ShowErrorSheet

@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
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
                onDigitClicked = {
                    navController.navigate(Screen.DigitBarcode.route)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(300.dp),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                )
                Text(
                    text = "Clique no botão abaixo para começar!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
            }
        }
    }
    if (showPermissionsDeniedDialog) {
        ShowErrorSheet(
            message = "Permissões negadas podem resultar em funcionalidades indisponíveis.",
            onDismiss = {
                showPermissionsDeniedDialog = false
            }
        )
    }
}