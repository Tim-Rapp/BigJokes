package timrapp.bigjokes.data

object JokeApiConfig {
    // for prod app I'd put this in buildconfig and when testing I'd
    // use powermock to override the url value
    var url = "https://icanhazdadjoke.com/"
}