package com.example.curcurrent

import com.example.curcurrent.model.Currency

interface CurrencyView {
    fun setLoading(loading : Boolean)
    fun fillRatesData(rates : List<Currency>)
    fun getContext()
}