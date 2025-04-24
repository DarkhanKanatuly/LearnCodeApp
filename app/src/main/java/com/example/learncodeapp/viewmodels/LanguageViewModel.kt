package com.example.learncodeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learncodeapp.R
import com.example.learncodeapp.data.AppDatabase
import com.example.learncodeapp.data.LanguageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LanguageViewModel(private val database: AppDatabase) : ViewModel() {

    val languages: Flow<List<LanguageEntity>> = database.languageDao().getAllLanguages()

    init {
        initializeDatabase()
    }

    private fun initializeDatabase() {
        viewModelScope.launch {
            val existingLanguages = database.languageDao().getAllLanguages().firstOrNull()
            if (existingLanguages.isNullOrEmpty()) {
                val initialLanguages = listOf(
                    LanguageEntity(
                        name = "Python",
                        iconResId = R.drawable.ic_python,
                        description = "Python — это простой и мощный язык программирования, идеально подходящий для начинающих. Он используется в веб-разработке, анализе данных, машинном обучении и автоматизации.",
                        videoId = "dQw4w9WgXcQ",
                        thumbnailUrl = "https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg"
                    ),
                    LanguageEntity(
                        name = "C#",
                        iconResId = R.drawable.ic_csharp,
                        description = "C# — это объектно-ориентированный язык от Microsoft, часто используемый для разработки игр (Unity), приложений Windows и веб-сервисов.",
                        videoId = "dQw4w9WgXcQ",
                        thumbnailUrl = "https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg"
                    ),
                    LanguageEntity(
                        name = "C++",
                        iconResId = R.drawable.ic_cpp,
                        description = "C++ — это производительный язык, используемый в системном программировании, разработке игр и высокопроизводительных приложений, таких как движки и симуляторы.",
                        videoId = "dQw4w9WgXcQ",
                        thumbnailUrl = "https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg"
                    )
                )
                database.languageDao().insertLanguages(initialLanguages)
            }
        }
    }
}