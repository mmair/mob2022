package at.campus02.mob.quizgame

import retrofit2.Response

// Hilfsklassen für die Unit-Tests.
//
// Es werden die für die Tests zu verwendenden Repositories definiert (arbeiten mit fixen Testdaten,
// ohne echte Server-Kommunikation), damit man reproduzierbare Testfälle/Ergebnisse erhält.

private fun dummyQuestionFor(category: Category, index: Int): Question {
    return Question(
        question = "${category.name}: Dummy Question $index",
        correct_answer = "correct",
        incorrect_answers = listOf("incorrect 1", "incorrect 2", "incorrect 3")
    )
}

private fun dummyQuestionsFor(category: Category): List<Question> {
    return (0..9).map { dummyQuestionFor(category, it) }
}

// Testfragen pro Kategorie
val questionMap = mapOf(
    Category.GENERAL to dummyQuestionsFor(Category.GENERAL),
    Category.SCIENCE to dummyQuestionsFor(Category.SCIENCE),
    Category.HISTORY to dummyQuestionsFor(Category.HISTORY),
    Category.COMPUTER to dummyQuestionsFor(Category.COMPUTER),
)

/**
 * Implementierung des TriviaDbApi für Unit-Tests.
 * Wickelt die Liste der Testfragen in ein Retrofit-Response-Objekt ein und gibt es
 * asynchron zurück (suspend function).
 *
 * Für die Unit-Tests kann eine Instanz dieser Klasse dem QuestionRepository als
 * Konstruktor-Parameter übergeben werden.
 */
class TestTriviaDbApi : TriviaDbApi {
    override suspend fun getQuestions(): Response<QuestionsResponse> {
        return Response.success(QuestionsResponse(0, questionMap[Category.GENERAL]!!))
    }

    override suspend fun getQuestionsForCategory(categoryId: Int): Response<QuestionsResponse> {
        return Response.success(QuestionsResponse(0, questionMap[Category.fromId(categoryId)]!!))
    }

}

/**
 * Implementierung des PreferencesRepository für die Unit-Tests.
 * Liefert aktuell einfach eine fixe Countdown-Zeit zurück.
 */
class TestPreferencesRepository : PreferencesRepository {
    override fun getTimerDuration(): Long {
        return 1L
    }
}