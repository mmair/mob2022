package at.campus02.mob.quizgame

import android.app.Application
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

// -------------------------------------------------------------------------------------------------
// Application
// -------------------------------------------------------------------------------------------------
// Application class, markiert als Einstiegspunkt für Dependency Injection
// Achtung: damit das funktioniert, muss im AndroidManifest.xml file der Name
// der zu verwendenden Application eingetragen werden (analog zu einer Activity)
@HiltAndroidApp
class QuizApplication : Application()

// -------------------------------------------------------------------------------------------------
// Repositories
// -------------------------------------------------------------------------------------------------
// Das QuestionRepository wird dem GameViewModel bei der Erzeugung via DI "injiziert".
// Intern verwendet es das TriviaDbApi (welches es selbst über den Konstruktor injiziert
// bekommt).
class QuestionRepository @Inject constructor(
    val api: TriviaDbApi
)  {

    suspend fun getQuestions(): List<Question> {
        val response = api.getQuestions()
        if (response.isSuccessful) {
            val questions = response.body()?.results ?: throw IllegalStateException("No questions received.")
            if (questions.size != 10) {
                throw IllegalStateException("Received ${questions.size} instead of 10 questions.")
            }
            return questions
        } else {
            throw java.lang.IllegalStateException("Error when fetching questions: ${response.message()}")
        }
    }
}

// Repository für die "SharedPreferences", in diesem Fall nur für die Dauer des Timers.
// Verwaltet wird die Dauer über das SettingsFragment, dieses Repository greift nur darauf
// zu und gibt den persistenten Wert zurück.
class PreferencesRepository @Inject constructor(
    private val application: Application
) {
    fun getTimerDuration(): Long {
        return PreferenceManager.getDefaultSharedPreferences(application).getInt("timer_duration", 20).toLong() * 1000L
    }
}


