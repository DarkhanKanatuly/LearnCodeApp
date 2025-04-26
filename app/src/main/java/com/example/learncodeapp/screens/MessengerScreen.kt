package com.example.learncodeapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learncodeapp.R
import java.text.SimpleDateFormat
import java.util.*

// Модель данных для чата
data class Chat(
    val id: Long,
    val userName: String,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int = 0, // Количество непрочитанных сообщений
    val avatarResId: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerScreen(navController: NavController) {
    // пока что заглушка: список чатов
    val chats = remember {
        listOf(
            Chat(
                id = 1,
                userName = "Айка",
                lastMessage = "Привет! Малснба?",
                timestamp = System.currentTimeMillis() - 5 * 60 * 1000,
                unreadCount = 2,
                avatarResId = R.drawable.ic_profile
            ),
            Chat(
                id = 2,
                userName = "Михаил",
                lastMessage = "Давай обсудим проект, а то я тупой",
                timestamp = System.currentTimeMillis() - 60 * 60 * 1000,
                unreadCount = 0,
                avatarResId = R.drawable.ic_profile
            ),
            Chat(
                id = 3,
                userName = "Эльмира",
                lastMessage = "Че на завтра задали, айтш!",
                timestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000,
                unreadCount = 1,
                avatarResId = R.drawable.ic_profile
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Сообщения",
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
        if (chats.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Нет сообщений",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chats) { chat ->
                    ChatItem(
                        chat = chat,
                        onClick = {
                            navController.navigate("messengerDetailScreen/${chat.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар
            if (chat.avatarResId != null) {
                Image(
                    painter = painterResource(id = chat.avatarResId),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Заглушка, если аватар не указан
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    color = Color(0xFF6A4CAF)
                ) {
                    Text(
                        text = chat.userName.first().toString(),
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chat.userName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatTimestamp(chat.timestamp),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

// Форматирование времени
fun formatTimestamp(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val diff = currentTime - timestamp
    return when {
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} мин назад" // Менее часа
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} ч назад" // Менее суток
        else -> SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(Date(timestamp))
    }
}