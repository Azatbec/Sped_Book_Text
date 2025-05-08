package com.example.sped_book_text

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class HomePage : AppCompatActivity() {

    private var level = 1
    private var currentExp = 0
    private var expToNextLevel = 100

    private lateinit var progressBar: ProgressBar
    private lateinit var levelTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        sharedPreferences = getSharedPreferences("UserProgress", Context.MODE_PRIVATE)

        // Загрузка сохраненных данных
        level = sharedPreferences.getInt("level", 1)
        currentExp = sharedPreferences.getInt("currentExp", 0)
        expToNextLevel = sharedPreferences.getInt("expToNextLevel", 100)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textView = findViewById<TextView>(R.id.textViewTitle)
        textView.text = "Количество пройденных тренировок"

        // ===== Настройка LineChart =====
        val lineChart = findViewById<LineChart>(R.id.lineChart)
        val entries = listOf(
            Entry(0f, 2f),
            Entry(1f, 4f),
            Entry(2f, 1f),
            Entry(3f, 7f),
            Entry(4f, 3f)
        )
        val dataSet = LineDataSet(entries, "Прогресс")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 8f
        dataSet.setDrawFilled(true)
        dataSet.fillAlpha = 100

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.description.text = ""
        lineChart.animateY(1000)
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.legend.isEnabled = true
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(true)
        lineChart.axisRight.isEnabled = false

        // ===== Прогресс уровень =====
        progressBar = findViewById(R.id.progressBarLevel)
        levelTextView = findViewById(R.id.levelTextView)

        // Загрузка прогресса
        updateProgressBar()

        val progressFrame = findViewById<View>(R.id.menu_progers9)
        val frameAnimation = AnimationUtils.loadAnimation(this, R.anim.progress_animation)
        progressFrame.startAnimation(frameAnimation)

        // ===== Кнопки меню =====
        val button1 = findViewById<Button>(R.id.menu_button)
        val button2 = findViewById<Button>(R.id.menu_button2)
        val button3 = findViewById<Button>(R.id.menu_button3)
        val button4 = findViewById<Button>(R.id.menu_button4)

        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        // Функция для анимации и перехода
        fun animateAndNavigate(view: View, destination: Class<*>) {
            view.startAnimation(fadeOut)
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val intent = Intent(this@HomePage, destination)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }

        button2.setOnClickListener {
            animateAndNavigate(it, Traning::class.java)
        }

        button3.setOnClickListener {
            animateAndNavigate(it, Progress::class.java)
        }
        /*
        button4.setOnClickListener {
            animateAndNavigate(it, Autoruzation::class.java)
        }*/
        button4.setOnClickListener {
            animateAndNavigate(it, Profile::class.java)
        }

        val button7 = findViewById<Button>(R.id.button7)

        button7.setOnClickListener {
            animateAndNavigate(it, Traning::class.java)
        }

        val button12 = findViewById<Button>(R.id.button12)
        button12.setOnClickListener {
            animateAndNavigate(it, Traning::class.java)
        }

    }

    private fun addExperience(points: Int) {
        currentExp += points
        if (currentExp >= expToNextLevel) {
            levelUp()
        }
        updateProgressBar()

        // Сохранение данных
        val editor = sharedPreferences.edit()
        editor.putInt("currentExp", currentExp)
        editor.putInt("level", level)
        editor.putInt("expToNextLevel", expToNextLevel)
        editor.apply()
    }

    private fun levelUp() {
        level++
        currentExp -= expToNextLevel
        expToNextLevel += 20
    }

    private fun updateProgressBar() {
        progressBar.max = expToNextLevel

        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, currentExp)
        animator.duration = 1000
        animator.interpolator = DecelerateInterpolator()
        animator.start()

        levelTextView.text = "Уровень: ${getLevelName(level)} (уровень $level)"
    }

    private fun getLevelName(level: Int): String {
        return when (level) {
            in 0..4 -> "Новичок"
            in 5..9 -> "Ученик"
            in 10..19 -> "Продвинутый"
            in 20..49 -> "Профессионал"
            else -> "Мастер"
        }
    }

    // Проверка для достижения
    private fun checkForAchievement() {
        if (currentExp == 0) {
            // Например, первое достижение
            Toast.makeText(this, "Поздравляю с первым достижением!", Toast.LENGTH_SHORT).show()
            addExperience(25) // Добавляем 25 очков за первое достижение
        }
    }


}
