package com.example.sped_book_text

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.sped_book_text.databinding.FragmentMetronomeReadingBinding

class MetronomeReadingFragment : Fragment() {

    private var _binding: FragmentMetronomeReadingBinding? = null
    private val binding get() = _binding!!

    private var isMetronomeRunning = false
    private var metronomeHandler: Handler? = null
    private var metronomeRunnable: Runnable? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMetronomeReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSeekBar()
        setupListeners()
    }

    private fun setupSeekBar() {
        binding.bpmSeekBar.max = 140
        binding.bpmSeekBar.progress = 0
        binding.bpmText.text = "Частота: 60 BPM"

        binding.bpmSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val bpm = 60 + progress
                binding.bpmText.text = "Частота: $bpm BPM"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupListeners() {
        binding.startStopButton.setOnClickListener {
            if (isMetronomeRunning) {
                stopMetronome()
            } else {
                startMetronome()
            }
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.instructionButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, MetronomeInstructionFragment())
                addToBackStack(null)
            }
        }
    }

    private fun startMetronome() {
        val bpm = 60 + binding.bpmSeekBar.progress
        val delay = (60000 / bpm).toLong()

        // Выбираем звуковой файл в зависимости от BPM
        val soundResId = when (bpm) {
            60 -> R.raw.click_60
            80 -> R.raw.click_80
            100 -> R.raw.click_100
            else -> R.raw.click_60 // По умолчанию для других значений BPM
        }

        // Инициализируем MediaPlayer с выбранным звуком
        mediaPlayer?.release() // Освобождаем предыдущий MediaPlayer, если он был
        mediaPlayer = MediaPlayer.create(requireContext(), soundResId)

        metronomeHandler = Handler(Looper.getMainLooper())
        metronomeRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.start()
                metronomeHandler?.postDelayed(this, delay)
            }
        }
        metronomeHandler?.post(metronomeRunnable!!)

        binding.startStopButton.text = "Остановить метроном"
        isMetronomeRunning = true
    }

    private fun stopMetronome() {
        metronomeHandler?.removeCallbacks(metronomeRunnable!!)
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        binding.startStopButton.text = "Запустить метроном"
        isMetronomeRunning = false
    }

    override fun onDestroyView() {
        stopMetronome()
        _binding = null
        metronomeHandler = null
        metronomeRunnable = null
        super.onDestroyView()
    }
}