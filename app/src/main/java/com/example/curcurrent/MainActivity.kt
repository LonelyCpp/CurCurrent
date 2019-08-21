package com.example.curcurrent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.curcurrent.model.Currency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var ratesAPI = RatesAPI.create()
    var subscribe : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribe = ratesAPI.currentRates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{
                val rateTable = ArrayList<Currency>()
                for(rate in it.rates){
                    rateTable.add(
                        Currency(rate.key, "test", rate.value)
                    )
                }
                rateTable
            }
            .subscribe(
                { result ->
                    run {
                        Log.d("api", result.toString())
                    }
                },
                { error -> Log.d("api", error.localizedMessage) }
            )

    }

    override fun onDestroy() {
        subscribe?.dispose()
        super.onDestroy()
    }
}
