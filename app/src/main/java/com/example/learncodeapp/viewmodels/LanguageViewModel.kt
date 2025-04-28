package com.example.learncodeapp.viewmodels

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learncodeapp.data.AppDatabase
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(navController: NavController, lessonId: Long?) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val lessonDao = database.lessonDao()
    var lesson by remember { mutableStateOf<com.example.learncodeapp.data.LessonEntity?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(lessonId) {
        if (lessonId != null) {
            coroutineScope.launch {
                lesson = lessonDao.getAllLessons().find { it.id == lessonId }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = lesson?.title ?: "Урок не найден",
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
        if (lesson == null) {
            Text(
                text = "Урок не найден",
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
                Text(
                    text = lesson!!.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = lesson!!.content,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6A4CAF)
                        )
                    ) {
                        Text("Назад", color = Color.White)
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                lessonDao.updateLessonCompletion(lesson!!.id, true)
                                navController.popBackStack()
                            }
                        },
                        enabled = !lesson!!.completed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (lesson!!.completed) Color.Gray else Color(0xFF4CAF50)
                        )
                    ) {
                        Text(
                            text = if (lesson!!.completed) "Завершено" else "Завершить",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}