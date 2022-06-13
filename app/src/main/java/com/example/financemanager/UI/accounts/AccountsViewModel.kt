package com.example.financemanager.UI.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.R
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountsUseCases: AccountsUseCases
) : ViewModel() {

    private val _accounts = MutableStateFlow(emptyList<Account>())
    val accounts : StateFlow<List<Account>> = _accounts

    private var getAccountsJob: Job? = null

    init {
        getAccounts()

        viewModelScope.launch {
            val account = com.example.financemanager.data.models.Account(
                name = "default",
                color = R.color.red_purple)
            accountsUseCases.addAccount(account)
        }
    }

    private fun getAccounts() {
        getAccountsJob?.cancel()
        getAccountsJob = accountsUseCases.getAccounts()
            .onEach { accounts ->
                _accounts.value = accounts
            }
            .launchIn(viewModelScope)
    }
}