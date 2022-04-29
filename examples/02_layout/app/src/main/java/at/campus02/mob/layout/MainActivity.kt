package at.campus02.mob.layout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import at.campus02.mob.layout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // viewBinding - im build.gradle eintragen, damit das funktioniert!
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // layout über viewBinding "inflaten" und als contentView zuweisen
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        // Zugriff auf den TextView aus dem Code heraus
        val clickMeButton: TextView = findViewById(R.id.clickMeButton)
        val displayView: TextView = findViewById(R.id.displayView)

        // OnClickListener zuweisen (es gibt mehrere Methoden, siehe dazu Skriptum)
        // -> das hier ist die "Lambda"-Methode
        clickMeButton.setOnClickListener {
            displayView.text = "I have been clicked (old)!"
        }

        // Zugriff mittels viewBinding
        // der OnClickListener wird hier erneut zugewiesen, d.h. dieser Listener "gewinnt"
        binding.clickMeButton.setOnClickListener {
            // Zuweisen eines neuen Texts auf einen TextView
            // (man wird es nur nicht lange sehen, weil die Activity gewechselt wird).
            binding.displayView.text = "I have been clicked!"

            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
    }
}