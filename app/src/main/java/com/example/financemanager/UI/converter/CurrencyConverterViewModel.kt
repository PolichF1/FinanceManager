package com.example.financemanager.UI.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.data.repositories.ConverterRepository
import com.example.financemanager.data.useCases.ConvertCurrencyUseCase
import com.example.financemanager.utils.Resource
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

    var resultValue = 0.0

    fun convert(amount: Double, from: String, to: String) {
        viewModelScope.launch {
            _conversion.value = ConversionState.Loading
            resultValue = convertCurrencyUseCase(amount, from, to)
            _conversion.value =
                when (resultValue) {
                    -1.0 -> ConversionState.Error(CONVERTER_ERROR_MESSAGE)
                    -2.0 -> ConversionState.Error(CONVERTER_ERROR_MESSAGE)
                    else -> ConversionState.Ready(resultValue.toAmountFormat(withMinus = false))
                }
        }
    }

    fun convertButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.Convert)
        }
    }

    sealed class Event {
        object Convert : Event()
    }

    sealed class ConversionState {
        class Ready(val result: String) : ConversionState()
        class Error(val error: String) : ConversionState()
        object Loading : ConversionState()
        object Idle : ConversionState()
    }

    companion object {
        private const val CONVERTER_ERROR_MESSAGE = "CONVERTOR ERROR"
    }

}