package com.example.financemanager.UI.accounts

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountsUseCases
import com.example.financemanager.data.useCases.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val accountUseCase: AccountsUseCases
) : ViewModel() {

    private val _accounts = MutableStateFlow(emptyList<Account>())
    val accounts = _accounts.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private var getAccountsJob: Job? = null

    init {
        getAccounts()
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountUseCase.getAccounts()
            .onEach { accounts -> _accounts.value = accounts }
            .launchIn(viewModelScope)
    }

    fun selectAccount(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.OpenAccountActionSheet(account))
        }
    }

    fun addButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.NavigateToAddAccountScreen)
        }
    }

    fun getPreferences() = sharedPreferences

    sealed class Event() {
        object NavigateToAddAccountScreen : Event()
        data class OpenAccountActionSheet (val account: Account) : Event()
    }
}