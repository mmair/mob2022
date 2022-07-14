package at.campus02.mob.quizgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import at.campus02.mob.quizgame.databinding.FragmentCategoryScreenBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Category screen.
 */
@AndroidEntryPoint
class CategoryScreenFragment : Fragment() {

    private lateinit var binding: FragmentCategoryScreenBinding
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.generalCategoryButton.setOnClickListener {
            gameViewModel.start(Category.GENERAL)
            findNavController().navigate(R.id.action_categoryScreenFragment_to_gameFragment)
        }
        binding.historyCategoryButton.setOnClickListener {
            gameViewModel.start(Category.HISTORY)
            findNavController().navigate(R.id.action_categoryScreenFragment_to_gameFragment)
        }
        binding.scienceCategoryButton.setOnClickListener {
            gameViewModel.start(Category.SCIENCE)
            findNavController().navigate(R.id.action_categoryScreenFragment_to_gameFragment)
        }
        binding.computerCategoryButton.setOnClickListener {
            gameViewModel.start(Category.COMPUTER)
            findNavController().navigate(R.id.action_categoryScreenFragment_to_gameFragment)
        }
    }
}