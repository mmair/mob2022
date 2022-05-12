package at.campus02.mob.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import at.campus02.mob.viewmodel.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Observers
        gameViewModel.question.observe(this) { question ->
            binding.questionText.text = question.question
            binding.button1Label.text = question.answerA
            binding.button2Label.text = question.answerB
            binding.button3Label.text = question.answerC
            binding.button4Label.text = question.answerD
        }
        gameViewModel.buttonMarkers.observe(this) { buttonMarkerMap ->
            binding.button1Layout.setBackgroundResource(buttonMarkerMap[Choice.A] ?: R.drawable.button_background)
            binding.button2Layout.setBackgroundResource(buttonMarkerMap[Choice.B] ?: R.drawable.button_background)
            binding.button3Layout.setBackgroundResource(buttonMarkerMap[Choice.C] ?: R.drawable.button_background)
            binding.button4Layout.setBackgroundResource(buttonMarkerMap[Choice.D] ?: R.drawable.button_background)
        }

        // User Aktionen
        binding.button1Layout.setOnClickListener {
            gameViewModel.chooseAnswer(Choice.A)
        }
        binding.button2Layout.setOnClickListener {
            gameViewModel.chooseAnswer(Choice.B)
        }
        binding.button3Layout.setOnClickListener {
            gameViewModel.chooseAnswer(Choice.C)
        }
        binding.button4Layout.setOnClickListener {
            gameViewModel.chooseAnswer(Choice.D)
        }

        binding.continueButtonLayout.setOnClickListener {
            gameViewModel.next()
        }
    }
}