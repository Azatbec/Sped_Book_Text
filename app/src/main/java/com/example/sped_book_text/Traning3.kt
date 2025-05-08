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

class Traning3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Включаем поддержку полноэкранного режима
        setContentView(R.layout.activity_traning3)

        // Настройка отступов для системных панелей (статусбар, навбар)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Загрузка анимации "затухания"
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        // Функция анимации и навигации на следующую активность
        fun animateAndNavigate(view: View, destination: Class<*>) {
            view.startAnimation(fadeOut)
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val intent = Intent(this@Traning3, destination)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }

        // Кнопка для перехода на следующую активность (например, Traning4)
        val button13 = findViewById<View>(R.id.button13)
        button13?.setOnClickListener {
            animateAndNavigate(it, Traning4::class.java)
        }
        val button14 = findViewById<View>(R.id.button14)
        button14?.setOnClickListener {
            animateAndNavigate(it, Traning2::class.java)
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
