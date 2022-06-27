package com.example.financemanager.UI.accounts.add

import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountsUseCases
import com.example.financemanager.utils.PRIMARY_COLOR
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

    fun selectColorClick(image: ImageView) {
        viewModelScope.launch {
            val colorInt = image.imageTintList?.defaultColor
            val color = if (colorInt != null) String.format(
                "#%06X",
                0xFFFFFF and colorInt
            ) else PRIMARY_COLOR
            _events.emit(Event.SelectColor(color))
        }
    }

    sealed class Event {
        object AddAccount : Event()
        data class SelectColor(val color: String) : Event()
    }

}