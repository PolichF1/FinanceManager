package com.example.financemanager.UI.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

//    private var _binding: FragmentCategoriesBinding? = null
//    private val binding get() = _binding!!

    private val binding: FragmentCategoriesBinding by viewBinding()

//    private lateinit var categoriesViewModel: CategoriesViewModel

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textCategories
        categoriesViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
    }

}