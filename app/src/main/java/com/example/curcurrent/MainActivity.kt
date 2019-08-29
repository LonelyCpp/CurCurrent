package com.example.curcurrent

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.curcurrent.adapter.CurrencySpinnerAdapter
import com.example.curcurrent.model.Currency
import com.example.curcurrent.utility.SpinnerItemSelectedCallback
import com.example.curcurrent.utility.TextChangedCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CurrencyView {

    private val currencyInteractor = CurrencyInteractor(this)

    override fun getContext(): Context {
        return applicationContext
    }

    override fun setLoading(loading: Boolean) {
        srlContainer.isRefreshing = loading
    }

    override fun fillRatesData(rates: List<Currency>) {
        val spinnerItemSelectedCallback = SpinnerItemSelectedCallback(this::convert)



        CurrencySpinnerAdapter(this, rates, Color.WHITE).also {
            var selectedItemId = 0
            if(sourceCountrySpinner.selectedItem != null){
                selectedItemId = sourceCountrySpinner.selectedItemId.toInt()
            }
            sourceCountrySpinner.adapter = it
            sourceCountrySpinner.setSelection(selectedItemId)
            sourceCountrySpinner.onItemSelectedListener = spinnerItemSelectedCallback
        }

        CurrencySpinnerAdapter(this, rates).also {
            var selectedItemId = 0
            if(destinationCountrySpinner.selectedItem != null){
                selectedItemId = destinationCountrySpinner.selectedItemId.toInt()
            }
            destinationCountrySpinner.adapter = it
            destinationCountrySpinner.setSelection(selectedItemId)
            destinationCountrySpinner.onItemSelectedListener = spinnerItemSelectedCallback
        }
        srlContainer.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currencyInteractor.loadRates()
        amountInput.addTextChangedListener(TextChangedCallback(this::convert))
        btnSwapCurrency.setOnClickListener {
            swapCurrencies()
        }
        srlContainer.setOnRefreshListener {
            currencyInteractor.loadRates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currencyInteractor.disposeObservables()
    }

    private fun convert() {
        var amount = amountInput.text.toString()

        val src = sourceCountrySpinner.selectedItem
        val dst = destinationCountrySpinner.selectedItem

        if (amount.isBlank()) {
            amount = "0"
        }

        if (src is Currency && dst is Currency) {
            val result = currencyInteractor.convert(amount.toDouble(), src, dst)
            resultText.text = String.format("%.4f", result)

            tvSourceRate.text = getString(R.string.currency_val, 1f, src.shortForm)
            tvSourceCurrency.text = src.countryName

            tvDestinationRate.text = getString(
                R.string.currency_val,
                currencyInteractor.convert(1.0, src, dst), dst.shortForm
            )
            tvDestinationRateCurrency.text = dst.countryName
        }
    }

    private fun swapCurrencies() {
        val srcIndex = sourceCountrySpinner.selectedItemId
        val dstIndex = destinationCountrySpinner.selectedItemId

        sourceCountrySpinner.setSelection(dstIndex.toInt())
        destinationCountrySpinner.setSelection(srcIndex.toInt())
    }
}