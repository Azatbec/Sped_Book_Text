package com.example.sped_book_text.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val password: String,
    val nickname: String,
    val avatarUri: String? = null
)
