package com.example.sped_book_text

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sped_book_text.database.AppDatabase
import kotlinx.coroutines.*

class Autoruzation : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autoruzation)

        db = AppDatabase.getDatabase(this)

        // Убираем отступы от системных панелей (например, навигационная панель, статусбар)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailField = findViewById<EditText>(R.id.inputName)
        val passwordField = findViewById<EditText>(R.id.inputName2)
        val btnLogin = findViewById<Button>(R.id.button2)
        val btnRegister = findViewById<Button>(R.id.button4)

        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
            } else {
                // Запуск проверки данных на фоне
                CoroutineScope(Dispatchers.IO).launch {
                    val user = db.userDao().getUserByEmail(email)

                    withContext(Dispatchers.Main) {
                        if (user == null || user.password != password) {
                            Toast.makeText(
                                this@Autoruzation,
                                "Неверный email или пароль",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Сохраняем userId в SharedPreferences для последующего использования
                            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
                            prefs.edit().putInt("current_user_id", user.id).apply()

                            try {
                                // Переход в профиль пользователя
                                val intent = Intent(this@Autoruzation, Profile::class.java)
                                startActivity(intent)

                                // Здесь мы НЕ вызываем finish() сразу. Завершаем активность только после загрузки новой активности.
                                // Если нужно, используйте задержку перед завершением активности.
                                // finish()
                            } catch (e: Exception) {
                                // Логируем ошибку, если не удалось запустить активность Profile
                                Toast.makeText(this@Autoruzation, "Ошибка перехода в профиль", Toast.LENGTH_SHORT).show()
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish() // Закрываем текущую активность, чтобы не возвращаться на неё
        }
    }
}
