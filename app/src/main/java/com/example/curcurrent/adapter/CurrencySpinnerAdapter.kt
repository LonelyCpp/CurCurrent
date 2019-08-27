package com.example.curcurrent.adapter

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.example.curcurrent.R
import com.example.curcurrent.model.Currency

class CurrencySpinnerAdapter(
    private val context: Context,
    private val currencies: List<Currency>,
    private val viewTextColor: Int = Color.BLACK,
    private val dropDownTextColor: Int = Color.BLACK
) :
    SpinnerAdapter {

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: View? = convertView

        if (convertView == null) {
            holder = LayoutInflater.from(context)
                .inflate(R.layout.country_spinner_item, parent, false)
        }

        val currencyItem = currencies[position]
        val tvSpinner = holder!!.findViewById<TextView>(R.id.tvSpinner)
        tvSpinner.text = currencyItem.countryName
        tvSpinner.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        tvSpinner.setPadding(10, 10, 10, 10)
        tvSpinner.setTextColor(dropDownTextColor)
        return holder
    }


    override fun getCount(): Int {
        return currencies.size

    }

    override fun isEmpty(): Boolean {
        return currencies.isEmpty()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: View? = convertView

        if (convertView == null) {
            holder = LayoutInflater.from(context)
                .inflate(R.layout.country_spinner_item, parent, false)
        }

        val currencyItem = currencies[position]
        val tvSpinner = holder!!.findViewById<TextView>(R.id.tvSpinner)
        tvSpinner.text = currencyItem.shortForm
        tvSpinner.setTextColor(viewTextColor)

        return holder
    }

    override fun getItem(position: Int): Currency {
        return currencies[position]
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}