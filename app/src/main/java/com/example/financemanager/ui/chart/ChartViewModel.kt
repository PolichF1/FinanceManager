package com.example.financemanager.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.data.useCases.GetCategoryViewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class ChartViewModel @Inject constructor(
    private val getCategoryViewsUseCase: GetCategoryViewsUseCase
) : ViewModel() {

    private val _categoryViews = MutableStateFlow(emptyList<CategoryView>())
    val categoryViews = _categoryViews.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    private val _selectedDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(null to null)

    private var getCategoriesView: Job? = null

    init {
        getCategoryViews()
    }

    private fun getCategoryViews() {
        getCategoriesView?.cancel()
        getCategoriesView =
            getCategoryViewsUseCase(_selectedDateRange.value, _selectedAccount.value)
                .onEach { categories -> _categoryViews.value = categories }
                .launchIn(viewModelScope)
    }

    fun setDateRange(from: LocalDate?, to: LocalDate?) {
        _selectedDateRange.value = from to to
        getCategoryViews()
    }

    fun setSelectedAccount(account: Account? = null) {
        _selectedAccount.value = account
        getCategoryViews()
    }

    fun selectDateClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }

    sealed class Event {
        object SelectDate : Event()
    }
}