package com.example.financemanager.UI.transactions

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.MainActivityViewModel
import com.example.financemanager.R
import com.example.financemanager.databinding.FragmentTransactionsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

        binding.listOfTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }

        binding.newTransactionButton.setOnClickListener {
            viewModel.addTransactionClick(
                activityViewModel.currentAccount.value ?: activityViewModel.accounts.value[0]
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.transactions.collectLatest {
                transactionAdapter.updateData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is TransactionsViewModel.Event.OpenAddTransactionSheet -> {
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
                }
            }
        }
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