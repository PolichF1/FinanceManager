package com.example.financemanager.UI.transactions.add.select_category

import android.content.SharedPreferences
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.data.useCases.CategoryUseCases
import com.example.financemanager.utils.mapOfDrawables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private var getCategoryViewsJob: Job? = null

    private val _categoryViews = MutableStateFlow(emptyList<CategoryView>())
    val categoryViews = _categoryViews.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        getCategoryViews()
    }

    private fun getCategoryViews(from: LocalDate? = null, to: LocalDate? = null) {
        getCategoryViewsJob?.cancel()
        getCategoryViewsJob =
            categoryUseCases.getCategoryViews(from, to)
                .onEach { categories -> _categoryViews.value = categories }
                .launchIn(viewModelScope)
    }

    fun setDateRange(from: LocalDate?, to: LocalDate?) {
        getCategoryViews(from, to)
    }

    fun selectCategoryClick(account: Account, categoryView: CategoryView) {
        viewModelScope.launch {
            _events.emit(Event.SelectCategory(account, categoryView))
        }
    }

    fun getPreferences() = sharedPreferences

    sealed class Event {
        data class SelectCategory(val account: Account, val category: CategoryView) : Event()
    }
}