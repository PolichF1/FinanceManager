package com.example.financemanager.UI.accounts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.R
import com.example.financemanager.databinding.FragmentAccountsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private val binding: FragmentAccountsBinding by viewBinding()
    private val accountsViewModel: AccountsViewModel by viewModels()
    private val accountsAdapter = AccountsRecyclerAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_layer, null))
        binding.listOfAccounts.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(itemDecoration)
        }

        lifecycleScope.launch {
            accountsViewModel.accounts.collectLatest { newList ->
                accountsAdapter.setData(newList)
            }
        }
    }

}