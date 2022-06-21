package com.example.financemanager.UI.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.useCases.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
   private val transactionUseCases: TransactionUseCases
): ViewModel() {

    private val _transactions = MutableStateFlow(emptyList<Transaction>())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private var getTransactionsJob: Job? = null

    init {
        getTransactions()
    }

    private fun getTransactions() {
        getTransactionsJob?.cancel()
        getTransactionsJob = transactionUseCases.getTransactions()
            .onEach { transactions -> _transactions.value = transactions }
            .launchIn(viewModelScope)
    }

    fun selectDateClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }

    fun addTransactionClick(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.OpenAddTransactionSheet(account))
        }
    }

    sealed class Event {
        object SelectDate: Event()
        data class OpenAddTransactionSheet(val account: Account): Event()
    }


}