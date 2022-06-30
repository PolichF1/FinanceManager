package com.example.financemanager.UI.account_filter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.MainActivityViewModel
import com.example.financemanager.R
import com.example.financemanager.UI.accounts.AccountsRecyclerAdapter
import com.example.financemanager.databinding.DialogFragmentAccountFilterBinding
import com.example.financemanager.utils.PRIMARY_COLOR
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

        binding.listOfAccounts.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(getDivider())
        }

        binding.allAccountsCurrency.text = viewModel.getPreferences().getString(
            "currency",
            requireContext().resources.getStringArray(R.array.currency_values)[0]
        )

        DrawableCompat.setTint(
            binding.allAccountsIconColor.drawable,
            Color.parseColor(activityViewModel.currentAccount.value?.color ?: PRIMARY_COLOR)
        )

        binding.allAccountsItem.setOnClickListener {
            activityViewModel.setCurrentAccount(null)
            dismiss()
        }

        accountsAdapter.setOnClickListener(AccountsRecyclerAdapter.OnClickListener {
            viewModel.selectAccount(it)
            dismiss()
        })

        lifecycleScope.launchWhenStarted {
            viewModel.accounts.collectLatest { newList ->
                accountsAdapter.submitList(newList)
                var amount = 0.0
                newList.forEach { amount += it.amount }

                binding.allAccountsAmount.text = amount.toAmountFormat(withMinus = false)
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
}