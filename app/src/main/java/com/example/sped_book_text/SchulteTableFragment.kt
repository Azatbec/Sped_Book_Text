package com.example.sped_book_text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.sped_book_text.databinding.FragmentSchulteTableBinding

class SchulteTableFragment : Fragment() {

    private var _binding: FragmentSchulteTableBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSchulteTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSeekBar()
        setupListeners()
    }

    private fun setupSeekBar() {
        binding.trainingLevelSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val levelName = when (progress) {
                    in 0..3 -> "Простой"
                    in 4..7 -> "Средний"
                    in 8..10 -> "Сложный"
                    else -> "Неизвестно"
                }
                binding.levelText.text = "Уровень: $levelName"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupListeners() {
        binding.button3.setOnClickListener {
            // Переход к фрагменту с инструкцией
            parentFragmentManager.commit {
                replace(R.id.fragment_container, SchulteInstructionFragment())
                addToBackStack(null)
            }
        }

        binding.startTrainingButton.setOnClickListener {
            val selectedLevel = binding.trainingLevelSeekBar.progress
            Toast.makeText(requireContext(), "Запуск тренировки, уровень $selectedLevel", Toast.LENGTH_SHORT).show()

            // Переход к фрагменту ExerciseFragment
            parentFragmentManager.commit {
                replace(R.id.fragment_container, ExerciseFragment.newInstance(selectedLevel))
                addToBackStack(null)
            }
        }

        binding.button16.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                requireActivity().finish() // Завершает активность, если стек пуст
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}