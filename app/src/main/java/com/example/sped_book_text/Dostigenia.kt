package com.example.sped_book_text

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
import com.example.sped_book_text.databinding.FragmentDostigeniaBinding

class Dostigenia : Fragment() {

    private var _binding: FragmentDostigeniaBinding? = null
    private val binding get() = _binding!!

    data class Achievement(val title: String, val isCompleted: Boolean)

    enum class FilterType { ALL, COMPLETED, IN_PROGRESS }

    private val allAchievements = listOf(
        Achievement("Сосредоточенность", true),
        Achievement("Завершитель", true),
        Achievement("Эксперт", false),
        Achievement("Полное завершение", true),
        Achievement("Начальный этап", false),
        Achievement("Ранний старт", false),
        Achievement("Настойчивость", true),
        Achievement("Идеальный баланс", false),
        Achievement("Быстрый старт", true),
        Achievement("Мастер концентрации", true),
        Achievement("Ветеран практики", false),
        Achievement("Непоколебимость", true),
        Achievement("Уверенное продвижение", false),
        Achievement("Цель достигнута", true),
        Achievement("Этап завершён", false)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDostigeniaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button16.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.button17.setOnClickListener { showAchievements(FilterType.ALL) }
        binding.button18.setOnClickListener { showAchievements(FilterType.COMPLETED) }
        binding.button19.setOnClickListener { showAchievements(FilterType.IN_PROGRESS) }

        binding.button20.setOnClickListener {
            val fragment = Dostigenia2()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        showAchievements(FilterType.ALL)
    }

    private fun showAchievements(filter: FilterType) {
        binding.gridLayoutAchievements.removeAllViews()

        val filtered = when (filter) {
            FilterType.ALL -> allAchievements
            FilterType.COMPLETED -> allAchievements.filter { it.isCompleted }
            FilterType.IN_PROGRESS -> allAchievements.filter { !it.isCompleted }
        }

        val context = binding.root.context
        val displayMetrics = resources.displayMetrics
        val density = displayMetrics.density

        val itemSizeDp = 100
        val itemSizePx = (itemSizeDp * density).toInt()
        val marginPx = (8 * density).toInt()
        val topMarginPx = (6 * density).toInt()

        val iconMap = mapOf(
            "Сосредоточенность" to R.drawable.focus_icon,
            "Завершитель" to R.drawable.complete_icon,
            "Эксперт" to R.drawable.expert_icon,
            "Полное завершение" to R.drawable.full_finish_icon,
            "Начальный этап" to R.drawable.start_stage_icon,
            "Ранний старт" to R.drawable.early_start_icon,
            "Настойчивость" to R.drawable.persistence_icon,
            "Идеальный баланс" to R.drawable.balance_icon,
            "Быстрый старт" to R.drawable.fast_start_icon,
            "Мастер концентрации" to R.drawable.master_focus_icon,
            "Ветеран практики" to R.drawable.veteran_icon,
            "Непоколебимость" to R.drawable.steadfast_icon,
            "Уверенное продвижение" to R.drawable.progress_icon,
            "Цель достигнута" to R.drawable.goal_icon,
            "Этап завершён" to R.drawable.stage_done_icon
        )

        binding.gridLayoutAchievements.columnCount = 3

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

            // Обработчик клика для перехода на фрагмент с подробным описанием
            container.setOnClickListener {
                val fragment = AchievementDetailsFragment.newInstance(achievement.title)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            binding.gridLayoutAchievements.addView(container)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
