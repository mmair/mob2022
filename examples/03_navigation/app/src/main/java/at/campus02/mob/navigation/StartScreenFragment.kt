package at.campus02.mob.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.campus02.mob.navigation.databinding.FragmentStartScreenBinding

/**
 * Start screen.
 */
class StartScreenFragment : Fragment() {

    private lateinit var binding: FragmentStartScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.startButton.setOnClickListener {
            val navigationController = findNavController()
            navigationController.navigate(R.id.action_startScreenFragment_to_categoryScreenFragment)
        }
    }
}