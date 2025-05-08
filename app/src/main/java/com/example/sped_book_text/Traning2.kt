package com.example.sped_book_text

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Traning2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Включаем поддержку полноэкранного режима
        setContentView(R.layout.activity_traning2)

        // Обработчик WindowInsets для корректного отображения на экранах с вырезами
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Загружаем анимацию (если нужно добавить анимацию)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        // Функция для анимации и перехода
        fun animateAndNavigate(view: View, destination: Class<*>) {
            view.startAnimation(fadeOut)
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    // После завершения анимации переход
                    val intent = Intent(this@Traning2, destination)
                    startActivity(intent)
                    // Плавный переход между активностями
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }

        // Пример кнопки для перехода
        val button13 = findViewById<View>(R.id.button13)
        button13?.setOnClickListener {
            animateAndNavigate(it, Traning3::class.java) // Переход на Traning3 с анимацией
        }

        val button14 = findViewById<View>(R.id.button14)
        button14?.setOnClickListener {
            animateAndNavigate(it, Traning::class.java) // Переход на Traning3 с анимацией
        }

        val menu_button = findViewById<View>(R.id.menu_button)
        menu_button?.setOnClickListener {
            animateAndNavigate(it, HomePage::class.java) // Переход на Traning3 с анимацией
        }

        val menu_button3 = findViewById<View>(R.id.menu_button3)
        menu_button3?.setOnClickListener {
            animateAndNavigate(it, Progress::class.java) // Переход на HomePage с анимацией
        }
    }
}
