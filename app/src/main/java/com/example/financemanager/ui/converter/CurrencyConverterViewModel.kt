package com.example.financemanager.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.data.useCases.ConvertCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModel() {

    private val _conversion = MutableStateFlow<ConversionState>(ConversionState.Idle)
    val conversion = _conversion.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun convert(amount: Double, from: String, to: String) {
        viewModelScope.launch {
            _conversion.value = ConversionState.Loading
            val value = convertCurrencyUseCase(amount, from, to)
            _conversion.value =
                when (value) {
                    -1.0 -> ConversionState.Error("Unexpected error")
                    -2.0 -> ConversionState.Error("Check your internet connection")
                    else -> ConversionState.Ready(value.toAmountFormat(withMinus = false))
                }
        }
    }

    fun convertButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.Convert)
        }
    }

    sealed class ConversionState {
        class Ready(val result: String) : ConversionState()
        class Error(val error: String) : ConversionState()
        object Loading : ConversionState()
        object Idle : ConversionState()
    }

    sealed class Event {
        object Convert : Event()
    }

}