package com.example.sped_book_text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sped_book_text.databinding.FragmentAchievementDetailsBinding

class AchievementDetailsFragment : Fragment() {

    private var _binding: FragmentAchievementDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_ACHIEVEMENT_TITLE = "achievement_title"

        fun newInstance(achievementTitle: String): AchievementDetailsFragment {
            val fragment = AchievementDetailsFragment()
            val args = Bundle()
            args.putString(ARG_ACHIEVEMENT_TITLE, achievementTitle)
            fragment.arguments = args
            return fragment
        }
    }

    data class Achievement(val title: String, val description: String, val iconResId: Int, val isAchieved: Boolean)

    private val allAchievements = listOf(
        Achievement("Сосредоточенность", "Достижение для улучшения концентрации.", R.drawable.focus_icon, true),
        Achievement("Завершитель", "Достижение для завершения всех задач.", R.drawable.complete_icon, true),
        Achievement("Эксперт", "Достижение для глубоких знаний в области.", R.drawable.expert_icon, false),
        Achievement("Полное завершение", "Достижение за полное завершение всех этапов.", R.drawable.full_finish_icon, true),
        Achievement("Начальный этап", "Достижение для старта первого этапа.", R.drawable.start_stage_icon, false),
        Achievement("Ранний старт", "Достижение за быстрый старт.", R.drawable.early_start_icon, false),
        Achievement("Настойчивость", "Достижение за постоянство в действиях.", R.drawable.persistence_icon, true),
        Achievement("Идеальный баланс", "Достижение за гармонию в действиях.", R.drawable.balance_icon, false),
        Achievement("Быстрый старт", "Достижение за быстрый старт в любом деле.", R.drawable.fast_start_icon, true),
        Achievement("Мастер концентрации", "Достижение за высокую концентрацию внимания.", R.drawable.master_focus_icon, true),
        Achievement("Ветеран практики", "Достижение за многолетнюю практику.", R.drawable.veteran_icon, false),
        Achievement("Непоколебимость", "Достижение за стойкость и нерушимость.", R.drawable.steadfast_icon, true),
        Achievement("Уверенное продвижение", "Достижение за уверенное движение к цели.", R.drawable.progress_icon, false),
        Achievement("Цель достигнута", "Достижение за выполнение поставленной цели.", R.drawable.goal_icon, true),
        Achievement("Этап завершён", "Достижение за завершение этапа.", R.drawable.stage_done_icon, false),
        Achievement("Уверенность", "Достижение за уверенность в действиях.", R.drawable.confidence_icon, true),
        Achievement("Лидер", "Достижение за лидерские качества.", R.drawable.leader_icon, true),
        Achievement("Продвинутый уровень", "Достижение за достижение высокого уровня.", R.drawable.advanced_icon, false),
        Achievement("Покоритель целей", "Достижение за успешное достижение целей.", R.drawable.goal_conqueror_icon, true),
        Achievement("Новичок", "Достижение для новых пользователей.", R.drawable.beginner_icon, false),
        Achievement("Энергия и сила", "Достижение за высокую активность.", R.drawable.energy_icon, true),
        Achievement("Стратег", "Достижение за стратегическое мышление.", R.drawable.strategist_icon, false),
        Achievement("Опытный игрок", "Достижение за накопленный опыт.", R.drawable.experienced_icon, true),
        Achievement("Творческий подход", "Достижение за использование креативных решений.", R.drawable.creativity_icon, false),
        Achievement("Скорочтение мастер", "Достижение за мастерство в скорочтении.", R.drawable.reading_icon, true),
        Achievement("Мастер внимательности", "Достижение за внимание к деталям.", R.drawable.detail_icon, false),
        Achievement("Дисциплина", "Достижение за самодисциплину.", R.drawable.discipline_icon, true),
        Achievement("Командный игрок", "Достижение за умение работать в команде.", R.drawable.teamplayer_icon, true),
        Achievement("Позитивное мышление", "Достижение за позитивный настрой.", R.drawable.positivity_icon, false),
        Achievement("Чтение с пониманием", "Достижение за способность быстро и понятно воспринимать прочитанное.", R.drawable.reading_icon, true),
        Achievement("Постоянство", "Достижение за проявление постоянства и усердия.", R.drawable.consistency_icon, false),
        Achievement("Фокус внимания", "Достижение за умение сосредотачиваться.", R.drawable.focus_icon, true),
        Achievement("Рекорд скорости", "Достижение за рекорды по скорости выполнения.", R.drawable.speed_icon, true),
        Achievement("Прогресс каждый день", "Достижение за ежедневный прогресс.", R.drawable.daily_progress_icon, false),
        Achievement("Логика и анализ", "Достижение за способность логически мыслить и анализировать.", R.drawable.logic_icon, true),
        Achievement("Развитие памяти", "Достижение за развитие памяти.", R.drawable.memory_icon, false),
        Achievement("Критическое мышление", "Достижение за способность к критическому анализу.", R.drawable.critical_icon, false),
        Achievement("Внимательность к деталям", "Достижение за внимание к мелким деталям.", R.drawable.detail_icon, true),
        Achievement("Эмоциональный интеллект", "Достижение за развитие эмоционального интеллекта.", R.drawable.emotional_icon, true),
        Achievement("Самоорганизация", "Достижение за умение организовывать себя и свои дела.", R.drawable.self_org_icon, false),
        Achievement("Многозадачность", "Достижение за умение эффективно работать над несколькими задачами одновременно.", R.drawable.multitasking_icon, true),
        Achievement("Мотивация к успеху", "Достижение за высокую мотивацию и стремление к успеху.", R.drawable.motivation_icon, true)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val achievementTitle = arguments?.getString(ARG_ACHIEVEMENT_TITLE)
        val achievement = allAchievements.find { it.title == achievementTitle }

        if (achievement != null) {
            binding.achievementTitle.text = achievement.title
            binding.achievementDescription.text = achievement.description
            binding.achievementIcon.setImageResource(achievement.iconResId)
        }

        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
