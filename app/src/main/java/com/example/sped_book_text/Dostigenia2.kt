package com.example.sped_book_text

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sped_book_text.databinding.FragmentDostigenia2Binding

class Dostigenia2 : Fragment() {

    private var _binding: FragmentDostigenia2Binding? = null
    private val binding get() = _binding!!

    data class Achievement(val title: String, val isCompleted: Boolean)

    enum class FilterType { ALL, COMPLETED, IN_PROGRESS }

    private val achievements = listOf(
        Achievement("Уверенность", true),
        Achievement("Лидер", true),
        Achievement("Продвинутый уровень", false),
        Achievement("Покоритель целей", true),
        Achievement("Новичок", false),
        Achievement("Энергия и сила", true),
        Achievement("Стратег", false),
        Achievement("Опытный игрок", true),
        Achievement("Настойчивость", true),
        Achievement("Творческий подход", false),
        Achievement("Скорочтение мастер", true),
        Achievement("Мастер внимательности", false),
        Achievement("Дисциплина", true),
        Achievement("Командный игрок", true),
        Achievement("Позитивное мышление", false),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDostigenia2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Навигация к ProgressActivity
        binding.button16.setOnClickListener {
            val intent = Intent(requireContext(), Progress::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Навигация назад
        binding.button21.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Фильтрация достижений
        binding.button17.setOnClickListener { showAchievements(FilterType.ALL) }
        binding.button18.setOnClickListener { showAchievements(FilterType.COMPLETED) }
        binding.button19.setOnClickListener { showAchievements(FilterType.IN_PROGRESS) }

        // Переход к фрагменту Dostigenia3
        binding.button20.setOnClickListener {
            val fragment = Dostigenia3()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Отображение всех достижений по умолчанию
        showAchievements(FilterType.ALL)
    }

    private fun showAchievements(filter: FilterType) {
        binding.gridLayoutAchievements.removeAllViews()

        // Применение фильтра к достижениям
        val filtered = when (filter) {
            FilterType.ALL -> achievements
            FilterType.COMPLETED -> achievements.filter { it.isCompleted }
            FilterType.IN_PROGRESS -> achievements.filter { !it.isCompleted }
        }

        val context = binding.root.context
        val displayMetrics = resources.displayMetrics
        val density = displayMetrics.density

        val itemSizeDp = 100
        val itemSizePx = (itemSizeDp * density).toInt()
        val marginPx = (8 * density).toInt()
        val topMarginPx = (6 * density).toInt()

        // Сопоставление достижений с изображениями
        val iconMap = mapOf(
            "Уверенность" to R.drawable.confidence_icon,
            "Лидер" to R.drawable.leader_icon,
            "Продвинутый уровень" to R.drawable.advanced_icon,
            "Покоритель целей" to R.drawable.goal_conqueror_icon,
            "Новичок" to R.drawable.beginner_icon,
            "Энергия и сила" to R.drawable.energy_icon,
            "Стратег" to R.drawable.strategist_icon,
            "Опытный игрок" to R.drawable.experienced_icon,
            "Настойчивость" to R.drawable.persistence_icon,
            "Творческий подход" to R.drawable.creativity_icon,
            "Скорочтение мастер" to R.drawable.speed_reading_icon,
            "Мастер внимательности" to R.drawable.attention_master_icon,
            "Дисциплина" to R.drawable.discipline_icon,
            "Командный игрок" to R.drawable.teamplayer_icon,
            "Позитивное мышление" to R.drawable.positivity_icon
        )

        binding.gridLayoutAchievements.columnCount = 3

        // Создание представлений для каждого достижения
        filtered.forEach { achievement ->
            val container = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = ViewGroup.MarginLayoutParams(
                    itemSizePx,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(marginPx, marginPx, marginPx, marginPx)
                }
                isClickable = true
                isFocusable = true

                setOnClickListener {
                    val fragment = AchievementDetailsFragment.newInstance(achievement.title)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }

            val imageView = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(itemSizePx, itemSizePx)
                val iconRes = iconMap[achievement.title]
                if (iconRes != null) {
                    setImageResource(iconRes)
                } else {
                    background = ContextCompat.getDrawable(context, R.drawable.circle_shape_4)
                }
                scaleType = ImageView.ScaleType.FIT_CENTER
            }

            val textView = TextView(context).apply {
                text = achievement.title
                textSize = 14f
                maxLines = 2
                setTextColor(Color.BLACK)
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = topMarginPx
                }
            }

            container.addView(imageView)
            container.addView(textView)
            binding.gridLayoutAchievements.addView(container)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
