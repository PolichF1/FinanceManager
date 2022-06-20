package com.example.financemanager.UI.accounts.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.R
import com.example.financemanager.data.models.Account
import com.example.financemanager.databinding.FragmentAccountAddBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class AccountAddFragment : Fragment(R.layout.fragment_account_add) {

    private val binding: FragmentAccountAddBinding by viewBinding()

    private val viewModel: AccountAddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launchWhenCreated {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is AccountAddViewModel.Event.AddAccount -> {
                        val account = Account(
                            name = binding.nameTextField.editText?.text.toString(),
                            amount = binding.amountTextField.editText?.text.toString().toDoubleOrNull() ?: 0.0,
                            currency = binding.currencyTextField.editText?.text.toString(),
                            color = 2555555
                        )

                        viewModel.addAccount(account)
                        findNavController().navigate(AccountAddFragmentDirections.actionAccountAddFragmentToAccountsFragment())
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.apply) {
            viewModel.applyButtonClick()
        }

        return super.onOptionsItemSelected(item)
    }
}