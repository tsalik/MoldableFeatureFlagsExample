package blog.tsalikis.exploringfeatureflags.data

import blog.tsalikis.exploringfeatureflags.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FeatureApi {
    @GET("/api/features/")
    suspend fun getFeatures(
        @Query("country") country: String
    ): FeatureResponse
}

@JsonClass(generateAdapter = true)
data class FeatureResponse(
    @Json(name = "showWelcomeMessage") val showWelcomeMessage: Boolean,
)

object ApiService {
    private val client by lazy {
        val builder = OkHttpClient.Builder()
            .addInterceptor(BuildNumberInterceptor())

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }

        builder.build()
    }

    val retrofit: FeatureApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .build()
        .create(FeatureApi::class.java)
}

class BuildNumberInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Build-Number", BuildConfig.VERSION_CODE.toString())
            .build()
        return chain.proceed(request)
    }

}

