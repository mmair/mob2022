package at.campus02.mob.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class Choice {
    A, B, C, D
}

data class Question(
    val question: String,
    private val correct_answer: String,
    private val incorrect_answers: List<String>
) {
    private val randomizedAnswers = (incorrect_answers + correct_answer).shuffled()
    val answerA = randomizedAnswers[0]
    val answerB = randomizedAnswers[1]
    val answerC = randomizedAnswers[2]
    val answerD = randomizedAnswers[3]
    val correctChoice = when(correct_answer) {
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

private val theQuestions: List<Question> get() = listOf(
    Question(
        question = "How is the weather today 1?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 2?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 3?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 4?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 5?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 6?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 7?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 8?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 9?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
    Question(
        question = "How is the weather today 10?",
        correct_answer = "Sunny",
        incorrect_answers = listOf("Rainy", "Foggy", "Cloudy")),
)

class GameViewModel : ViewModel() {

    // intern, veränderbar
    private var questionsMutable: MutableLiveData<List<Question>> = MutableLiveData()
    private var questionMutable: MutableLiveData<Question> = MutableLiveData()
    private var buttonMarkersMutable: MutableLiveData<Map<Choice, Int>> = MutableLiveData(mapOf(
        Choice.A to R.drawable.button_background,
        Choice.B to R.drawable.button_background,
        Choice.C to R.drawable.button_background,
        Choice.D to R.drawable.button_background,
    ))

    // von außen sichtbar, aber nicht veränderbar
    val questions: LiveData<List<Question>> get() = questionsMutable
    val question: LiveData<Question> get() = questionMutable
    val buttonMarkers: LiveData<Map<Choice, Int>> get() = buttonMarkersMutable

    // index auf den Fragen
    private var index = 0

    // User Aktionen
    fun start() {
        questionsMutable.value = theQuestions
        questionMutable.value = questionsMutable.value?.get(index)
    }

    fun chooseAnswer(choice: Choice) {
        if (question.value?.isAnswered != true) {
            question.value?.choose(choice)
            updateButtonMarkers()
        }
    }

    fun next() {
        if (question.value?.isAnswered == true) {
            index++
            if (index < (questions.value?.size ?: 0)) {
                questionMutable.value = questions.value?.get(index)
                updateButtonMarkers()
            }
        }
    }

    // Hilfsmethoden
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
}