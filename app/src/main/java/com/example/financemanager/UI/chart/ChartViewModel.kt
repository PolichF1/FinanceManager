package com.example.financemanager.UI.chart

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.data.useCases.CategoryUseCases
import com.example.financemanager.data.useCases.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChartViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _categoriesWithAmount = MutableStateFlow(emptyList<CategoryView>())
    val categoriesWithAmount = _categoriesWithAmount.asStateFlow()

    private var getCategoriesWithAmountJob: Job? = null

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        getCategoriesWithAmount()
    }

    private fun getCategoriesWithAmount() {
        getCategoriesWithAmountJob?.cancel()
        getCategoriesWithAmountJob = categoryUseCases.getCategoryViews()
            .onEach { categories -> _categoriesWithAmount.value = categories }
            .launchIn(viewModelScope)
    }

    fun getPreferences() = sharedPreferences

    fun selectDateCLick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }



    sealed class Event {
        object SelectDate: Event()
    }

}