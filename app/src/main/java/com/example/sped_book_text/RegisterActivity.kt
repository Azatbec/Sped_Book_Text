package com.example.sped_book_text

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sped_book_text.databinding.ActivityRegisterBinding
import com.example.sped_book_text.database.AppDatabase
import com.example.sped_book_text.model.User
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getDatabase(this)

        binding.btRegist.setOnClickListener {
            val name = binding.etName2.text.toString()
            val surname = binding.etSurname2.text.toString()
            val email = binding.etMail2.text.toString()
            val phone = binding.etphone2.text.toString()
            val password = binding.etPassword2.text.toString()
            val nickname = binding.etNikname2.text.toString()

            val user = User(
                name = name,
                surname = surname,
                email = email,
                phone = phone,
                password = password,
                nickname = nickname
            )

            CoroutineScope(Dispatchers.IO).launch {
                val userDao = db.userDao()
                val existing = userDao.getUserByEmail(email)

                withContext(Dispatchers.Main) {
                    if (existing != null) {
                        Toast.makeText(this@RegisterActivity, "Пользователь уже существует", Toast.LENGTH_SHORT).show()
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            userDao.insertUser(user)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@RegisterActivity, "Регистрация успешна. Войдите в систему.", Toast.LENGTH_SHORT).show()

                                // Возврат на экран авторизации
                                val intent = Intent(this@RegisterActivity, Autoruzation::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}
