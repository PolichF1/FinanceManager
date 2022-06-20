package com.example.financemanager.UI.accounts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.R
import com.example.financemanager.databinding.FragmentAccountsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private val binding: FragmentAccountsBinding by viewBinding()

    private val viewModel: AccountsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val accountsAdapter =
            AccountsRecyclerAdapter(AccountsRecyclerAdapter.OnClickListener { account ->
                viewModel.selectAccount(account)
            })

        binding.listOfAccounts.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(getDivider())
        }

        lifecycleScope.launchWhenCreated {
            viewModel.accounts.collectLatest { newList ->
                accountsAdapter.submitList(newList)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is AccountsViewModel.Event.NavigateToAddAccountScreen -> {
                        if (getCurrentDestination() == this@AccountsFragment.javaClass.name) {
                            findNavController().navigate(
                                AccountsFragmentDirections.actionAccountsFragmentToAccountAddFragment()
                            )
                        }
                    }
                    is AccountsViewModel.Event.OpenAccountActionSheet -> {
                        if (getCurrentDestination() == this@AccountsFragment.javaClass.name) {
                            findNavController().navigate(
                                AccountsFragmentDirections.actionAccountsFragmentToAccountActionsSheetFragment(
                                    event.account
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.accounts_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_account) {
            viewModel.addButtonClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDivider() = DividerItemDecoration(
        requireContext(),
        DividerItemDecoration.VERTICAL
    ).apply {
        setDrawable(
            requireNotNull(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider_layer,
                    null
                )
            )
        )
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className
}