package at.campus02.mob.viewmodel

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

// Einstiegspunkt für DI - Achtung: im Manifest deklarieren!
@HiltAndroidApp
class QuizApplication : Application()

class QuestionRepository @Inject constructor(
    val api: TriviaDbApi
) {

    suspend fun getQuestions(): List<Question> {
        val response = api.getQuestionsWithCoroutines(10)
        if (response.isSuccessful) {
            val questions =
                response.body()?.results ?: throw IllegalStateException("No questions received")
            if (questions.size != 10) {
                throw IllegalStateException("Received ${questions.size} instead of 10 questions")
            }
            return questions
        } else {
            throw IllegalStateException("Error when fetching questions: ${response.message()}")
        }
    }
}