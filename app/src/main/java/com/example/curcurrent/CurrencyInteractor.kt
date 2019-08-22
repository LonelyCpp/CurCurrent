package com.example.curcurrent

import android.util.Log
import com.example.curcurrent.model.Currency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CurrencyInteractor(private val view : CurrencyView){

    var ratesAPI = RatesAPI.create()
    var disposables = CompositeDisposable()
    var rates:List<Currency>? = null

    fun loadRates(){
        view.setLoading(true)
        val subscribe = ratesAPI.currentRates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map{
                val rateTable = ArrayList<Currency>()
                for(rate in it.rates){
                    rateTable.add(
                        Currency(rate.key.toUpperCase(), "test", rate.value)
                    )
                }
                rateTable
            }
            .subscribe(
                { result ->
                    run {
                        rates = result
                        view.fillRatesData(result)
                        view.setLoading(false)
                    }
                },
                { error -> Log.d("api", error.localizedMessage) }
            )
        disposables.add(subscribe)
    }

    fun convert(amount: Double, source: Currency, destination: Currency): Double{
        return amount * (destination.rate / source.rate)
    }

    fun disposeObservaples(){
        disposables.clear()
    }
}