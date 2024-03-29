package com.example.financemanager.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.TransactionView
import com.example.financemanager.data.useCases.DeleteTransactionByIdUseCase
import com.example.financemanager.data.useCases.GetTransactionViewsUseCase
import com.example.financemanager.data.useCases.GetTransactionsWithDayInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactionViewsUseCase: GetTransactionViewsUseCase,
    private val getTransactionsWithDayInfoUseCase: GetTransactionsWithDayInfoUseCase,
    private val deleteTransactionByIdUseCase: DeleteTransactionByIdUseCase
) : ViewModel() {

    private val _transactionsUiState = MutableStateFlow<UiState>(UiState.Idle)
    val transactionsUiState = _transactionsUiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    private val _selectedDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(null to null)

    private var getTransactionsJob: Job? = null

    init {
        getTransactions()
    }

    private fun getTransactions() {

        _transactionsUiState.value = UiState.Loading

        getTransactionsJob?.cancel()

        val range = _selectedDateRange.value
        val account = _selectedAccount.value

        getTransactionsJob =
            getTransactionViewsUseCase(range, account)
                .onEach { transactions ->
                    val data = getTransactionsWithDayInfoUseCase(
                        transactions,
                        range,
                        account
                    )
                    _transactionsUiState.value = UiState.Ready(data)
                }
                .launchIn(viewModelScope)
    }

    suspend fun deleteTransaction(transaction: TransactionView) {
        deleteTransactionByIdUseCase(transaction)
    }

    fun setDateRange(from: LocalDate? = null, to: LocalDate? = null) {
        _selectedDateRange.value = from to to
        getTransactions()
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

    sealed class UiState {
        data class Ready(val transactions: List<Any>) : UiState()
        object Loading : UiState()
        object Idle : UiState()
    }

    sealed class Event {
        object SelectDate : Event()
        data class OpenTheAddTransactionSheet(val account: Account) : Event()
        data class ShowTheDeleteTransactionDialog(val transaction: TransactionView) : Event()
        data class DeleteTransaction(val transaction: TransactionView) : Event()
    }
}