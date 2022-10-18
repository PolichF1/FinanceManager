package com.example.financemanager.ui.accounts.account_filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.MainActivityViewModel
import com.example.financemanager.R
import com.example.financemanager.ui.accounts.AccountsRecyclerAdapter
import com.example.financemanager.databinding.DialogFragmentAccountFilterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.example.financemanager.utils.Utils.getDivider
import com.example.financemanager.utils.Utils.setTint

@AndroidEntryPoint
class AccountFilterFragment : DialogFragment(R.layout.dialog_fragment_account_filter) {

    private val binding: DialogFragmentAccountFilterBinding by viewBinding()

    private val viewModel: AccountFilterViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var accountsAdapter: AccountsRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupAllAccountsItem()
        setupCollectors()
    }

    private fun setupRecyclerView(){
        binding.listOfAccounts.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(getDivider(requireContext()))
        }

        accountsAdapter.setOnClickListener(AccountsRecyclerAdapter.OnClickListener {
            viewModel.selectAccount(it)
            dismiss()
        })
    }

    private fun setupAllAccountsItem() {
        with(binding) {
            allAccountsCurrency.text = activityViewModel.getCurrency()
            allAccountsIconColor.setTint(activityViewModel.selectedAccount.value?.color)
            allAccountsItem.setOnClickListener {
                activityViewModel.setCurrentAccount(null)
                dismiss()
            }
        }
    }

    private fun setupCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.accounts.collectLatest { newList ->
                accountsAdapter.submitList(newList)
                binding.allAccountsAmount.text =
                    viewModel.getFullAmount().toAmountFormat(withMinus = false)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountFilterViewModel.Event.SelectAccount -> {
                        activityViewModel.setCurrentAccount(it.account)
                    }
                }
            }
        }
    }
}