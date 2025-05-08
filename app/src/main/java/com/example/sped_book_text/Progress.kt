package com.example.sped_book_text

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class Progress : AppCompatActivity() {

    private lateinit var fragmentContainer: FrameLayout
    private lateinit var button15: Button
    private lateinit var menuButton: Button
    private lateinit var menuButton2: Button
    private lateinit var menuButton3: Button
    private lateinit var menuButton4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progress)

        // Инициализация UI элементов
        initializeViews()

        // Настройка графика прогресса
        setupLineChart()

        // Настройка анимации для ProgressBar
        setupProgressBar()

        // Обработка WindowInsets для безопасной зоны
        handleWindowInsets()

        // Обработчики кнопок
        setupButtons()
    }

    private fun initializeViews() {
        fragmentContainer = findViewById(R.id.fragment_container)
        button15 = findViewById(R.id.button15)
        menuButton = findViewById(R.id.menu_button)
        menuButton2 = findViewById(R.id.menu_button2)
        menuButton3 = findViewById(R.id.menu_button3)
        menuButton4 = findViewById(R.id.menu_button4)

        // Изначально скрываем фрагмент
        fragmentContainer.visibility = FrameLayout.GONE
    }

    private fun setupLineChart() {
        val lineChartProgress = findViewById<LineChart>(R.id.lineChartProgress)

        val entries = listOf(
            Entry(0f, 3f),
            Entry(1f, 5f),
            Entry(2f, 2f),
            Entry(3f, 6f),
            Entry(4f, 4f)
        )

        val dataSet = LineDataSet(entries, "Прогресс")
        dataSet.apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextSize = 8f
            setDrawFilled(true)
            fillAlpha = 100
        }

        val lineData = LineData(dataSet)
        lineChartProgress.apply {
            data = lineData
            description.text = ""
            animateY(1000)
            setTouchEnabled(true)
            setPinchZoom(true)
            legend.isEnabled = true
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
        }
    }

    private fun setupProgressBar() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBarLevel)
        val progressText = findViewById<TextView>(R.id.progressText)

        val animation = AnimationUtils.loadAnimation(this, R.anim.progress_animation_2)
        progressBar.startAnimation(animation)

        val targetProgress = 22
        progressBar.max = 44
        progressBar.progress = 0

        Thread {
            for (i in 1..targetProgress) {
                Thread.sleep(400)
                runOnUiThread {
                    progressBar.progress = i
                    progressText.text = "$i/44"
                }
            }
        }.start()
    }

    private fun handleWindowInsets() {
        val mainView = findViewById<View>(R.id.main)
        mainView?.let { view ->
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }

    private fun setupButtons() {
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        // Функция анимации и перехода
        fun animateAndNavigate(view: View, destination: Class<*>) {
            view.startAnimation(fadeOut)
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val intent = Intent(this@Progress, destination)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }

        // Обработчики кнопок для переходов:
        menuButton.setOnClickListener { animateAndNavigate(it, HomePage::class.java) }
        menuButton2.setOnClickListener { animateAndNavigate(it, Traning::class.java) }
        menuButton4.setOnClickListener { animateAndNavigate(it, Profile::class.java)
        }
        // Переход на фрагмент
        button15.setOnClickListener {
            toggleMenuVisibility(false)
            fragmentContainer.visibility = FrameLayout.VISIBLE
            supportFragmentManager.commit {
                replace(R.id.fragment_container, Dostigenia())
                addToBackStack(null)
            }
        }

        // Восстановление кнопок при возврате из фрагмента
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                toggleMenuVisibility(true)
                fragmentContainer.visibility = FrameLayout.GONE
            }
        }
    }

    private fun toggleMenuVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) Button.VISIBLE else Button.GONE
        button15.visibility = visibility
        menuButton.visibility = visibility
        menuButton2.visibility = visibility
        menuButton3.visibility = visibility
        menuButton4.visibility = visibility
    }
}
