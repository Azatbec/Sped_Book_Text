package com.example.sped_book_text

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sped_book_text.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

    private var _binding: FragmentTrainingBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_LEVEL = "level"
        private const val ARG_CHECKBOX_ID = "checkboxId"

        fun newInstance(level: Int, checkboxId: Int?): TrainingFragment {
            val fragment = TrainingFragment()
            val args = Bundle().apply {
                putInt(ARG_LEVEL, level)
                putInt(ARG_CHECKBOX_ID, checkboxId ?: -1)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение настроек из аргументов
        val level = arguments?.getInt(ARG_LEVEL, 1) ?: 1
        val checkboxId = arguments?.getInt(ARG_CHECKBOX_ID, -1).takeIf { it != -1 }

        // Кнопка "Назад"
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Запуск тренировки
        if (checkboxId == null) {
            Toast.makeText(requireContext(), "Выберите режим для тренировки", Toast.LENGTH_SHORT).show()
            binding.trainingTextView.text = "Выберите режим в настройках"
        } else {
            startTraining(level, checkboxId)
        }
    }

    private fun startTraining(level: Int, checkboxId: Int) {
        val text = binding.trainingTextView.text.toString()
        val words = text.split(" ")
        val speed = (11 - level) * 100L // Скорость в миллисекундах (высокий уровень = быстрее)

        when (checkboxId) {
            R.id.checkbox_frame_mode -> {
                // Покадровый режим: показываем по одному слову
                var index = 0
                val handler = Handler(Looper.getMainLooper())
                val runnable = object : Runnable {
                    override fun run() {
                        if (index < words.size) {
                            binding.trainingTextView.text = words[index]
                            index++
                            handler.postDelayed(this, speed)
                        } else {
                            binding.trainingTextView.text = text
                        }
                    }
                }
                handler.post(runnable)
            }
            R.id.checkbox_smooth_scroll -> {
                // Плавная прокрутка: текст движется вверх
                val animator = ValueAnimator.ofFloat(0f, -binding.trainingTextView.height.toFloat())
                animator.duration = speed * 50 // Длительность зависит от уровня
                animator.addUpdateListener { animation ->
                    binding.trainingTextView.translationY = animation.animatedValue as Float
                }
                animator.start()
            }
            R.id.checkbox_central_highlight -> {
                // Центральное выделение: подсветка центрального слова
                var index = 0
                val handler = Handler(Looper.getMainLooper())
                val runnable = object : Runnable {
                    override fun run() {
                        if (index < words.size) {
                            val spannable = SpannableString(text)
                            val start = words.take(index).joinToString(" ").length + if (index > 0) 1 else 0
                            val end = start + words[index].length
                            spannable.setSpan(
                                BackgroundColorSpan(ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)),
                                start,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            binding.trainingTextView.text = spannable
                            index++
                            handler.postDelayed(this, speed)
                        } else {
                            binding.trainingTextView.text = text
                        }
                    }
                }
                handler.post(runnable)
            }
            R.id.checkbox_vertical_focus -> {
                // Фокус по вертикали: текст движется вверх с акцентом на центре
                val animator = ValueAnimator.ofFloat(binding.trainingTextView.height.toFloat(), -binding.trainingTextView.height.toFloat())
                animator.duration = speed * 50
                animator.addUpdateListener { animation ->
                    binding.trainingTextView.translationY = animation.animatedValue as Float
                }
                animator.start()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}