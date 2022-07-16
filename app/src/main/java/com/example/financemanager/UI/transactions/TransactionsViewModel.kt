package com.example.financemanager.UI.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.TransactionView
import com.example.financemanager.data.useCases.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _transactionsWithDayInfo = MutableStateFlow(emptyList<Any>())
    val transactionsWithDayInfo: StateFlow<List<Any>> = _transactionsWithDayInfo

    private var getTransactionsJob: Job? = null

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    private val _selectedDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(null to null)

    init {
        getTransactions()
    }

    private fun getTransactions(from: LocalDate? = null, to: LocalDate? = null) {
        getTransactionsJob?.cancel()

        val range = _selectedDateRange.value
        val account = _selectedAccount.value

        getTransactionsJob =
            transactionUseCases.getTransactionViews(range, account)
                .onEach { transactions ->
                    _transactionsWithDayInfo.value =
                        transactionUseCases.getTransactionListWithDayInfo(
                            transactions,
                            range,
                            account)
                }
                .launchIn(viewModelScope)
    }

    fun setDateRange(from: LocalDate? = null, to: LocalDate? = null) {
        _selectedDateRange.value = from to to
        getTransactions(from, to)
    }

    fun setSelectedAccount(account: Account? = null) {
        _selectedAccount.value = account
        getTransactions()
    }

    fun selectDateClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }

    fun addTransactionClick(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.OpenTheAddTransactionSheet(account))
        }
    }

    fun deleteButtonClick(transaction: TransactionView) {
        viewModelScope.launch {
            _events.emit(Event.ShowTheDeleteTransactionDialog(transaction))
        }
    }

    fun deleteConfirmationButtonClick(transaction: TransactionView) {
        viewModelScope.launch {
            _events.emit(Event.DeleteTransaction(transaction))
        }
    }

    suspend fun deleteTransaction(transaction: TransactionView) {
        transactionUseCases.deleteTransactionById(transaction)
    }

    sealed class Event {
        object SelectDate : Event()
        data class OpenTheAddTransactionSheet(val account: Account) : Event()
        data class ShowTheDeleteTransactionDialog(val transaction: TransactionView): Event()
        data class DeleteTransaction(val transaction: TransactionView): Event()
    }
}