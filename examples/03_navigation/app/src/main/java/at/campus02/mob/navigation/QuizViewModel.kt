package at.campus02.mob.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel: ViewModel() {


    val questionText: MutableLiveData<String> = MutableLiveData("Wie ist das Wetter heute?")


    fun changeQuestion() {
        questionText.value = "Haben Sie bemerkt, dass sich die Frage geändert hat?"
    }

}