package com.example.financemanager.ui.accounts.edit

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.UpdateAccountUseCase
import com.example.financemanager.utils.Utils.getImageViewTint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountEditViewModel @Inject constructor(
    private val updateAccountUseCase: UpdateAccountUseCase
) : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun updateAccount(account: Account) {
        updateAccountUseCase(account)
    }

    fun applyButtonClick() {
        viewModelScope.launch {
            _events.emit(Event.UpdateAccount)
        }
    }


    fun selectColorClick(image: ImageView) {
        viewModelScope.launch {
            val color = getImageViewTint(image)
            _events.emit(Event.SelectColor(color))
        }
    }

    sealed class Event {
        object UpdateAccount : Event()
        data class SelectColor(val color: String) : Event()
    }
}