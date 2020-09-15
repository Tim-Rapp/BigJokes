package timrapp.bigjokes.data

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoTestingIdlingResource {
    private const val resource = "GLOBAL"

    val idlingResource = CountingIdlingResource(resource)

    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        idlingResource.decrement()
    }

}
