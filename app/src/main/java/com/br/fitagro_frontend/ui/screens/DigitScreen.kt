package com.br.fitagro_frontend.ui.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.fitagro_frontend.R
import com.br.fitagro_frontend.domain.navigation.Screen
import com.br.fitagro_frontend.domain.viewmodel.MainViewModel
import com.br.fitagro_frontend.ui.theme.primaryLight
import com.br.fitagro_frontend.util.Utils.LoadingDialog
import com.br.fitagro_frontend.util.Utils.ShowErrorSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitScreen(navController: NavController, mainViewModel: MainViewModel) {

    val context = LocalContext.current as Activity
    val inputBarcode by mainViewModel.inputName.collectAsStateWithLifecycle()
    val isLoading by mainViewModel.isResultLoading.collectAsStateWithLifecycle()
    val errorMessage by mainViewModel.errorMessage.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }
    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    LaunchedEffect(Unit) {
        mainViewModel.clearBarcodeInput()
        mainViewModel.clearErrorMessage()
        mainViewModel.isResultLoading.value = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {

                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = ""
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = if (inputBarcode.isNotEmpty()) primaryLight else Color.LightGray,
                onClick = {
                    if (inputBarcode.isNotEmpty()) {
                        mainViewModel.sendName(
                            name = inputBarcode,
                            onSuccess = {
                                navController.navigate(
                                    route = Screen.Result.route,
                                )
                            },
                            onFailure = {
                                mainViewModel.setIsErrorDialogVisible(true)
                            }
                        )
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "Camera",
                    tint = if (inputBarcode.isNotEmpty()) MaterialTheme.colorScheme.onPrimary else Color.DarkGray
                )
            }
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 26.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Digite o nome da fruta que você deseja fornecer.",
                lineHeight = TextUnit(35F, TextUnitType.Sp),
                fontSize = TextUnit(28F, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "O nome deverá estar próximo a seção de hortifruti",
                color = Color.DarkGray,
                fontSize = TextUnit(18F, TextUnitType.Sp),
                fontWeight = FontWeight.W400,
            )

            Spacer(modifier = Modifier.height(30.dp))
            val options =
                listOf(
                    "Café",
                    "Arroz",
                    "Cana-de-açúcar",
                    "Milho",
                    "Soja",
                    "Pastagens",
                    "Trigo",
                    "Cevada",
                    "Maçã",
                    "Melancia",
                    "Mamão",
                    "Algodão",
                    "Kiwi",
                    "Melão"
                )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = primaryLight,
                        focusedBorderColor = primaryLight
                    ),
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText ?: "",
                    onValueChange = { },
                    label = { Text("Selecione...") },
                    placeholder = { Text("Selecione...") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = selectionOption)
                            },
                            onClick = {
                                selectedOptionText = selectionOption
                                mainViewModel.onInputBarcodeChange(selectionOption)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }

    if (isLoading) {
        LoadingDialog()
    }

    if (!errorMessage.isNullOrEmpty()) {
        mainViewModel.setIsErrorDialogVisible(true)
        ShowErrorSheet(message = errorMessage.toString(),
            onDismiss = {
                mainViewModel.clearBarcodeInput()
                mainViewModel.clearErrorMessage()
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mainViewModel.clearErrorMessage()
            mainViewModel.isResultLoading.value = false
            mainViewModel.setIsErrorDialogVisible(false)
        }
    }
}

