package com.example.sped_book_text

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserProfileActivity : AppCompatActivity() {

    // Инициализация элементов UI
    private lateinit var tvName: TextView
    private lateinit var tvSurname: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvNickname: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Инициализация элементов
        tvName = findViewById(R.id.tvName)
        tvSurname = findViewById(R.id.tvSurname)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        tvNickname = findViewById(R.id.tvNickname)

        // Получение данных, переданных через Intent
        val name = intent.getStringExtra("NAME")
        val surname = intent.getStringExtra("SURNAME")
        val email = intent.getStringExtra("EMAIL")
        val phone = intent.getStringExtra("PHONE")
        val nickname = intent.getStringExtra("NICKNAME")

        // Отображение данных в UI
        tvName.text = "Name: $name"
        tvSurname.text = "Surname: $surname"
        tvEmail.text = "Email: $email"
        tvPhone.text = "Phone: $phone"
        tvNickname.text = "Nickname: $nickname"
    }
}
