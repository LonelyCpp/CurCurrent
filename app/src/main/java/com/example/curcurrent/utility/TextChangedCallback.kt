package com.example.curcurrent.utility

import android.text.Editable
import android.text.TextWatcher

class TextChangedCallback(private val callback: () -> (Unit)) : TextWatcher{
    override fun afterTextChanged(s: Editable?) {
        // no-op
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // no-op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        callback()
    }
}