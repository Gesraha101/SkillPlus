import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "http://172.16.1.188:2000/" //10.0.0.2.2 //192.168.1.6
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
