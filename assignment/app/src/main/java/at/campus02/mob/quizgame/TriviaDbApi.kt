package at.campus02.mob.quizgame

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Hilfsdatenklasse für die Antwort vom REST API
data class QuestionsResponse(
    val response_code: Int,
    val results: List<Question>
)

// ---------------------------------------------------------------------------------------------
// Definition des REST API via Interface und Retrofit-Annotationen
// ---------------------------------------------------------------------------------------------
interface TriviaDbApi {
    @GET("api.php?amount=10&type=multiple")
    suspend fun getQuestions(): Response<QuestionsResponse>
}

// -------------------------------------------------------------------------------------------------
// Dependency Injection "Provider"
// -------------------------------------------------------------------------------------------------
// Ein "dagger" Module - damit kann man (in diesem Fall auf ViewModelComponent Ebene)
// Instanzen von Klassen für DI zur Verfügung stellen, die zB. eine Konfiguration benötigen
// und daher nicht einfach über den default-Constructor injiziert werden können.
// In unserem Fall das TriviaDbApi, welches nicht einfach instanziert, sondern erst über
// den Retrofit-Builder konfiguriert und gebaut werden muss.
@Module
@InstallIn(ViewModelComponent::class)
object TriviaApiModule {

    @Provides
    fun getTriviaApi(): TriviaDbApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://opentdb.com/")
            .build()
            .create(TriviaDbApi::class.java)
    }
}
