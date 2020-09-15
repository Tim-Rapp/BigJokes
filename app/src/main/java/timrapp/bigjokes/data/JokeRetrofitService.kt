package timrapp.bigjokes.data

import timrapp.bigjokes.BuildConfig
import timrapp.bigjokes.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface JokeRetrofitService {
    @GET("/")
    // magic:  suspend keyword tells Retrofit to use coroutines when generating nextJoke implementation
    suspend fun nextJoke(): Joke

    companion object {
        fun get(): JokeRetrofitService {
            // note -- I ignored the info at https://icanhazdadjoke.com/api#custom-user-agent
            // in favor of the more common "<app name>/<version> <httpAgent>" format
            val httpAgent = System.getProperty("http.agent")

            val client = OkHttpClient.Builder()
                .addInterceptor {
                    it.proceed(
                        it.request().newBuilder()
                            .header("Accept", "application/json")
                            .header(
                                "User-Agent",
                                "${R.string.app_name}/${BuildConfig.VERSION_NAME} $httpAgent"
                            )
                            .build()
                    )
                }
                .build()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(JokeApiConfig.url)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create<JokeRetrofitService>(JokeRetrofitService::class.java)
        }
    }
}
