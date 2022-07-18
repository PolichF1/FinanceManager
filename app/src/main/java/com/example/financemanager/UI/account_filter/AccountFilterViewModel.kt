package com.example.financemanager.UI.account_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.GetAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountFilterViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
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
        getAccountsJob = getAccountsUseCase()
            .onEach { accounts ->
                _accounts.value = accounts
            }
            .launchIn(viewModelScope)
    }

    fun selectAccount(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.SelectAccount(account))
        }
    }

    fun getFullAmount(): Double {
        return _accounts.value.sumOf { it.amount }
    }

    sealed class Event {
        data class SelectAccount(val account: Account) : Event()
    }

}