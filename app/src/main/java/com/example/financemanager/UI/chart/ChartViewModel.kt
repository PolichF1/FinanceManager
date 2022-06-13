package com.example.financemanager.UI.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChartViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is categories Fragment"
    }
    val text: LiveData<String> = _text

}