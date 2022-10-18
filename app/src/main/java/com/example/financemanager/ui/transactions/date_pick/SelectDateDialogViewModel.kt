package com.example.financemanager.ui.transactions.date_pick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.DateUtils.DAY_IN_MS
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.DateUtils.toLocalDate
import com.example.financemanager.DateUtils.toMilliseconds

class SelectDateViewModel : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun getTheDate (daysAgo :Int = 0): LocalDate {
        return if (daysAgo != 0)
            (getCurrentLocalDate().toMilliseconds() - (daysAgo * DAY_IN_MS)).toLocalDate()
        else getCurrentLocalDate()
    }

    fun selectDateClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }

    fun selectTodayClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectToday)
        }
    }

    fun selectWeekClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectWeek)
        }
    }

    fun selectMonthClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectMonth)
        }
    }

    fun selectYearClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectYear)
        }
    }

    fun selectAllTimeClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectAllTime)
        }
    }

    sealed class Event {
        object SelectDate : Event()
        object SelectToday : Event()
        object SelectWeek : Event()
        object SelectMonth : Event()
        object SelectYear : Event()
        object SelectAllTime : Event()
    }
}