package com.example.financemanager.UI.account_filter

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.MainActivityViewModel
import com.example.financemanager.R
import com.example.financemanager.UI.accounts.AccountsRecyclerAdapter
import com.example.financemanager.databinding.DialogFragmentAccountFilterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AccountFilterFragment : DialogFragment(R.layout.dialog_fragment_account_filter) {

    private val binding: DialogFragmentAccountFilterBinding by viewBinding()

    private val viewModel: AccountFilterViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var accountsAdapter: AccountsRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountsAdapter.setOnClickListener(AccountsRecyclerAdapter.OnClickListener{ account ->
            viewModel.selectAccount(account)
            dismiss()
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

        binding.allAccountsItem.setOnClickListener {
            activityViewModel.setCurrentAccount(null)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is AccountFilterViewModel.Event.SelectAccount -> {
                        activityViewModel.setCurrentAccount(event.account)
                    }
                }
            }
        }
    }

    private fun getDivider() = DividerItemDecoration (
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
}