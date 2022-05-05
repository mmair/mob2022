package at.campus02.mob.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import at.campus02.mob.navigation.databinding.FragmentGameScreenBinding

class GameScreenFragment : Fragment() {

    private lateinit var binding: FragmentGameScreenBinding
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.questionText.observe(this) { questionTextFromModel ->
            binding.questionText.text = questionTextFromModel
        }

        binding.buttonA.setOnClickListener {
            viewModel.changeQuestion()
        }

    }

}