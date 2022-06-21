package com.example.financemanager.UI.transactions.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.Category
import com.example.financemanager.data.useCases.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private var getCategoriesJob: Job? = null

    private val _categories = MutableStateFlow(emptyList<Category>())
    val categories = _categories.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        getCategories()
    }

    private fun  getCategories () {
        getCategoriesJob?.cancel()
        getCategoriesJob = categoryUseCases.getCategories()
            .onEach { categories ->
                _categories.value = categories
            }
            .launchIn(viewModelScope)
    }

    fun selectCategoryClick(account: Account, category: Category) {
        viewModelScope.launch {
            _events.emit(Event.SelectCategory(account, category))
        }
    }

    sealed class Event {
        data class SelectCategory (val account: Account, val category: Category): Event()
    }
}