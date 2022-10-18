package com.example.financemanager.ui.transactions.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.useCases.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun addTransaction(transaction: Transaction) {
        addTransactionUseCase(transaction)
    }

    fun applyButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.AddTransaction)
        }
    }

    sealed class Event {
        object AddTransaction : Event()
    }

}