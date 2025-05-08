package com.example.sped_book_text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class BlankFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_blank, container, false)

        // Ищем кнопку для возврата
        val backButton = view.findViewById<Button>(R.id.button)
        backButton.setOnClickListener {
            // Скрываем фрагмент-контейнер
            requireActivity().findViewById<View>(R.id.fragment_container).visibility = View.GONE

            // Удаляем фрагмент из back stack
            requireActivity().supportFragmentManager.popBackStack()

            // Показываем кнопки снова в MainActivity
            (activity as MainActivity).showButtons()
        }

        return view
    }
}
