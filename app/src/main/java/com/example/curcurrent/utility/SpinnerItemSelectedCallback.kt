package com.example.curcurrent.utility

import android.view.View
import android.widget.AdapterView

class SpinnerItemSelectedCallback(private val callback: () -> (Unit)): AdapterView.OnItemSelectedListener{
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // no-op
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        callback()
    }
}