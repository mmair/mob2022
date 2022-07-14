package at.campus02.mob.quizgame

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Hilfsklasse, damit in den Unit-Tests die asynchronen Methoden verwendet werden können (also
 * bei uns <code>viewModelScope.launch</code> bzw. <code>MainScope().launch</code>.
 *
 * Die mittlerweile empfohlene Variante:
 * <code>class MainDispatcherRule(val dispatcher: TestDispatcher = StandardTestDispatcher()): TestWatcher()</code>
 * funktioniert leider nicht wirklich zusammen mit dem Observer in den Tests.
 */
@ExperimentalCoroutinesApi
class MainDispatcherRule(val dispatcher: TestDispatcher = TestCoroutineDispatcher()): TestWatcher() {

    override fun starting(description: Description) = Dispatchers.setMain(dispatcher)

    override fun finished(description: Description) = Dispatchers.resetMain()

}