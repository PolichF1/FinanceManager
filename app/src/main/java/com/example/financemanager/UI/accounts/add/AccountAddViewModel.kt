package com.example.financemanager.UI.accounts.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountAddViewModel @Inject constructor(
    private val accountsUseCases: AccountsUseCases
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun addAccount(account: Account) {
        accountsUseCases.addAccount(account)
    }

    fun applyButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.AddAccount)
        }
    }

    sealed class Event {
        object AddAccount : Event()
    }

}