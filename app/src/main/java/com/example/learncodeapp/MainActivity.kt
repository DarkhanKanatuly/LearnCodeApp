package com.example.learncodeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.learncodeapp.data.AppDatabase
import com.example.learncodeapp.screens.HomeScreen
import com.example.learncodeapp.screens.LanguageDetailScreen // Импорт должен быть правильным
import com.example.learncodeapp.screens.SettingsScreen
import com.example.learncodeapp.ui.theme.AppTheme
import com.example.learncodeapp.viewmodels.LanguageViewModel
import com.example.learncodeapp.viewmodels.LanguageViewModelFactory // Импорт должен быть правильным

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()

    val viewModel: LanguageViewModel = viewModel(factory = LanguageViewModelFactory(database))
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("language_detail/{languageName}") { backStackEntry ->
            val languageName = backStackEntry.arguments?.getString("languageName") ?: ""
            LanguageDetailScreen(navController = navController, languageName = languageName, viewModel = viewModel)
        }
        composable("settings") {
            SettingsScreen(navController = navController) // Ошибка здесь, исправим SettingsScreen.kt
        }
    }
}