package com.example.financemanager.ui.accounts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.MainActivityViewModel
import com.example.financemanager.R
import com.example.financemanager.databinding.FragmentAccountsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.example.financemanager.utils.Utils.getDivider

@AndroidEntryPoint
class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private val binding: FragmentAccountsBinding by viewBinding()

    private val viewModel: AccountsViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var accountsAdapter: AccountsRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setupRecyclerView()
        setupCollectors()
    }


    private fun setupMenu() {

        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.accounts_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.add_account){
                    viewModel.addButtonClick()
                    true
                } else false
            }

            override fun onMenuClosed(menu: Menu) {
                super.onMenuClosed(menu)
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        binding.listOfAccounts.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(getDivider(requireContext()))
            itemAnimator = null
        }

        accountsAdapter.setOnClickListener(
            AccountsRecyclerAdapter.OnClickListener {
                viewModel.selectAccount(it)
            }
        )
    }

    private fun setupCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.accounts.collectLatest { newList ->
                accountsAdapter.submitList(newList)
                with(binding) {
                    fullAmount.text = viewModel.getFullAmount().toAmountFormat(withMinus = false)
                    mainCurrency.text = activityViewModel.getCurrency()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountsViewModel.Event.NavigateToAddAccountScreen -> {
                        if (getCurrentDestination() == this@AccountsFragment.javaClass.name) {
                            findNavController().navigate(AccountsFragmentDirections.actionAccountsFragmentToAccountAddFragment())
                        }
                    }
                    is AccountsViewModel.Event.OpenTheAccountActionsSheet -> {
                        if (getCurrentDestination() == this@AccountsFragment.javaClass.name) {
                            findNavController().navigate(
                                AccountsFragmentDirections.actionAccountsFragmentToAccountActionsSheetFragment(
                                    it.account, viewModel.accounts.value.size
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private var currentCurrency: String = ""

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        if (currentCurrency != activityViewModel.getCurrency()) {
            binding.mainCurrency.text = activityViewModel.getCurrency()
            accountsAdapter.notifyDataSetChanged()
        }
    }

    override fun onStop() {
        super.onStop()
        currentCurrency = activityViewModel.getCurrency()
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className
}