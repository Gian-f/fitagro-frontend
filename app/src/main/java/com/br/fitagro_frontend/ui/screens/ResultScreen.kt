package com.br.fitagro_frontend.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.fitagro_frontend.R
import com.br.fitagro_frontend.domain.navigation.Screen
import com.br.fitagro_frontend.domain.viewmodel.MainViewModel
import com.br.fitagro_frontend.ui.theme.primaryLight
import com.br.fitagro_frontend.ui.theme.yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, mainViewModel: MainViewModel) {
    val classeToxicologica by mainViewModel.classeToxicologica.collectAsStateWithLifecycle()
    val classeAmbiental by mainViewModel.classeAmbiental.collectAsStateWithLifecycle()
    val result by mainViewModel.resultName.collectAsStateWithLifecycle()

    BackHandler {
        navController.popBackStack(Screen.Home.route, inclusive = false)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack(
                    Screen.Home.route,
                    inclusive = false
                )
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
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(
                        Alignment.Center
                    )
            ) {
                Row {
                    Text(
                        text = "Exibindo resultados para: ",
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )
                    Text(
                        text = result ?: "",
                        fontWeight = FontWeight.W600,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Classe toxicológica do produto é:", fontWeight = FontWeight.W600)
                Text(
                    text = classeToxicologica
                        ?.replace("Categoria 1 \u0096", "")?.trimStart()
                        ?.replace("Categoria 2 \u0096", "")?.trimStart()
                        ?.replace("Categoria 3 \u0096", "")?.trimStart()
                        ?.replace("Categoria 4 \u0096", "")?.trimStart()
                        ?.replace("Categoria 5 \u0096", "")?.trimStart()
                        ?: "Sucesso!",
                    fontSize = 18.sp,
                    color = if (classeToxicologica?.contains("Produto Pouco Tóxico") == true || classeToxicologica?.equals(
                            "Improvável"
                        ) == true
                    ) primaryLight
                    else if (classeToxicologica?.contains("Produto Muito Tóxico") == true) Color.Red else yellow,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Classe ambiental do produto é:", fontWeight = FontWeight.W600)
                Text(
                    text = (classeAmbiental)?.trimStart() ?: "Sucesso!",
                    fontSize = 18.sp,
                    color = if (classeAmbiental?.contains("Pouco") == true || classeToxicologica?.contains(
                            "Improvável"
                        ) == true
                    ) primaryLight
                    else if (classeAmbiental?.contains("Produto Muito Perigoso ao Meio Ambiente") == true) Color.Red else if (classeAmbiental?.contains(
                            "Produto Perigoso ao Meio Ambiente"
                        ) == true
                    ) yellow else Color.Black,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }
}