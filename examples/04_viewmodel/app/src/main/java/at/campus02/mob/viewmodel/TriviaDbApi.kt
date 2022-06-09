package at.campus02.mob.viewmodel

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

data class QuestionsResponse(
    val response_code: Int,
    val results: List<Question>
)

// https://opentdb.com/api.php?amount=10&type=multiple

interface TriviaDbApi {
    @GET("api.php?amount=10&type=multiple")
    fun getQuestions(): Call<QuestionsResponse>

    @GET("api.php?type=multiple")
    suspend fun getQuestionsWithCoroutines(@Query("amount") amount: Int): Response<QuestionsResponse>
}

val triviaDbApi = Retrofit.Builder()
    // JSON converter: Moshi, keine zusätzlichen eigenen Converter
    .addConverterFactory(
        MoshiConverterFactory.create(
            Moshi.Builder().build()
        )
    )
    // BASE URL des API
    .baseUrl("https://opentdb.com/")
    // Konfiguration bauen
    .build()
    // API Interface implementieren lassen
    .create(TriviaDbApi::class.java)