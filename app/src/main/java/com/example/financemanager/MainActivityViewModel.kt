package com.example.financemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val accountsUseCases: AccountsUseCases
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

    private var getAccountJob: Job? = null


    private fun getAccounts() {
        getAccountJob?.cancel()
        getAccountJob = accountsUseCases.getAccounts()
            .onEach { accounts ->
                _accounts.value = accounts
            }
            .launchIn(viewModelScope)
    }

    fun settingsButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.OpenSettingsScreen)
        }
    }

    fun selectAccountButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.OpenSelectAccountDialog)
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


    sealed class Event {
        object OpenSettingsScreen: Event()
        object OpenSelectAccountDialog: Event()
    }

}