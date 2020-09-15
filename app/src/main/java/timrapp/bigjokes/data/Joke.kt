package timrapp.bigjokes.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Joke(@Json(name="joke") val text: String) {
}