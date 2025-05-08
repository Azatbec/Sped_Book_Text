package com.example.sped_book_text

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TrainingResultFragment : Fragment() {

    private var score: Int = 0
    private var level: Int = 0
    private var timeLeft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            score = it.getInt(ARG_SCORE)
            level = it.getInt(ARG_LEVEL)
            timeLeft = it.getString(ARG_TIME_LEFT, "")
        }

        Log.d("TrainingResult", "Получены данные: Уровень=$level, Очки=$score, Время=$timeLeft")
        saveScoreForLevel(level, score)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_training_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val levelName = when (level) {
            0 -> "Простой"
            1 -> "Средний"
            2 -> "Сложный"
            else -> "Очень Сложный "
        }

        view.findViewById<TextView>(R.id.resultScore).text = "Очки: $score"
        view.findViewById<TextView>(R.id.resultTime).text = "Время: $timeLeft"
        view.findViewById<TextView>(R.id.resultLevel).text = "Уровень: $levelName"

        view.findViewById<View>(R.id.buttonBack).setOnClickListener {
            val intent = Intent(activity, Traning::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun saveScoreForLevel(level: Int, score: Int) {
        val prefs = requireContext().getSharedPreferences("training_results", 0)
        val key = "level_$level"
        val previous = prefs.getInt(key, 0)

        if (score > previous) {
            prefs.edit().putInt(key, score).apply()
            Log.d("TrainingResult", "Новый рекорд для уровня $level: $score (предыдущий: $previous)")
        } else {
            Log.d("TrainingResult", "Результат $score не превысил рекорд $previous для уровня $level")
        }
    }

    companion object {
        private const val ARG_SCORE = "score"
        private const val ARG_LEVEL = "level"
        private const val ARG_TIME_LEFT = "time_left"

        fun newInstance(score: Int, level: Int, timeLeft: String) = TrainingResultFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SCORE, score)
                putInt(ARG_LEVEL, level)
                putString(ARG_TIME_LEFT, timeLeft)
            }
        }
    }
}
