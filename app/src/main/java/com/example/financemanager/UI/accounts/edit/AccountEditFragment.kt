package com.example.financemanager.UI.accounts.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.R
import com.example.financemanager.databinding.FragmentAccountEditBinding


class AccountEditFragment : Fragment(R.layout.fragment_account_edit) {

    private val binding: FragmentAccountEditBinding by viewBinding()

    private val viewModel: AccountEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}