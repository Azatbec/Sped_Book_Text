package com.example.sped_book_text

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.fragment.findNavController

class ExerciseFragment : Fragment() {

    private var level: Int = 0
    private lateinit var exerciseContent: FrameLayout
    private lateinit var timerText: TextView
    private lateinit var taskText: TextView
    private lateinit var scoreText: TextView
    private lateinit var resultText: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var timeInMillis: Long = 0

    private var expectedNumber = 1
    private var score = 0
    private var maxNumber = 0
    private var timeLeft = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            level = it.getInt(ARG_LEVEL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskText = view.findViewById(R.id.taskText)
        timerText = view.findViewById(R.id.timerText)
        scoreText = view.findViewById(R.id.scoreText)
        resultText = view.findViewById(R.id.resyltText)
        exerciseContent = view.findViewById(R.id.exerciseContent)

        val tableSize = when (level) {
            in 0..3 -> 3
            in 4..7 -> 4
            in 8..10 -> 5
            else -> 3
        }

        timeInMillis = when (level) {
            in 0..3 -> 60000L
            in 4..7 -> 45000L
            in 8..10 -> 30000L
            else -> 60000L
        }

        maxNumber = tableSize * tableSize
        expectedNumber = 1
        score = 0

        taskText.text = "Найдите число $expectedNumber"
        scoreText.text = "Очки: $score"
        showBestScore()

        startTimer()
        generateSchulteTable(tableSize)
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                val seconds = millisUntilFinished / 1000
                timerText.text = "$seconds сек"
            }

            override fun onFinish() {
                timerText.text = "Время вышло!"
                endExercise()
            }
        }.start()
    }

    private fun generateSchulteTable(size: Int) {
        val gridLayout = GridLayout(requireContext()).apply {
            rowCount = size
            columnCount = size
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        val numbers = (1..(size * size)).shuffled()

        for (number in numbers) {
            val button = AppCompatButton(requireContext()).apply {
                text = number.toString()
                textSize = 18f
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(4, 4, 4, 4)
                }

                setOnClickListener {
                    if (number == expectedNumber) {
                        score++
                        scoreText.text = "Очки: $score"
                        expectedNumber++
                        taskText.text = if (expectedNumber <= maxNumber)
                            "Найдите число $expectedNumber"
                        else
                            "Задание завершено!"

                        it.isEnabled = false
                        (it as AppCompatButton).alpha = 0.4f

                        if (expectedNumber > maxNumber) {
                            countDownTimer.cancel()
                            endExercise()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Неверно! Ищем $expectedNumber", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            gridLayout.addView(button)
        }

        exerciseContent.removeAllViews()
        exerciseContent.addView(gridLayout)
    }

    private fun endExercise() {
        saveScoreForLevel(level, score)

        val bundle = Bundle().apply {
            putInt("score", score)
            putInt("level", level)
            putString("time_left", "${timeLeft / 1000} сек")
        }

        val fragment = TrainingResultFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun saveScoreForLevel(level: Int, score: Int) {
        val prefs = requireContext().getSharedPreferences("training_results", Context.MODE_PRIVATE)
        val bestScore = prefs.getInt("level_$level", 0)
        if (score > bestScore) {
            prefs.edit().putInt("level_$level", score).apply()
        }
    }

    private fun showBestScore() {
        val prefs = requireContext().getSharedPreferences("training_results", Context.MODE_PRIVATE)
        val bestScore = prefs.getInt("level_$level", 0)
        resultText.text = "Рекорд: $bestScore"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }

    companion object {
        private const val ARG_LEVEL = "training_level"

        fun newInstance(level: Int) = ExerciseFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_LEVEL, level)
            }
        }
    }
}
