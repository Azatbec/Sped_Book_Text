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
import com.example.sped_book_text.databinding.FragmentDostigenia3Binding

class Dostigenia3 : Fragment() {

    private var _binding: FragmentDostigenia3Binding? = null
    private val binding get() = _binding!!

    data class Achievement(val title: String, val isCompleted: Boolean)

    enum class FilterType { ALL, COMPLETED, IN_PROGRESS }

    private val achievements = listOf(
        Achievement("Чтение с пониманием", true),
        Achievement("Постоянство", false),
        Achievement("Фокус внимания", true),
        Achievement("Рекорд скорости", true),
        Achievement("Прогресс каждый день", false),
        Achievement("Логика и анализ", true),
        Achievement("Уверенность", true),
        Achievement("Развитие памяти", false),
        Achievement("Творческое мышление", true),
        Achievement("Критическое мышление", false),
        Achievement("Внимательность к деталям", true),
        Achievement("Эмоциональный интеллект", true),
        Achievement("Самоорганизация", false),
        Achievement("Многозадачность", true),
        Achievement("Мотивация к успеху", true)
    )

    private val iconMap = mapOf(
        "Чтение с пониманием" to R.drawable.reading_icon,
        "Постоянство" to R.drawable.consistency_icon,
        "Фокус внимания" to R.drawable.focus_icon,
        "Рекорд скорости" to R.drawable.speed_icon,
        "Прогресс каждый день" to R.drawable.daily_progress_icon,
        "Логика и анализ" to R.drawable.logic_icon,
        "Уверенность" to R.drawable.confidence_icon,
        "Развитие памяти" to R.drawable.memory_icon,
        "Творческое мышление" to R.drawable.creativity_icon,
        "Критическое мышление" to R.drawable.critical_icon,
        "Внимательность к деталям" to R.drawable.detail_icon,
        "Эмоциональный интеллект" to R.drawable.emotional_icon,
        "Самоорганизация" to R.drawable.self_org_icon,
        "Многозадачность" to R.drawable.multitasking_icon,
        "Мотивация к успеху" to R.drawable.motivation_icon
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDostigenia3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button16.setOnClickListener {
            val intent = Intent(requireContext(), Progress::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.button20.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.button17.setOnClickListener { showAchievements(FilterType.ALL) }
        binding.button18.setOnClickListener { showAchievements(FilterType.COMPLETED) }
        binding.button19.setOnClickListener { showAchievements(FilterType.IN_PROGRESS) }

        showAchievements(FilterType.ALL)
    }

    private fun showAchievements(filter: FilterType) {
        binding.gridLayoutAchievements.removeAllViews()

        val filtered = when (filter) {
            FilterType.ALL -> achievements
            FilterType.COMPLETED -> achievements.filter { it.isCompleted }
            FilterType.IN_PROGRESS -> achievements.filter { !it.isCompleted }
        }

        binding.gridLayoutAchievements.columnCount = 3

        filtered.forEach {
            val view = createAchievementView(it)
            binding.gridLayoutAchievements.addView(view)
        }
    }

    private fun createAchievementView(achievement: Achievement): View {
        val context = requireContext()
        val density = resources.displayMetrics.density

        val itemSizePx = (100 * density).toInt()
        val marginPx = (8 * density).toInt()
        val topMarginPx = (6 * density).toInt()

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = ViewGroup.MarginLayoutParams(
                itemSizePx,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(marginPx, marginPx, marginPx, marginPx)
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

        // Добавляем обработчик клика по достижению
        container.setOnClickListener {
            val achievementTitle = achievement.title
            val fragment = AchievementDetailsFragment.newInstance(achievementTitle)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        container.addView(imageView)
        container.addView(textView)
        return container
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
