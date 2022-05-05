package at.campus02.mob.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import at.campus02.mob.navigation.databinding.FragmentCategoryScreenBinding

/**
 * Category screen.
 */
class CategoryScreenFragment : Fragment() {

    private lateinit var binding: FragmentCategoryScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryScreenBinding.inflate(inflater)
        return binding.root
    }
}