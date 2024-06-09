package com.br.fitagro_frontend.util

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.br.fitagro_frontend.R
import com.br.fitagro_frontend.data.enums.FabOption
import com.br.fitagro_frontend.ui.theme.primaryLight
import kotlinx.coroutines.launch

class CutOutShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val outlinePath = Path()
        outlinePath.addRect(Rect(Offset(0f, 0f), size))

        val cutoutHeight = size.height * 0.4f
        val cutoutWidth = size.width * 0.75f
        val center = Offset(size.width / 2f, size.height / 2f)

        val cutoutPath = Path()
        cutoutPath.addRoundRect(
            RoundRect(
                Rect(
                    topLeft = center - Offset(
                        cutoutWidth / 2f,
                        cutoutHeight / 2f
                    ),
                    bottomRight = center + Offset(
                        cutoutWidth / 2f,
                        cutoutHeight / 2f
                    )
                ),
                cornerRadius = CornerRadius(16f, 16f)
            )
        )

        val finalPath = Path.combine(
            PathOperation.Difference,
            outlinePath,
            cutoutPath
        )

        return Outline.Generic(finalPath)
    }
}

object Utils {
    @Composable
    fun ExpandableFAB(
        onDigitClicked: (String) -> Unit,
        onQrCodeClicked: (String) -> Unit,
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column(horizontalAlignment = Alignment.End) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                horizontalAlignment = Alignment.End,
            ) {
                if (expanded) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier.width(200.dp),
                        containerColor = primaryLight,
                        onClick = { onQrCodeClicked.invoke(FabOption.QRCODE.value) },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Scannear cultura", color = Color.White)
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ExtendedFloatingActionButton(
                        modifier = Modifier.width(200.dp),
                        containerColor = primaryLight,
                        onClick = { onDigitClicked.invoke(FabOption.BARCODE.value) },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Selecionar cultura", color = Color.White)
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_colorize_24),
                                contentDescription = null,
                                tint = Color.White

                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            FloatingActionButton(
                containerColor = primaryLight,
                onClick = { expanded = !expanded },
                content = {
                    Icon(
                        painter = if (!expanded)
                            painterResource(id = R.drawable.baseline_camera_alt_24) else
                            painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = "Camera",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
            )
        }
    }

    @Composable
    fun LoadingDialog(
        message: String = "Carregando. Por favor, aguarde...",
    ) {
        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            properties = DialogProperties(usePlatformDefaultWidth = false),
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    LoadingAnimation()
                    Text(
                        textAlign = TextAlign.Center,
                        text = message,
                        fontSize = TextUnit(22f, TextUnitType.Sp),
                        lineHeight = TextUnit(30f, TextUnitType.Sp),
                    )
                }
            },
            confirmButton = {},
        )
    }


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    fun ShowErrorSheet(
        message: String,
        onDismiss: () -> Unit = {},
    ) {
        val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            state.expand()
        }
        if (state.isVisible) {
            ModalBottomSheet(
                sheetState = state,
                onDismissRequest = {
                    scope.launch { state.hide() }
                    onDismiss.invoke()
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = message,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonColors(
                                Color.Black,
                                Color.White,
                                Color.Black,
                                Color.Black
                            ),
                            onClick = {
                                scope.launch { state.hide() }
                                onDismiss.invoke()
                            }) {
                            Text(text = "OK")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            )
        }
    }

    @Composable
    fun LoadingAnimation() {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(300.dp),
                iterations = LottieConstants.IterateForever
            )
        }
    }
}
