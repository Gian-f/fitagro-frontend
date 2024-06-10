package com.br.fitagro_frontend.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.br.fitagro_frontend.R
import com.br.fitagro_frontend.domain.navigation.Screen
import com.br.fitagro_frontend.domain.viewmodel.MainViewModel
import com.br.fitagro_frontend.ui.theme.primaryLight
import com.br.fitagro_frontend.ui.theme.yellow
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

enum class ResultColor {
    Red,
    Yellow,
    Green
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, mainViewModel: MainViewModel) {
    val result by mainViewModel.result.collectAsStateWithLifecycle()
    val resultName by mainViewModel.resultName.collectAsStateWithLifecycle()
    val context = LocalContext.current
    BackHandler {
        navController.popBackStack(Screen.Home.route, inclusive = false)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = ""
                    )
                }
            },
            title = { Text(text = "Resultado") })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Row {
                    Text(
                        text = "Exibindo resultados para: ",
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )
                    Text(text = resultName ?: "", fontWeight = FontWeight.W600, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (!result?.url.isNullOrEmpty()) {
                    val imageRequest = ImageRequest.Builder(context)
                        .data(result?.url)
                        .dispatcher(Dispatchers.IO)
                        .memoryCacheKey(result?.url)
                        .diskCacheKey(result?.url)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build()

                    AsyncImage(
                        model = imageRequest,
                        contentDescription = "Image Description",
                        modifier = Modifier
                            .size(300.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(15.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                DisplayClassInfo(
                    "Classe toxicológica do produto é:",
                    result?.classeToxicologica,
                    determineResultColor(result?.classeToxicologica)
                )
                Spacer(modifier = Modifier.height(16.dp))
                DisplayClassInfo(
                    "Classe ambiental do produto é:",
                    result?.classeAmbiental,
                    determineResultColor(result?.classeAmbiental)
                )
            }
        }
    }
}


@Composable
fun DisplayClassInfo(label: String, classInfo: String?, desiredColor: ResultColor) {
    Text(text = label, fontWeight = FontWeight.W600, fontSize = 14.sp)
    val cleanedText = classInfo?.let {
        it.replace("Categoria 1 \u0096", "").trimStart()
            .replace("Categoria 2 \u0096", "").trimStart()
            .replace("Categoria 3 \u0096", "").trimStart()
            .replace("Categoria 4 \u0096", "").trimStart()
            .replace("Categoria 5 \u0096", "").trimStart()
            ?: "Sucesso!"
    } ?: "Sucesso!"

    val textColor = when (desiredColor) {
        ResultColor.Red -> Color.Red
        ResultColor.Yellow -> yellow
        ResultColor.Green -> primaryLight
    }

    Text(text = cleanedText, fontSize = 16.sp, fontWeight = FontWeight.W500, color = textColor)
}


fun determineResultColor(classInfo: String?): ResultColor {
    return when {
        classInfo?.contains("Produto Pouco Tóxico") == true || classInfo?.contains("Improvável") == true -> ResultColor.Green
        classInfo?.contains("Produto Muito Tóxico") == true || classInfo?.contains("Muito Perigoso") == true -> ResultColor.Red
        else -> ResultColor.Yellow
    }
}