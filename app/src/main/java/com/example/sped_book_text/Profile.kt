package com.example.sped_book_text

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.sped_book_text.database.AppDatabase
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.*

class Profile : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var imageView: ImageView
    private val REQUEST_CODE_PICK_IMAGE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Инициализация базы данных
        db = AppDatabase.getDatabase(this)

        // Получение ссылок на элементы интерфейса
        val textViewName = findViewById<TextView>(R.id.textView3)
        val textViewLastName = findViewById<TextView>(R.id.textView8)
        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val taskTextView = findViewById<TextView>(R.id.textViewTask)
        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)
        imageView = findViewById(R.id.imageView2)

        // Загрузка данных пользователя
        loadUser(textViewName, textViewLastName)

        // Отображение диаграммы
        setupPieChart(pieChart)

        // Отображение задания
        loadOrGenerateTask(taskTextView)

        // Навигация
        findViewById<Button>(R.id.menu_button).setOnClickListener { navigateTo(HomePage::class.java) }
        findViewById<Button>(R.id.menu_button2).setOnClickListener { navigateTo(Traning::class.java) }
        findViewById<Button>(R.id.menu_button3).setOnClickListener { navigateTo(Progress::class.java) }

        // Переход в достижения
        findViewById<Button>(R.id.button22).setOnClickListener {
            hideProfileUI()
            fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.commit {
                replace(R.id.fragment_container, Dostigenia())
                addToBackStack(null)
            }
        }

        // Возврат из достижений
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                showProfileUI()
                fragmentContainer.visibility = View.GONE
            }
        }

        // Меню настроек
        findViewById<Button>(R.id.button6).setOnClickListener { showSettingsMenu(it) }

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /** Загрузка данных пользователя по сохранённому ID в SharedPreferences */
    private fun loadUser(nameView: TextView, surnameView: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
            val currentUserId = prefs.getInt("current_user_id", -1)

            if (currentUserId != -1) {
                val user = db.userDao().getUserById(currentUserId)

                withContext(Dispatchers.Main) {
                    if (user != null) {
                        nameView.text = user.name
                        surnameView.text = user.surname
                    } else {
                        nameView.text = "Имя не найдено"
                        surnameView.text = "Фамилия не найдена"
                    }
                }
            } else {
                // Обработка случая, если currentUserId == -1
                runOnUiThread {
                    Toast.makeText(this@Profile, "Ошибка загрузки данных пользователя", Toast.LENGTH_SHORT).show()
                    navigateTo(Autoruzation::class.java)
                }
            }
        }
    }

    private fun setupPieChart(pieChart: PieChart) {
        val entries = listOf(
            PieEntry(40f, "Категория 1"),
            PieEntry(30f, "Категория 2"),
            PieEntry(20f, "Категория 3"),
            PieEntry(10f, "Категория 4")
        )
        val dataSet = PieDataSet(entries, "Категории").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }
        pieChart.apply {
            data = PieData(dataSet)
            setUsePercentValues(true)
            isDrawHoleEnabled = false
            description.isEnabled = false
            invalidate()
        }
    }

    private fun loadOrGenerateTask(taskView: TextView) {
        val prefs = getSharedPreferences("profile_tasks", MODE_PRIVATE)
        val lastUpdate = prefs.getLong("last_update", 0L)
        val currentTime = System.currentTimeMillis()
        val THIRTY_HOURS_MS = 30 * 60 * 60 * 1000L

        val task = if (currentTime - lastUpdate >= THIRTY_HOURS_MS) {
            getRandomTask().also {
                prefs.edit()
                    .putString("current_task", it)
                    .putLong("last_update", currentTime)
                    .apply()
            }
        } else {
            prefs.getString("current_task", "Задание недоступно") ?: "Задание недоступно"
        }

        taskView.text = task
    }

    private fun getRandomTask(): String {
        return listOf(
            "Прочти 5 страниц книги",
            "Сделай упражнение на внимание",
            "Попробуй новую технику скорочтения",
            "Пройди тренировку Шульте",
            "Сделай перерыв и отдохни 10 минут"
        ).random()
    }

    private fun showSettingsMenu(view: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.profile_settings_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit_profile -> {
                    startActivity(Intent(this, EditProfileActivity::class.java))
                    true
                }
                R.id.menu_change_avatar -> {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                        type = "image/*"
                    }
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
                    true
                }
                R.id.menu_logout -> {
                    getSharedPreferences("user_session", MODE_PRIVATE).edit().clear().apply()
                    val intent = Intent(this, Autoruzation::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                imageView.setImageURI(uri)

                val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
                val currentUserId = prefs.getInt("current_user_id", -1)

                if (currentUserId != -1) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.userDao().updateAvatarUri(currentUserId, uri.toString())
                    }
                }
            }
        }
    }

    private fun hideProfileUI() {
        listOf(
            R.id.button22, R.id.pieChart, R.id.textView3, R.id.textView8,
            R.id.menu_button, R.id.menu_button2, R.id.menu_button3,
            R.id.menu_button4, R.id.button6, R.id.textViewTask
        ).forEach { id -> findViewById<View>(id)?.visibility = View.GONE }
    }

    private fun showProfileUI() {
        listOf(
            R.id.button22, R.id.pieChart, R.id.textView3, R.id.textView8,
            R.id.menu_button, R.id.menu_button2, R.id.menu_button3,
            R.id.menu_button4, R.id.button6, R.id.textViewTask
        ).forEach { id -> findViewById<View>(id)?.visibility = View.VISIBLE }
    }

    private fun navigateTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
    }
}
