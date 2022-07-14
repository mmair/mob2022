package at.campus02.mob.quizgame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Zur Veranschaulichung ein Beispiel für Unit-Tests für das GameViewModel.
 *
 * Nachdem wir mit asynchronen Funktionen arbeiten, aber nicht die gesamte Umgebung der
 * App zur Verfügung haben, müssen wir uns auch darum kümmern, dass die Verwendung der
 * "suspend" Funktionen und der "coroutines scopes" bei den Unit-Tests auch so laufen, wie
 * sie sollen. Das passiert durch die beiden annotierten Felder instantTaskExecutorRule
 * und mainDispatcherRule.
 */
class GameViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Die in den Unit-Tests verwendeten Repositories können direkt als Felder der
    // Test-Klasse definiert werden.
    private val preferencesRepository = TestPreferencesRepository()
    private val questionRepository = QuestionRepository(TestTriviaDbApi())

    @ExperimentalCoroutinesApi
    @Test
    fun afterStartFirstQuestionOfSelectedCategoryIsDisplayed() {
        // given
        val viewModel = GameViewModel(questionRepository, preferencesRepository)

        // when
        viewModel.start(Category.COMPUTER)

        // Hilfsmethode, um LiveData "observieren" zu können ohne tatsächliche App.
        val question = viewModel.question.getOrAwaitValue()

        // then (die backticks ` werden benötigt, weil "is" ein Kotlin Keyword ist).
        assertThat(question, `is`(questionMap[Category.COMPUTER]?.get(0)))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun nextQuestionIsOnlyDisplayedIfActualQuestionHasBeenAnswered() {
        // given
        val viewModel = GameViewModel(questionRepository, preferencesRepository)
        viewModel.start(Category.GENERAL)

        // then
        assertThat(viewModel.question.getOrAwaitValue(), `is`(questionMap[Category.GENERAL]?.get(0)))

        // when
        viewModel.next()

        // then: same question as it has not been answered yet
        assertThat(viewModel.question.getOrAwaitValue(), `is`(questionMap[Category.GENERAL]?.get(0)))

        // when
        viewModel.question.getOrAwaitValue().choose(Choice.A)
        viewModel.next()

        // then: next question as previous question has been answered
        assertThat(viewModel.question.getOrAwaitValue(), `is`(questionMap[Category.GENERAL]?.get(1)))
    }

    /**
     * Hilfsfunktion, um LiveData zu "beobachten".
     * Nachdem in den Unit-Tests nicht die volle Funktionalität zur Verfügung steht
     * (weil zB. der entsprechende LifeCycle nicht wirklich vorhanden ist, ohne dass der
     * Code tatsächlich in der App läuft), muss man sich beim Observer etwas behelfen.
     *
     * Nach der vorgegebenen Wartezeit wird eine TimeoutException geschmissen, falls
     * "onChanged" des Observers nicht aufgerufen wurde.
     *
     * Implementiert als "Extension Function" auf LiveData.
     */
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}