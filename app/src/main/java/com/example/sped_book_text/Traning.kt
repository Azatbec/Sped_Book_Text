package com.example.sped_book_text

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit

class Traning : AppCompatActivity() {

    private lateinit var menuButton: View
    private lateinit var menuButton2: View
    private lateinit var menuButton3: View
    private lateinit var menuButton4: View
    private lateinit var nextButton: View
    private lateinit var progressButton: View
    private lateinit var schulteButton: View
    private lateinit var sglagivanieButton: ImageView
    private lateinit var metronomButton: ImageView  // Новый элемент для кнопки metronom

    private lateinit var menuContainer: View
    private lateinit var fragmentContainer: View

    private lateinit var fadeOut: Animation
    private lateinit var fadeIn: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_traning)

        initViews()
        applyWindowInsets()
        loadAnimations()
        setupListeners()
        setupBackStackListener()

        fragmentContainer.visibility = View.GONE
    }

    private fun initViews() {
        menuButton = findViewById(R.id.menu_button)
        menuButton2 = findViewById(R.id.menu_button2)
        menuButton3 = findViewById(R.id.menu_button3)
        menuButton4 = findViewById(R.id.menu_button4)
        nextButton = findViewById(R.id.button13)
        progressButton = findViewById(R.id.menu_button3)
        schulteButton = findViewById(R.id.shult)
        sglagivanieButton = findViewById(R.id.sglagivanie)
        metronomButton = findViewById(R.id.metronom)  // Инициализация нового элемента

        menuContainer = findViewById(R.id.menu_container)
        fragmentContainer = findViewById(R.id.fragment_container)
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadAnimations() {
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
    }

    private fun setupListeners() {
        menuButton.setOnClickListener {
            navigateWithFade(HomePage::class.java)
        }

        nextButton.setOnClickListener {
            navigateWithFade(Traning2::class.java)
        }

        progressButton.setOnClickListener {
            navigateWithFade(Progress::class.java)
        }
        menuButton4.setOnClickListener {
            navigateWithFade(Profile::class.java)
        }
        schulteButton.setOnClickListener {
            hideMenu() // Скрываем элементы перед показом фрагмента
            fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.commit {
                replace(R.id.fragment_container, SchulteTableFragment())
                addToBackStack(null)
            }
        }

        sglagivanieButton.setOnClickListener {
            hideMenu() // Скрываем элементы перед показом фрагмента
            fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MetronomeReadingFragment())
                addToBackStack(null)
            }
        }

        // Добавление слушателя для нового элемента ImageView
        metronomButton.setOnClickListener {
            hideMenu() // Скрываем элементы перед показом фрагмента
            fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.commit {
                replace(R.id.fragment_container, SmoothingReadingFragment())  // Переход в SmoothingReadingFragment
                addToBackStack(null)
            }
        }
    }

    private fun setupBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                showMenu() // Показываем меню при возврате
                fragmentContainer.visibility = View.GONE
            } else {
                fragmentContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateWithFade(destination: Class<*>) {
        menuContainer.startAnimation(fadeOut)
        startActivity(Intent(this@Traning, destination))
        overridePendingTransition(0, 0)
        toggleMenu(false)
    }

    private fun toggleMenu(visible: Boolean) {
        if (visible) {
            menuContainer.visibility = View.VISIBLE
            menuContainer.startAnimation(fadeIn)
        } else {
            menuContainer.startAnimation(fadeOut)
            menuContainer.postDelayed({
                menuContainer.visibility = View.GONE
            }, fadeOut.duration)
        }
    }

    // Метод скрытия всех кнопок
    private fun hideMenu() {
        menuButton.visibility = View.GONE
        menuButton2.visibility = View.GONE
        menuButton3.visibility = View.GONE
        menuButton4.visibility = View.GONE
        nextButton.visibility = View.GONE
        progressButton.visibility = View.GONE
        schulteButton.visibility = View.GONE
        sglagivanieButton.visibility = View.GONE
        metronomButton.visibility = View.GONE  // Прячем кнопку metronom
        menuContainer.visibility = View.GONE
    }

    // Метод отображения всех кнопок
    private fun showMenu() {
        menuButton.visibility = View.VISIBLE
        menuButton2.visibility = View.VISIBLE
        menuButton3.visibility = View.VISIBLE
        menuButton4.visibility = View.VISIBLE
        nextButton.visibility = View.VISIBLE
        progressButton.visibility = View.VISIBLE
        schulteButton.visibility = View.VISIBLE
        sglagivanieButton.visibility = View.VISIBLE
        metronomButton.visibility = View.VISIBLE  // Отображаем кнопку metronom
        menuContainer.visibility = View.VISIBLE
    }
}
