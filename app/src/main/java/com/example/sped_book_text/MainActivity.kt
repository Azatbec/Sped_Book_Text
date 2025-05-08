package com.example.sped_book_text

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var authButton: Button // Кнопка перехода в Authorization
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация кнопок и контейнера фрагмента
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        authButton = findViewById(R.id.button2) // Пример ID кнопки для авторизации
        fragmentContainer = findViewById(R.id.fragment_container)

        // Инициализация SharedPreferences для хранения данных
        sharedPreferences = getSharedPreferences("UserProgress", MODE_PRIVATE)

        // Изначально фрагмент скрыт
        fragmentContainer.visibility = FrameLayout.GONE

        // Переход в Fragment
        button3.setOnClickListener {
            button1.visibility = Button.GONE
            button2.visibility = Button.GONE
            button3.visibility = Button.GONE
            authButton.visibility = Button.GONE

            fragmentContainer.visibility = FrameLayout.VISIBLE
            supportFragmentManager.commit {
                replace(R.id.fragment_container, BlankFragment())
                addToBackStack(null)
            }
        }

        // Переход в Authorization Activity
        authButton.setOnClickListener {
            val intent = Intent(this, Autoruzation::class.java)
            startActivity(intent)
        }

        // Переход в HomePage и добавление опыта
        button1.setOnClickListener {
            addExperience(25) // Добавление очков за вход на HomePage
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    // Метод для добавления опыта
    private fun addExperience(points: Int) {
        // Получаем текущий опыт из SharedPreferences
        val currentExp = sharedPreferences.getInt("currentExp", 0)
        val newExp = currentExp + points

        // Сохраняем обновленный опыт
        val editor = sharedPreferences.edit()
        editor.putInt("currentExp", newExp)

        // Если опыт превышает порог, увеличиваем уровень
        val expToNextLevel = sharedPreferences.getInt("expToNextLevel", 100)
        if (newExp >= expToNextLevel) {
            val newLevel = sharedPreferences.getInt("level", 1) + 1
            editor.putInt("level", newLevel)
            editor.putInt("currentExp", newExp - expToNextLevel) // Оставляем оставшийся опыт
        }

        editor.apply() // Применяем изменения
    }

    // Метод для отображения кнопок
    fun showButtons() {
        button1.visibility = Button.VISIBLE
        button2.visibility = Button.VISIBLE
        button3.visibility = Button.VISIBLE
        authButton.visibility = Button.VISIBLE
    }
}
