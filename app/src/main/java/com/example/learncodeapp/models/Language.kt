package com.example.learncodeapp.models

import com.example.learncodeapp.data.LanguageEntity

data class Language(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String
)

fun Language.toLanguageEntity(): LanguageEntity {
    return LanguageEntity(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl
    )
}

fun LanguageEntity.toLanguage(): Language {
    return Language(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl
    )
}