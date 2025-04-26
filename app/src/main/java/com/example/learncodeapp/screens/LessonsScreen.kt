package com.example.learncodeapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learncodeapp.data.AppDatabase
import com.example.learncodeapp.models.Lesson
import com.example.learncodeapp.models.toLesson
import com.example.learncodeapp.models.toLessonEntity
import com.example.learncodeapp.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(navController: NavController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)

    val scope = rememberCoroutineScope()
    var lessons by remember { mutableStateOf<List<Lesson>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") } // Добавляем состояние для поискового запроса

    // Загрузка уроков
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitClient.apiService.getLessons()
                if (response.isSuccessful) {
                    lessons = response.body() ?: emptyList()
                    database.lessonDao().deleteAll()
                    database.lessonDao().insertAll(lessons.map { it.toLessonEntity() })
                } else {
                    errorMessage = "Failed to load lessons: ${response.code()}"
                    lessons = database.lessonDao().getAllLessons().map { it.toLesson() }
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                lessons = database.lessonDao().getAllLessons().map { it.toLesson() }
            } finally {
                isLoading = false
            }
        }
    }

    // Сохраняем уроки в SavedStateHandle для передачи на LessonDetailScreen
    LaunchedEffect(lessons) {
        navController.currentBackStackEntry?.savedStateHandle?.set("lessons", lessons)
    }

    // Фильтруем уроки на основе поискового запроса
    val filteredLessons = lessons.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.language.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Уроки",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Поле поиска
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск уроков") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color(0xFF6A4CAF)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6A4CAF),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF6A4CAF),
                    cursorColor = Color(0xFF6A4CAF)
                )
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Unknown error",
                    color = Color.Red,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        scope.launch {
                            try {
                                val response = RetrofitClient.apiService.getLessons()
                                if (response.isSuccessful) {
                                    lessons = response.body() ?: emptyList()
                                    database.lessonDao().deleteAll()
                                    database.lessonDao().insertAll(lessons.map { it.toLessonEntity() })
                                } else {
                                    errorMessage = "Failed to load lessons: ${response.code()}"
                                    lessons = database.lessonDao().getAllLessons().map { it.toLesson() }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.message}"
                                lessons = database.lessonDao().getAllLessons().map { it.toLesson() }
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Попробовать снова")
                }
            } else {
                if (filteredLessons.isEmpty()) {
                    Text(
                        text = if (searchQuery.isEmpty()) "Нет уроков" else "Уроки не найдены",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredLessons) { lesson ->
                            AnimatedVisibility(visible = true) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate("lesson_detail/${lesson.id}")
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Lesson Icon",
                                            tint = Color(0xFF6A4CAF),
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = lesson.title,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Язык: ${lesson.language}",
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = lesson.content,
                                                fontSize = 16.sp,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            LinearProgressIndicator(
                                                progress = { if (lesson.completed) 1.0f else 0.5f },
                                                modifier = Modifier.fillMaxWidth(),
                                                color = Color(0xFF4CAF50),
                                                trackColor = Color.LightGray
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                            onClick = {
                                                navController.navigate("lesson_detail/${lesson.id}")
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF6A4CAF)
                                            )
                                        ) {
                                            Text("Начать", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}