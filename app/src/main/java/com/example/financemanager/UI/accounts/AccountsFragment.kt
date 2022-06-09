package com.example.financemanager.UI.accounts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.R
import com.example.financemanager.databinding.FragmentAccountsBinding


class AccountsFragment : Fragment() {

//    private var _binding: FragmentAccountsBinding? = null
//    private val binding
//        get() = _binding!!

    private val binding: FragmentAccountsBinding by viewBinding()

//    private lateinit var accountsViewModel: AccountsViewModel

    private val accountsViewModel: AccountsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textAccounts
        accountsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
    }

}