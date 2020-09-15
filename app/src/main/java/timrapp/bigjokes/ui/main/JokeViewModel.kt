package timrapp.bigjokes.ui.main

import androidx.lifecycle.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import timrapp.bigjokes.data.Joke
import timrapp.bigjokes.data.JokeRetrofitService
import java.io.IOException

class JokeViewModel : ViewModel() {
    private val retrofit = JokeRetrofitService.get()
    lateinit var lastJokeText: String

    fun getJoke() = liveData<Joke> {
        val joke = fetchJoke()
        if (joke != null) {
            // cache joke in case activity recreated
            lastJokeText = joke.text
            emit(joke)
        }
    }.map { joke -> joke.text }

    private suspend fun fetchJoke(): Joke? {
        return try {
            retrofit.nextJoke()
        } catch (e: IOException) {
            // in prod I'd log to Rollbar or similar
            println("exception fetching joke from API: ${e.toString()}")
            null
        }
    }
}
