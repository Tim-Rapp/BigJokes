package timrapp.bigjokes

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.AllOf
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import timrapp.bigjokes.data.EspressoTestingIdlingResource
import timrapp.bigjokes.data.JokeApiConfig

class UITest {
    private val mockWebServer = MockWebServer()
    private lateinit var scenario: ActivityScenario<MainActivity>

    @BeforeEach
    fun setup(){
        // espresso needs to know how to handle background tasks
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.idlingResource);
        mockWebServer.start(8080)
    }

    @AfterEach
    fun teardown(){
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.idlingResource);
        mockWebServer.shutdown()
        scenario.close()
    }

    @Test
    fun endToEndTest() {
        val firstJoke = "Why can’t you hear a pterodactyl go to the bathroom? The p is silent."
        val secondJoke = "What's blue and not that heavy? Light blue"
        mockWebServer.enqueue(MockResponse().setBody(createResponseBody(firstJoke)))
        mockWebServer.enqueue(MockResponse().setBody(createResponseBody(secondJoke)))
        JokeApiConfig.url = mockWebServer.url("/").toString()

        scenario = ActivityScenario.launch(MainActivity::class.java)

        // launch activity creates fragment which fetches joke
        scenario.onActivity { activity ->
            assertTrue(activity.supportFragmentManager.fragments[0].isResumed)
            assertTrue(activity.supportFragmentManager.fragments[0].isVisible)
        }
        assertEquals(1, mockWebServer.requestCount)
        Espresso.onView(withId(R.id.jokeTextView))
            .check(
                ViewAssertions.matches(
                    AllOf.allOf(
                        ViewMatchers.withText(firstJoke),
                        ViewMatchers.isDisplayed()
                    )
                )
            )

        // simulate recreate activity (e.g., low resources or rotate screen)
        scenario.recreate()
        Thread.sleep(100) // can replace with a waitFor
        assertEquals(
            1, mockWebServer.requestCount,
            "recreating app should not fetch a new joke"
        )

        // swipe down fetches new joke
        Espresso.onView(withId(R.id.swipeRefresh))
            .perform(ViewActions.swipeDown())
        assertEquals(
            2, mockWebServer.requestCount,
            "swipe should fetch a new joke"
        )
        Espresso.onView(withId(R.id.jokeTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(secondJoke)))
    }

    private fun createResponseBody(joke: String) : String{
        return """
            {
                "id": "1",
                "joke": "$joke",
                "status": 200
            }
        """.trimIndent()
    }
}