package com.example.financemanager.UI.accounts.actions

import android.app.AlertDialog
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
import com.example.financemanager.databinding.FragmentAccountActionsSheetBinding
import com.example.financemanager.toAmountFormat
import com.example.financemanager.utils.mapOfColors
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountActionsSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAccountActionsSheetBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: AccountActionsViewModel by viewModels()

    private val args by navArgs<AccountActionsSheetFragmentArgs>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountActionsSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = args.selectedAccount

        binding.accountName.text = account.name
        binding.accountAmount.text = account.amount.toAmountFormat(withMinus = false)
        binding.accountCurrency.text = viewModel.getPreferences().getString(
            "currency",
            requireContext().resources.getStringArray(R.array.currency_values)[0]
        )

        binding.actionsContainer.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                mapOfColors[account.color] ?: R.color.orange_red
            )
        )

        binding.editButton.setOnClickListener {
            viewModel.editButtonClick(account)
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteButtonClick(account)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is AccountActionsViewModel.Event.NavigateToEditAccountScreen -> {
                       if (getCurrentDestination() == this@AccountActionsSheetFragment.javaClass.name) {
                           findNavController().navigate(
                               AccountActionsSheetFragmentDirections
                                   .actionAccountActionsSheetFragmentToAccountEditFragment(account)
                           )
                       }
                    }
                    is AccountActionsViewModel.Event.ShowDeleteAccountDialog -> {
                        val alert = AlertDialog.Builder(requireContext())
                            .setIcon(R.drawable.ic_warning)
                            .setTitle(getString(R.string.delete_alert_title))
                            .setMessage(getString(R.string.delete_alert_subtitle))
                            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                                viewModel.deleteConfirmButtonClick()
                                this@AccountActionsSheetFragment.dismiss()
                            }
                            .setNegativeButton(getString(R.string.cancel)) {_, _ ->
                                this@AccountActionsSheetFragment.dismiss()
                            }
                            .setCancelable(false)
                            .create()

                        alert.show()
                    }
                    is  AccountActionsViewModel.Event.DeleteAccount -> {
                        viewModel.deleteAccount(account)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

