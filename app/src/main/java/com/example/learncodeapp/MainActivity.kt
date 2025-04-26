package com.example.learncodeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.learncodeapp.data.AppDatabase
import com.example.learncodeapp.models.Lesson
import com.example.learncodeapp.screens.*
import com.example.learncodeapp.ui.theme.AppTheme
import com.example.learncodeapp.viewmodels.LanguageViewModel
import com.example.learncodeapp.viewmodels.LanguageViewModelFactory
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp


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

    // Определяем, нужно ли показывать BottomNavigationBar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute !in listOf("login", "register")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "login", // Изменили startDestination на login
            modifier = Modifier.padding(padding)
        ) {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("register") {
                RegisterScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.Lessons.route) {
                LessonsScreen(navController = navController)
            }
            composable(Screen.Messenger.route) {
                MessengerScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }
            composable("language_detail/{languageName}") { backStackEntry ->
                val languageName = backStackEntry.arguments?.getString("languageName") ?: ""
                LanguageDetailScreen(
                    navController = navController,
                    languageName = languageName,
                    viewModel = viewModel
                )
            }
            composable("lesson_detail/{lessonId}") { backStackEntry ->
                val lessonId = backStackEntry.arguments?.getString("lessonId")?.toLongOrNull()
                val lessons = navController.previousBackStackEntry?.savedStateHandle?.get<List<Lesson>>("lessons") ?: emptyList()
                val lesson = lessons.find { it.id == lessonId }
                LessonDetailScreen(navController = navController, lesson = lesson)
            }
        }
    }
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

    // Заглушка: общее количество непрочитанных сообщений (в будущем можно получать с сервера)
    val unreadMessagesCount by remember { mutableStateOf(3) } // 2 + 0 + 1 из списка чатов

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
                        // Показываем бейдж для Messenger, если есть непрочитанные сообщения
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