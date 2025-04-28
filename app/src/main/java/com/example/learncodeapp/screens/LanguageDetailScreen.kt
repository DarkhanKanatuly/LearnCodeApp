package com.example.learncodeapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learncodeapp.viewmodels.LanguageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDetailScreen(navController: NavController, viewModel: LanguageViewModel) {
    val languageId = navController.currentBackStackEntry?.arguments?.getString("languageId")?.toLongOrNull()
    val languages by remember { mutableStateOf(viewModel.languages) }
    val language = languages.find { lang -> lang.id == languageId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = language?.name ?: "Язык не найден",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A4CAF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
        },
        containerColor = Color(0xFFF9F7FF)
    ) { padding ->
        if (language == null) {
            Text(
                text = "Язык не найден",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .wrapContentSize()
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    color = Color(0xFF6A4CAF)
                ) {
                    Text(
                        text = language.name.first().toString(),
                        color = Color.White,
                        fontSize = 48.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = language.description,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Временно убрали переход на videoPlayerScreen
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A4CAF)
                    )
                ) {
                    Text("Смотреть видео", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}