package com.example.curcurrent

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
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
        if (loading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currencyInteractor.loadRates()
        convertBtn.setOnClickListener {
            convert()
        }

        amountInput.addTextChangedListener(TextChangedCallback(this::convert))
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
}