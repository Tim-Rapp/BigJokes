package timrapp.bigjokes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import timrapp.bigjokes.data.Joke

class JokeTest {
    @Test
    fun joke(){
        // trivial test to make sure junit5 working
        assertEquals("some joke", Joke("some joke").text)
    }
}