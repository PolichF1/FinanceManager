package com.example.financemanager.UI.transactions.add.select_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.data.useCases.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private var getCategoriesJob: Job? = null

    private val _categories = MutableStateFlow(emptyList<CategoryView>())
    val categories = _categories.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        getCategories()
    }


    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = categoryUseCases.getCategoryViews()
            .onEach { categories ->
                _categories.value = categories
            }
            .launchIn(viewModelScope)
    }

    fun selectCategoryClick(account: Account, categoryView: CategoryView) {
        viewModelScope.launch {
            _events.emit(Event.SelectCategory(account, categoryView))
        }
    }


    sealed class Event {
        data class SelectCategory(val account: Account, val category: CategoryView) : Event()
    }

}