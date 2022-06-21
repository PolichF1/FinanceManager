package com.example.financemanager.UI.accounts.add

import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.useCases.AccountsUseCases
import com.example.financemanager.utils.mapOfColors
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
            var colorId: Int? = null
            var imageId = ImageViewCompat.getImageTintList(image)?.defaultColor
            mapOfColors.forEach { entry ->
                if (entry.value == imageId)
                    colorId = entry.key
            }
            _events.emit(Event.SelectColor(colorId))
        }
    }

    sealed class Event {
        object AddAccount : Event()
        data class SelectColor(val id: Int?) : Event()
    }

}