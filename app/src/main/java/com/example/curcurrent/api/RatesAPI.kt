import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RatesAPI {
    @GET("latest")
    fun currentRates() : Observable<ExchangeRate.Result>

    companion object {
        fun create() : RatesAPI {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl("https://api.ratesapi.io/api/")
                .build()
            return retrofit.create(RatesAPI::class.java)
        }
    }
}