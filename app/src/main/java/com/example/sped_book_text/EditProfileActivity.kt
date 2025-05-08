package com.example.sped_book_text

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.sped_book_text.database.AppDatabase
import com.example.sped_book_text.model.User
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextLastname: EditText
    private lateinit var editTextFirstname: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var saveButton: Button

    // Создание базы данных, если еще не создана
    private val userDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        // Инициализация UI элементов
        editTextLastname = findViewById(R.id.lastname)
        editTextFirstname = findViewById(R.id.firstname)
        editTextEmail = findViewById(R.id.editTextEmail)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val lastname = editTextLastname.text.toString()
            val firstname = editTextFirstname.text.toString()
            val email = editTextEmail.text.toString()

            if (lastname.isEmpty() || firstname.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                // Сохранение данных в базу
                saveUserProfile(lastname, firstname, email)
            }
        }
    }

    private fun saveUserProfile(lastname: String, firstname: String, email: String) {
        // Использование корутины для сохранения данных в базу
        lifecycleScope.launch {
            try {
                // Пример сохранения данных в базе данных
                val userDao = userDatabase.userDao()
                val user = User(
                    name = lastname, surname = firstname, email = email,
                    id = 0, // Нужно указать реальный ID, если используется автоинкремент
                    phone = "", // Укажите реальный номер телефона, если это необходимо
                    password = "", // Здесь необходимо использовать реальный пароль
                    nickname = "" // Используйте реальный никнейм, если это нужно
                )
                // Вставка пользователя в базу данных
                userDao.insertUser(user)

                // Отображаем Toast после успешного сохранения
                Toast.makeText(this@EditProfileActivity, "Профиль обновлён!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Обработка ошибок
                Toast.makeText(this@EditProfileActivity, "Ошибка при сохранении профиля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
