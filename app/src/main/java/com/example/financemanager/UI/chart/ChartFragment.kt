package com.example.financemanager.UI.chart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.R
import com.example.financemanager.databinding.FragmentChartBinding

class ChartFragment : Fragment(R.layout.fragment_chart) {

//    private var _binding: FragmentCategoriesBinding? = null
//    private val binding get() = _binding!!

    private val binding: FragmentChartBinding by viewBinding()

//    private lateinit var categoriesViewModel: CategoriesViewModel

    private val categoriesViewModel: ChartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}