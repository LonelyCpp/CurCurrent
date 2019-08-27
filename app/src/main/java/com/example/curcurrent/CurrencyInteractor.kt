package com.example.curcurrent

import android.util.Log
import com.example.curcurrent.model.Currency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CurrencyInteractor(private val view: CurrencyView) {

    var ratesAPI = RatesAPI.create()
    var disposables = CompositeDisposable()
    var rates: List<Currency>? = null
    var currencyMap: HashMap<String, String>? = null


    fun loadRates() {
        view.setLoading(true)
        val subscribe = ratesAPI.currentRates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val rateTable = ArrayList<Currency>()
                for (rate in it.rates) {
                    rateTable.add(
                        Currency(
                            rate.key.toUpperCase(),
                            countryMapper(rate.key.toUpperCase()),
                            rate.value
                        )
                    )
                }

                rateTable.sortBy { item -> item.countryName }

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
                { error -> Log.d("api err", error.toString()) }
            )
        disposables.add(subscribe)
    }

    fun convert(amount: Double, source: Currency, destination: Currency): Double {
        return amount * (destination.rate / source.rate)
    }

    fun disposeObservables() {
        disposables.clear()
    }

    private fun countryMapper(shortForm: String): String {

        if (currencyMap == null) {
            this.loadCurrencyMap()
        }

        return currencyMap!!.getValue(shortForm)
    }

    private fun loadCurrencyMap() {
        val context = view.getContext()
        val json: String = context.resources
            .openRawResource(R.raw.currency_codes)
            .bufferedReader()
            .use {
                it.readText()
            }
        val gson = Gson()
        currencyMap = gson.fromJson(json, object : TypeToken<HashMap<String, Any>>() {

        }.type)
    }
}