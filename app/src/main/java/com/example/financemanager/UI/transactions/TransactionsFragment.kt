package com.example.financemanager.UI.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.R
import com.example.financemanager.databinding.FragmentTransactionsBinding


class TransactionsFragment : Fragment(R.layout.fragment_transactions) {


//    private var _binding: FragmentTransactionsBinding? = null
//    private val binding get() = _binding!!

    private val binding: FragmentTransactionsBinding by viewBinding()

//    private lateinit var transactionsViewModel: TransactionsViewModel

    private val transactionsViewModel: TransactionsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }

}