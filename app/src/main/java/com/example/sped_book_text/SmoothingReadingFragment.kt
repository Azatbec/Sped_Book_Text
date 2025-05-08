package com.example.sped_book_text

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sped_book_text.databinding.FragmentSmoothingReadingBinding
import androidx.appcompat.app.AlertDialog

class SmoothingReadingFragment : Fragment() {

    private var _binding: FragmentSmoothingReadingBinding? = null
    private val binding get() = _binding!!

    private var currentLevel = 1
    private var selectedCheckboxId: Int? = null // ID выбранного чекбокса
    private var shouldShowSaveDialog = false // Флаг для диалога сохранения

    companion object {
        private const val TAG = "SmoothingReadingFragment"
        private const val KEY_LEVEL = "currentLevel"
        private const val KEY_CHECKBOX_ID = "selectedCheckboxId"
        private const val PREFS_NAME = "SmoothingReadingPrefs"
        private const val PREF_LEVEL = "prefLevel"
        private const val PREF_CHECKBOX_ID = "prefCheckboxId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmoothingReadingBinding.inflate(inflater, container, false)

        // Восстановление состояния
        if (savedInstanceState != null) {
            currentLevel = savedInstanceState.getInt(KEY_LEVEL, 1).coerceIn(1, 10)
            selectedCheckboxId = savedInstanceState.getInt(KEY_CHECKBOX_ID, -1).takeIf { it != -1 }
            Log.d(TAG, "Restored from savedInstanceState: level=$currentLevel, checkboxId=$selectedCheckboxId")
        } else {
            restoreSettings()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация SeekBar и уровня
        binding.trainingLevelSeekBar.progress = (currentLevel - 1).coerceIn(0, 9)
        binding.levelValueText.text = "Уровень: $currentLevel"

        // Восстановление состояния чекбоксов
        selectedCheckboxId?.let { id ->
            binding.checkboxes.find { it.id == id }?.isChecked = true
        }

        // Кнопка закрытия (назад)
        binding.button23.setOnClickListener {
            shouldShowSaveDialog = true
            showSaveSettingsDialog {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        // Кнопка "Начать тренировку"
        binding.button29.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Запуск тренировки: Уровень $currentLevel, Режим ${getModeName(selectedCheckboxId)}",
                Toast.LENGTH_SHORT
            ).show()

            // Переход к TrainingFragment
            val fragment = TrainingFragment.newInstance(currentLevel, selectedCheckboxId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Кнопка помощи
        binding.button30.setOnClickListener {
            showHelpDialog()
        }

        // Слушатель SeekBar
        binding.trainingLevelSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    currentLevel = (progress + 1).coerceIn(1, 10)
                    binding.levelValueText.text = "Уровень: $currentLevel"
                    Toast.makeText(
                        requireContext(),
                        "Уровень: $currentLevel",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateModeStatus()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Слушатели для чекбоксов
        binding.checkboxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                handleCheckboxChange(checkBox, isChecked, checkBox.id)
            }
        }
    }

    private val FragmentSmoothingReadingBinding.checkboxes: List<CheckBox>
        get() = listOf(
            checkboxFrameMode,
            checkboxSmoothScroll,
            checkboxCentralHighlight,
            checkboxVerticalFocus
        )

    private fun showHelpDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Инструкция по использованию")
            .setMessage(
                """
                Добро пожаловать в тренировку плавного чтения!

                1. **Выбор режима**:
                - Выберите один из режимов чтения, отметив соответствующий чекбокс:
                  - *Покадровый режим*: текст отображается по кадрам.
                  - *Плавная прокрутка*: текст прокручивается плавно.
                  - *Центральное выделение*: выделяет центральную часть текста.
                  - *Фокус по вертикали*: акцентирует внимание на вертикальном движении.
                - Только один режим может быть активен одновременно.

                2. **Выбор уровня**:
                - Используйте ползунок, чтобы выбрать уровень сложности от 1 до 10.
                - Текущий уровень отображается над ползунком.

                3. **Кнопки**:
                - *Кнопка "X" (вверху слева)*: возвращает на предыдущий экран.
                - *Кнопка "Начать тренировку"*: запускает тренировку с выбранными настройками.
                - *Кнопка "?" (внизу справа)*: открывает эту инструкцию.

                4. **Сохранение настроек**:
                - Выбранный уровень и режим сохраняются при повороте экрана.
                - При выходе вы можете выбрать, сохранить настройки или нет.

                Нажмите "ОК", чтобы закрыть инструкцию и продолжить настройку тренировки.
                """.trimIndent()
            )
            .setPositiveButton("ОК") { dialog, _ -> dialog.dismiss() }
            .setCancelable(true)
            .show()
    }

    private fun showSaveSettingsDialog(onProceed: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Сохранить настройки?")
            .setMessage("Хотите сохранить текущий уровень и выбранный режим для следующего запуска?")
            .setPositiveButton("Да") { _, _ ->
                saveSettings()
                onProceed()
            }
            .setNegativeButton("Нет") { _, _ ->
                onProceed()
            }
            .setCancelable(true)
            .setOnCancelListener {
                onProceed()
            }
            .show()
    }

    private fun saveSettings() {
        try {
            val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            with(prefs.edit()) {
                putInt(PREF_LEVEL, currentLevel)
                putInt(PREF_CHECKBOX_ID, selectedCheckboxId ?: -1)
                apply()
            }
            Log.d(TAG, "Settings saved: level=$currentLevel, checkboxId=$selectedCheckboxId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save settings", e)
            Toast.makeText(requireContext(), "Ошибка сохранения настроек", Toast.LENGTH_SHORT).show()
        }
    }

    private fun restoreSettings() {
        try {
            val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            currentLevel = prefs.getInt(PREF_LEVEL, 1).coerceIn(1, 10)
            selectedCheckboxId = prefs.getInt(PREF_CHECKBOX_ID, -1).takeIf { it != -1 }
            Log.d(TAG, "Settings restored: level=$currentLevel, checkboxId=$selectedCheckboxId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to restore settings", e)
            currentLevel = 1
            selectedCheckboxId = null
        }
    }

    private fun handleCheckboxChange(checkBox: CheckBox, isChecked: Boolean, checkBoxId: Int) {
        if (isChecked) {
            // Отключаем все остальные чекбоксы
            binding.checkboxes.forEach { otherCheckBox ->
                if (otherCheckBox.id != checkBoxId) otherCheckBox.isChecked = false
            }
            selectedCheckboxId = checkBoxId
            animateCheckbox(checkBox)
            updateModeStatus()
            Log.d(TAG, "Checkbox selected: id=$checkBoxId")
        } else if (selectedCheckboxId == checkBoxId) {
            selectedCheckboxId = null
            updateModeStatus()
            Log.d(TAG, "Checkbox deselected: id=$checkBoxId")
        }

        updateCheckboxStyle(checkBox, isChecked)
    }

    private fun updateCheckboxStyle(checkBox: CheckBox, isChecked: Boolean) {
        checkBox.isSelected = isChecked
        checkBox.setTypeface(null, if (isChecked) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL)
        checkBox.scaleX = if (isChecked) 1.05f else 1.0f
        checkBox.scaleY = if (isChecked) 1.05f else 1.0f

        val color = if (isChecked) {
            ContextCompat.getColor(requireContext(), R.color.checkbox_active)
        } else {
            ContextCompat.getColor(requireContext(), R.color.checkbox_inactive)
        }
        checkBox.setBackgroundColor(color)
    }


    private fun animateCheckbox(checkBox: CheckBox) {
        val colorFrom = ContextCompat.getColor(requireContext(), R.color.checkbox_active)
        val colorTo = ContextCompat.getColor(requireContext(), R.color.checkbox_inactive)
        val animator = ObjectAnimator.ofObject(
            checkBox,
            "backgroundColor",
            ArgbEvaluator(),
            colorFrom,
            colorTo
        )
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }


    private fun getModeName(checkboxId: Int?): String {
        return when (checkboxId) {
            R.id.checkbox_frame_mode -> "Покадровый режим"
            R.id.checkbox_smooth_scroll -> "Плавная прокрутка"
            R.id.checkbox_central_highlight -> "Центральное выделение"
            R.id.checkbox_vertical_focus -> "Фокус по вертикали"
            else -> "Не выбран"
        }
    }

    private fun updateModeStatus() {
        val modeName = getModeName(selectedCheckboxId)
        binding.levelValueText.text = "Уровень: $currentLevel, Режим: $modeName"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_LEVEL, currentLevel)
        outState.putInt(KEY_CHECKBOX_ID, selectedCheckboxId ?: -1)
        Log.d(TAG, "Saving instance state: level=$currentLevel, checkboxId=$selectedCheckboxId")
    }

    override fun onDestroyView() {
        if (isVisible && shouldShowSaveDialog) {
            showSaveSettingsDialog {}
        }
        super.onDestroyView()
        _binding = null
    }
}