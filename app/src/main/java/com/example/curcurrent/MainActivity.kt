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
            sourceCountrySpinner.adapter = it
            sourceCountrySpinner.onItemSelectedListener = spinnerItemSelectedCallback
        }

        CurrencySpinnerAdapter(this, rates).also {
            destinationCountrySpinner.adapter = it
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
        val amount = amountInput.text.toString()
        val src = sourceCountrySpinner.selectedItem
        val dst = destinationCountrySpinner.selectedItem
        if (amount.isBlank()) {
            resultText.text = getString(R.string.zero)
        } else if (src is Currency && dst is Currency) {
            val result = currencyInteractor.convert(amount.toDouble(), src, dst)
            resultText.text = "%.4f".format(result)
        }
    }

    private fun swapCurrencies() {
        val srcIndex = sourceCountrySpinner.selectedItemId
        val dstIndex = destinationCountrySpinner.selectedItemId

        sourceCountrySpinner.setSelection(dstIndex.toInt())
        destinationCountrySpinner.setSelection(srcIndex.toInt())
    }
}