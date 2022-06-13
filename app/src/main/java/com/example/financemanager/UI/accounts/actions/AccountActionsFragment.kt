package com.example.financemanager.UI.accounts.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.financemanager.databinding.FragmentAccountActionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AccountActionsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAccountActionsBinding? = null
    private val binding = requireNotNull(_binding)

    private val viewModel: AccountActionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}