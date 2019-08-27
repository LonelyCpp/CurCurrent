package com.example.curcurrent

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.example.curcurrent.adapter.CurrencySpinnerAdapter
import com.example.curcurrent.model.Currency
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
        CurrencySpinnerAdapter(this, rates, Color.WHITE).also {
            sourceCountrySpinner.adapter = it
        }

        CurrencySpinnerAdapter(this, rates).also {
            destinationCountrySpinner.adapter = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currencyInteractor.loadRates()
        convertBtn.setOnClickListener {
            convert()
        }

        amountInput.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    convert()
                    true
                }
                else -> false
            }
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
        if (src is Currency && dst is Currency && amount.isNotBlank()) {
            val result = currencyInteractor.convert(amount.toDouble(), src, dst)
            resultText.text = "%.4f".format(result)
        }
    }
}