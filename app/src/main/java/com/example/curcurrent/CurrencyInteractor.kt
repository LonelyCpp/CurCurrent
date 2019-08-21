package com.example.curcurrent

import android.util.Log
import com.example.curcurrent.model.Currency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CurrencyInteractor(val view : CurrencyView){

    var ratesAPI = RatesAPI.create()
    var disposables = CompositeDisposable()

    fun loadRates(){
        view.setLoading(true)
        val subscribe = ratesAPI.currentRates()
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
                        view.setLoading(false)
                    }
                },
                { error -> Log.d("api", error.localizedMessage) }
            )
        disposables.add(subscribe)
    }

    fun disposeObservaples(){
        disposables.clear()
    }
}