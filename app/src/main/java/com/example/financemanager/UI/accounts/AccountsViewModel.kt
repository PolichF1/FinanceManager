package com.example.financemanager.UI.accounts

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountUseCase: AccountUseCases
) : ViewModel() {

    private var getAccountsJob: Job? = null

    private val _accounts = MutableStateFlow(emptyList<Account>())
    val accounts = _accounts.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCase.getAccounts()
            .onEach { accounts ->
                _accounts.value = accounts
            }
            .launchIn(viewModelScope)
    }

    fun selectAccount(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.OpenTheAccountActionsSheet(account))
        }
    }

    fun addButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.NavigateToAddAccountScreen)
        }
    }

    fun getFullAmount(): Double {
        return _accounts.value.sumOf { it.amount }
    }

    sealed class Event {
        object NavigateToAddAccountScreen : Event()
        data class OpenTheAccountActionsSheet(val account: Account) : Event()
    }
}