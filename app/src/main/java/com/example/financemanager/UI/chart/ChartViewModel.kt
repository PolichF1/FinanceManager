package com.example.financemanager.UI.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ChartViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _categories = MutableStateFlow(emptyList<Category>())
    val categories: StateFlow<List<Category>> = _categories

    private val _transactions = MutableStateFlow(emptyList<Transaction>())
    val transactions : StateFlow<List<Transaction>> = _transactions

    val combineFlow = _categories.combine(_transactions) { categories, transactions ->
        categories to transactions
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private var getCategoriesJob: Job? = null
    private var getTransactionsJob: Job? = null

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        getCategories()
        getTransactions()
    }

    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = categoryUseCases.getCategories()
            .onEach { categories -> _categories.value = categories }
            .launchIn(viewModelScope)
    }

    private fun getTransactions() {
        getTransactionsJob?.cancel()
        getTransactionsJob = transactionUseCases.getTransactions()
            .onEach { transactions -> _transactions.value = transactions }
            .launchIn(viewModelScope)
    }

    fun selectDateCLick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }



    sealed class Event {
        object SelectDate: Event()
    }

}