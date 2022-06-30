package com.example.financemanager

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val accountUseCases: AccountUseCases
) : ViewModel() {

    private val _accounts = MutableStateFlow(emptyList<Account>())
    val accounts = _accounts.asStateFlow()

    private val _currentAccount = MutableStateFlow<Account?>(null)
    val currentAccount = _currentAccount.asStateFlow()

    private val _currentDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(
        getCurrentLocalDate() to getCurrentLocalDate()
    )
    val currentDateRange = _currentDateRange.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private var getAccountsJob: Job? = null

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCases.getAccounts()
            .onEach { accounts ->
                _accounts.value = accounts
            }
            .launchIn(viewModelScope)
    }

    fun settingsButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.OpenTheSettingsScreen)
        }
    }

    fun selectAccountButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.OpenTheSelectAccountDialog)
        }
    }

    fun setCurrentAccount(account: Account?) {
        viewModelScope.launch {
            _currentAccount.value = account
        }
    }

    fun setCurrentDateRange(begin: LocalDate?, end: LocalDate?) {
        viewModelScope.launch {
            _currentDateRange.value = begin to end
        }
    }

    fun getPreferences() = sharedPreferences

    sealed class Event {
        object OpenTheSettingsScreen : Event()
        object OpenTheSelectAccountDialog : Event()
    }
}