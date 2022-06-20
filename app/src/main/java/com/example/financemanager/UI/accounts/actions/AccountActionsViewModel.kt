package com.example.financemanager.UI.accounts.actions

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
class AccountActionsViewModel @Inject constructor(
    private val accountUseCases: AccountsUseCases
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
            _events.emit(Event.ShowDeleteAccountDialog(account))
        }
    }

    fun deleteConfirmButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.DeleteAccount)
        }
    }

    suspend fun deleteAccount(account: Account) {
        accountUseCases.deleteAccount(account)
    }


    sealed class Event {
        data class NavigateToEditAccountScreen (val account: Account) : Event()
        data class ShowDeleteAccountDialog (val account: Account): Event()
        object DeleteAccount : Event()
    }

}