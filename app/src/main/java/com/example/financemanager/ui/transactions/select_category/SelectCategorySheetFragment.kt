package com.example.financemanager.ui.transactions.select_category

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.MainActivityViewModel
import com.example.financemanager.databinding.FragmentSelectCategorySheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class SelectCategorySheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSelectCategorySheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SelectCategoryViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var categoriesRecyclerAdapter: CategoriesRecyclerAdapter

    private val args by navArgs<SelectCategorySheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectCategorySheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupContainerAppearance()
        setupRecyclerView()
        setupCollectors()
    }

    private fun setupContainerAppearance() {
        val account = args.selectedAccount
        with(binding) {
            accountName.text = account.name
            accountAmount.text = account.amount.toAmountFormat(withMinus = false)
            accountCurrency.text = activityViewModel.getCurrency()
            actionsContainer.setBackgroundColor(Color.parseColor(account.color))
        }
    }

    private fun setupRecyclerView() {
        binding.gridOfCategories.apply {
            adapter = categoriesRecyclerAdapter
            itemAnimator = null
        }

        categoriesRecyclerAdapter.setOnClickListener(
            CategoriesRecyclerAdapter.OnClickListener {
                viewModel.selectCategoryClick(args.selectedAccount, it)
            }
        )
    }

    private fun setupCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.categoryViews.collectLatest { newList ->
                categoriesRecyclerAdapter.submitList(newList)
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
                    is SelectCategoryViewModel.Event.SelectCategory -> {
                        if (getCurrentDestination() == this@SelectCategorySheetFragment.javaClass.name) {
                            findNavController().navigate(
                                SelectCategorySheetFragmentDirections.actionSelectCategorySheetFragmentToAddTransactionSheetFragment(
                                    it.account,
                                    it.category
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className
}