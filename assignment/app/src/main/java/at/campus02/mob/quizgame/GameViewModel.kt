package at.campus02.mob.quizgame

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

// -------------------------------------------------------------------------------------------------
// Datenmodell
// -------------------------------------------------------------------------------------------------
enum class Category(val categoryId: Int) {
    GENERAL(9),
    HISTORY(23),
    SCIENCE(17),
    COMPUTER(18);

    // nur für die Tests gebraucht!
    companion object {
        fun fromId(id: Int): Category {
            return values().find { it.categoryId == id } ?: throw IllegalArgumentException()
        }
    }
}

enum class Choice {
    A, B, C, D, NONE
}

data class Question(
    val question: String,
    private val correct_answer: String,
    private val incorrect_answers: List<String>
) {
    private var _randomizedAnswers: List<String>? = null
    val randomizedAnswers: List<String> get() {
        if (_randomizedAnswers == null) {
            _randomizedAnswers = (incorrect_answers + correct_answer).shuffled()
        }
        return _randomizedAnswers!!
    }
    val answerA get() = randomizedAnswers[0]
    val answerB get() = randomizedAnswers[1]
    val answerC get() = randomizedAnswers[2]
    val answerD get() = randomizedAnswers[3]
    val correctChoice get() = when(correct_answer) {
        answerA -> Choice.A
        answerB -> Choice.B
        answerC -> Choice.C
        answerD -> Choice.D
        else -> throw IllegalStateException("No correct answer found!")
    }
    var choice: Choice? = null

    fun choose(userChoice: Choice) {
        choice = userChoice
    }

    val isAnswered get() = choice != null
    val isCorrect get() = isAnswered && choice == correctChoice
}

// -------------------------------------------------------------------------------------------------
// ViewModel
// -------------------------------------------------------------------------------------------------
// durch die Annotation kann das ViewModel mit Dependency Injection umgehen (kriegt Abhängigkeiten
// injiziert) und trotzdem via "by activityViewModels()" im Fragment geholt werden.
@HiltViewModel
class GameViewModel @Inject constructor(
    val questionRepository: QuestionRepository,
    val preferencesRepository: PreferencesRepository
): ViewModel() {

    // ---------------------------------------------------------------------------------------------
    // LiveData
    // ---------------------------------------------------------------------------------------------
    // intern, veränderbar
    private var questionsMutable: MutableLiveData<List<Question>> = MutableLiveData()
    private var questionMutable: MutableLiveData<Question> = MutableLiveData()
    private var buttonMarkersMutable: MutableLiveData<Map<Choice, Int>> = MutableLiveData(mapOf(
        Choice.A to R.drawable.button_background,
        Choice.B to R.drawable.button_background,
        Choice.C to R.drawable.button_background,
        Choice.D to R.drawable.button_background,
    ))
    private var guessingProgressMutable: MutableLiveData<Int> = MutableLiveData(0)
    private var scoreMutable: MutableLiveData<String> = MutableLiveData()
    private var progressMarkersMutable: MutableLiveData<List<Int>> = MutableLiveData()
    private var errorMutable: MutableLiveData<String> = MutableLiveData()

    // ---------------------------------------------------------------------------------------------
    // von außen sichtbar, aber nicht veränderbar
    val questions: LiveData<List<Question>> get() = questionsMutable
    val question: LiveData<Question> get() = questionMutable
    val buttonMarkers: LiveData<Map<Choice, Int>> get() = buttonMarkersMutable
    val guessingProgress: LiveData<Int> get() = guessingProgressMutable
    val score: LiveData<String> get() = scoreMutable
    val progressMarkers: LiveData<List<Int>> get() = progressMarkersMutable
    val error: LiveData<String> get() = errorMutable

    // index auf den Fragen
    private var index = 0

    // ---------------------------------------------------------------------------------------------
    // User Aktionen
    // ---------------------------------------------------------------------------------------------
    fun start(category: Category) {
        errorMutable.value = null
        guessingCountDownTimer.cancel()
        questionMutable.value = Question("", "", listOf("", "", ""))
        questionsMutable.value = listOf()
        updateMarkers()
        // REST access mit kotlin coroutines
        viewModelScope.launch {
            try {
                val questionsFromServer = questionRepository.getQuestions(category.categoryId)
                MainScope().launch {
                    index = 0
                    questionsMutable.value = questionsFromServer
                    questionMutable.value = questionsMutable.value?.get(index)
                    updateMarkers()
                    guessingCountDownTimer.start()
                }
            } catch (exc: Exception) {
                errorMutable.postValue(exc.message)
            }
        }
    }

    private fun updateMarkers() {
        updateButtonMarkers()
        updateProgressMarkers()
        updateScore()
    }

    fun chooseAnswer(choice: Choice) {
        if (question.value?.isAnswered != true) {
            question.value?.choose(choice)
            updateMarkers()
            guessingCountDownTimer.cancel()
        }
    }

    fun next() {
        if (question.value?.isAnswered == true) {
            index++
            if (index < (questions.value?.size ?: 0)) {
                questionMutable.value = questionsMutable.value?.get(index)
                updateMarkers()
                guessingCountDownTimer.start()
            }
        }
    }

    fun selectQuestion(index: Int) {
        val allQuestions = questionsMutable.value ?: return
        if (allQuestions.all { it.isAnswered }) {
            this.index = index
            questionMutable.value = questionsMutable.value?.get(index)
            updateMarkers()
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Hilfsfunktionalität
    // ---------------------------------------------------------------------------------------------
    private val guessingCountDownTimer = object {
        private lateinit var countDownTimer: CountDownTimer

        fun start() {
            val timeInMs = preferencesRepository.getTimerDuration()
            guessingProgressMutable.value = 100
            countDownTimer = object : CountDownTimer(timeInMs, 500) {
                override fun onTick(remainingMillis: Long) {
                    guessingProgressMutable.value = ((remainingMillis / timeInMs.toDouble()) * 100).toInt()
                }

                override fun onFinish() {
                    guessingProgressMutable.value = 0
                    if (question.value?.isAnswered == false)
                        chooseAnswer(Choice.NONE)
                }
            }
            countDownTimer.start()
        }

        fun cancel() {
            guessingProgressMutable.value = 0
            if (this::countDownTimer.isInitialized)
                countDownTimer.cancel()
        }
    }

    private fun updateScore() {
        val allQuestions = questionsMutable.value ?: listOf()
        if (!allQuestions.isEmpty() && allQuestions.all { it.isAnswered }) {
            scoreMutable.value = "Score: ${allQuestions.count { it.isCorrect }} / ${allQuestions.size} correct"
        } else {
            scoreMutable.value = null
        }
    }

    private fun updateButtonMarkers() {
        buttonMarkersMutable.value = mapOf(
            Choice.A to buttonResourceFor(question.value, Choice.A),
            Choice.B to buttonResourceFor(question.value, Choice.B),
            Choice.C to buttonResourceFor(question.value, Choice.C),
            Choice.D to buttonResourceFor(question.value, Choice.D),
        )
    }

    private fun buttonResourceFor(question: Question?, choice: Choice): Int {
        return when {
            // keine Frage vorhanden: alle Buttons neutral
            question == null -> R.drawable.button_background
            // Frage noch nicht beantwortet: alle Buttons neutral
            !question.isAnswered -> R.drawable.button_background
            // Button entspricht der richtigen Antwort und die Frage wurde korrekt beantwortet: grün
            question.isCorrect && choice == question.correctChoice -> R.drawable.button_background_correct
            // Hint
            !question.isCorrect && choice == question.correctChoice -> R.drawable.button_background_hint
            // Button entspricht falsch gegebener Antwort
            !question.isCorrect && choice == question.choice -> R.drawable.button_background_incorrect
            // falls kein anderer Fall zuständig ist: neutral
            else -> R.drawable.button_background
        }
    }

    private fun updateProgressMarkers() {
        progressMarkersMutable.value = listOf(
            progressResourceFor(0),
            progressResourceFor(1),
            progressResourceFor(2),
            progressResourceFor(3),
            progressResourceFor(4),
            progressResourceFor(5),
            progressResourceFor(6),
            progressResourceFor(7),
            progressResourceFor(8),
            progressResourceFor(9),
        )
    }

    private fun progressResourceFor(index: Int): Int {
        // Die Frage, die dem Progress-Indikator mit diesem Index entspricht
        val progressQuestion = questions.value?.getOrNull(index)
        return when {
            // keine passende Frage -> neutraler Hintergrund
            progressQuestion == null -> R.drawable.progress_unanswered
            // header für die aktuelle Frage, noch unbeantwortet
            progressQuestion == question.value && !progressQuestion.isAnswered -> R.drawable.progress_current
            // header für die aktuelle Frage,  falsch beantwortet
            progressQuestion == question.value && !progressQuestion.isCorrect -> R.drawable.progress_current_incorrect
            // header für die aktuelle Frage,  richtig beantwortet
            progressQuestion == question.value && progressQuestion.isCorrect -> R.drawable.progress_current_correct
            // header für bereits beantwortete Frage,  richtig beantwortet
            progressQuestion.isAnswered && progressQuestion.isCorrect -> R.drawable.progress_correct
            // header für bereits beantwortete Frage,  falsch beantwortet
            progressQuestion.isAnswered && !progressQuestion.isCorrect -> R.drawable.progress_incorrect
            // falls nichts anderes zuschlägt
            else -> R.drawable.progress_unanswered
        }
    }
}
