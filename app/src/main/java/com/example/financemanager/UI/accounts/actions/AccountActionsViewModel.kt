package com.example.financemanager.UI.accounts.actions

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountActionsViewModel @Inject constructor(
    private val accountsUseCases: AccountUseCases
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun editButtonClick(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.NavigateToEditAccountScreen(account))
        }
    }

    fun deleteButtonClick(account: Account) {
        viewModelScope.launch {
            _events.emit(Event.ShowTheDeleteAccountDialog(account))
        }
    }

    fun deleteConfirmationButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.DeleteAccount)
        }
    }

    suspend fun deleteAccount(account: Account) {
        accountsUseCases.deleteAccount(account)
    }

    sealed class Event {
        data class NavigateToEditAccountScreen(val account: Account) : Event()
        data class ShowTheDeleteAccountDialog(val account: Account) : Event()
        object DeleteAccount : Event()
    }
}