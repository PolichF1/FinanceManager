package com.example.financemanager.UI.transactions.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.useCases.CategoryUseCases
import com.example.financemanager.data.useCases.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionsUseCases: TransactionUseCases
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()


    suspend fun addTransaction(transaction: Transaction) {
        transactionsUseCases.addTransaction(transaction)
    }

    fun applyButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.AddTransaction)
        }
    }


    sealed class Event {
        object AddTransaction: Event()
    }
}