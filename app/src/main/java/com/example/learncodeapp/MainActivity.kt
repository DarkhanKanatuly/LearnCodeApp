package com.example.learncodeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel // Добавляем импорт для viewModel
import com.example.learncodeapp.data.AppDatabase
import com.example.learncodeapp.screens.*
import com.example.learncodeapp.ui.theme.AppTheme
import com.example.learncodeapp.viewmodels.LanguageViewModel
import com.example.learncodeapp.viewmodels.LanguageViewModel.LanguageViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val database = AppDatabase.getDatabase(LocalContext.current)
                val languageViewModel: LanguageViewModel = viewModel(
                    factory = LanguageViewModelFactory(database)
                )

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = languageViewModel)
                        }
                        composable("languageDetailScreen/{languageId}") { backStackEntry ->
                            LanguageDetailScreen(navController = navController, viewModel = languageViewModel)
                        }
                        composable(Screen.Lessons.route) {
                            LessonsScreen(navController = navController)
                        }
                        composable("lesson_detail/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId")?.toLongOrNull()
                            LessonDetailScreen(navController = navController, lessonId = lessonId)
                        }
                        composable(Screen.Messenger.route) {
                            MessengerScreen(navController = navController)
                        }
                        composable("messengerDetailScreen/{chatId}") { backStackEntry ->
                            val chatId = backStackEntry.arguments?.getString("chatId")
                            MessengerDetailScreen(navController = navController)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(navController = navController)
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Lessons : Screen("lessons")
    object Messenger : Screen("messenger")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Pair(Screen.Home, Icons.Default.Home),
        Pair(Screen.Lessons, Icons.Default.List),
        Pair(Screen.Messenger, Icons.Outlined.ChatBubbleOutline),
        Pair(Screen.Profile, Icons.Default.Person),
        Pair(Screen.Settings, Icons.Default.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val unreadMessagesCount by remember { mutableStateOf(3) }

    NavigationBar(
        containerColor = Color(0xFF6A4CAF),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            val screen = item.first
            val icon = item.second
            NavigationBarItem(
                icon = {
                    Box {
                        Icon(
                            imageVector = icon,
                            contentDescription = screen.route
                        )
                        if (screen == Screen.Messenger && unreadMessagesCount > 0) {
                            Badge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 5.dp, y = (-5).dp),
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = unreadMessagesCount.toString(),
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    }
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    indicatorColor = Color(0xFF4CAF50)
                )
            )
        }
    }
}