package com.example.financemanager.UI.transactions.add.select_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.financemanager.R
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.databinding.FragmentSelectCategorySheetBinding
import com.example.financemanager.toAmountFormat
import com.example.financemanager.utils.mapOfColors
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class SelectCategorySheetFragment : BottomSheetDialogFragment() {


    private var _binding : FragmentSelectCategorySheetBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SelectCategoryViewModel by viewModels()

    @Inject
    lateinit var categoriesRecyclerAdapter: CategoriesRecyclerAdapter

    private val args by navArgs<SelectCategorySheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectCategorySheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = args.selectedAccount

        binding.accountName.text = account.name
        binding.accountAmount.text = account.amount.toAmountFormat(withMinus = false)

        binding.actionsContainer.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                mapOfColors[account.color] ?: R.color.orange_red
            )
        )

        categoriesRecyclerAdapter.setOnClickListener(CategoriesRecyclerAdapter.OnClickListener{
            viewModel.selectCategoryClick(account, it)
        })

        binding.gridOfCategories.apply {
            adapter = categoriesRecyclerAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.categories.collectLatest { newList ->
                categoriesRecyclerAdapter.submitList(newList)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is SelectCategoryViewModel.Event.SelectCategory -> {
                        if (getCurrentDestination() == this@SelectCategorySheetFragment.javaClass.name) {
                            findNavController().navigate(
                                SelectCategorySheetFragmentDirections
                                    .actionSelectCategorySheetFragmentToAddTransactionSheetFragment(
                                        event.account,
                                        event.category
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