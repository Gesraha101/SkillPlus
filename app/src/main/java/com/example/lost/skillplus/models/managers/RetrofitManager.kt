import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "http://10.1.41.86:2000/"
    private val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }

            return retrofit
        }

    fun getInstance() : Retrofit? {
        return retrofitInstance
    }
}
