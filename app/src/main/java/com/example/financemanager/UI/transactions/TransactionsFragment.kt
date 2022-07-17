package com.example.financemanager.UI.transactions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.MainActivityViewModel
import com.example.financemanager.R
import com.example.financemanager.data.models.TransactionView
import com.example.financemanager.databinding.FragmentTransactionsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private val binding: FragmentTransactionsBinding by viewBinding()

    private val viewModel: TransactionsViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var transactionAdapter: TransactionsRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.transactionsRecyclerView.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            itemAnimator = null
        }

        binding.newTransactionButton.setOnClickListener {
            viewModel.addTransactionClick(
                activityViewModel.selectedAccount.value ?: activityViewModel.accounts.value[0]
            )
        }

        transactionAdapter.setOnDeleteClickListener(TransactionsRecyclerAdapter.OnDeleteClickListener {
            viewModel.deleteButtonClick(it)
        })

        lifecycleScope.launchWhenStarted {
            viewModel.transactionsUiState.collectLatest {
                when (it) {
                    is TransactionsViewModel.UiState.Ready -> {
                        binding.progressBar.isVisible = false
                        binding.noTransaction.visibility =
                            if (it.transactions.isEmpty()) View.VISIBLE else View.INVISIBLE

                        transactionAdapter.submitList(it.transactions)
                    }
                    is TransactionsViewModel.UiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            activityViewModel.selectedDateRange.collectLatest {
                viewModel.setDateRange(it.first, it.second)
            }
        }

        lifecycleScope.launchWhenStarted {
            activityViewModel.selectedAccount.collectLatest {
                viewModel.setSelectedAccount(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is TransactionsViewModel.Event.OpenTheAddTransactionSheet -> {
                        if (getCurrentDestination() == this@TransactionsFragment.javaClass.name) {
                            findNavController().navigate(
                                TransactionsFragmentDirections.actionTransactionsFragmentToSelectCategorySheetFragment(
                                    it.account
                                )
                            )
                        }
                    }
                    is TransactionsViewModel.Event.SelectDate -> {
                        if (getCurrentDestination() == this@TransactionsFragment.javaClass.name) {
                            findNavController().navigate(
                                TransactionsFragmentDirections.actionTransactionsFragmentToSelectDateDialogFragment()
                            )
                        }
                    }
                    is TransactionsViewModel.Event.ShowTheDeleteTransactionDialog -> {
                        if (!alertIsShowing) {
                            showAlertDialog(it.transaction)
                        }
                    }
                    is TransactionsViewModel.Event.DeleteTransaction -> {
                        viewModel.deleteTransaction(it.transaction)
                    }
                }
            }
        }
    }

    private var currentCurrency: String = ""

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        if (currentCurrency != activityViewModel.getCurrency())
            transactionAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        currentCurrency = activityViewModel.getCurrency()
    }

    private var alertIsShowing = false

    private fun showAlertDialog(transaction: TransactionView) {
        alertIsShowing = true

        val alert = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_warning)
            .setTitle(getString(R.string.alert_transaction_title))
            .setMessage("From: ${transaction.categoryName}\nTo: ${transaction.accountName}\nAmount: ${transaction.amount}")
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                viewModel.deleteConfirmationButtonClick(transaction)
                transactionAdapter.clearSelectedPosition()
                alertIsShowing = false
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                alertIsShowing = false
            }
            .setOnCancelListener { alertIsShowing = false }
            .create()
        alert.show()
    }

    override fun onPause() {
        super.onPause()
        transactionAdapter.clearSelectedPosition()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.date_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.select_date) {
            viewModel.selectDateClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className

}