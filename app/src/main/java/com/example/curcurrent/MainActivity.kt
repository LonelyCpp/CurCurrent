package com.example.curcurrent

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.curcurrent.model.Currency
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CurrencyView {

    val currencyInteractor = CurrencyInteractor(

        this)

    override fun getContext(): Context {
        return getContext()
    }

    override fun setLoading(loading: Boolean) {
        if(loading){
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun fillRatesData(rates: List<Currency>) {
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            rates
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            sourceCountrySpinner.adapter = arrayAdapter
        }
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            rates
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            destinationCountrySpinner.adapter = arrayAdapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currencyInteractor.loadRates()
        convertBtn.setOnClickListener {
            val amount = amountInput.text.toString()
            val src = sourceCountrySpinner.selectedItem
            val dst = destinationCountrySpinner.selectedItem
            if(src is Currency && dst is Currency && amount.isNotBlank()){
                val result = currencyInteractor.convert(amount.toDouble(), src, dst)
                resultText.text = result.toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currencyInteractor.disposeObservaples()
    }
}
