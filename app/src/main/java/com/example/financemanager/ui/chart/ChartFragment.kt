package com.example.financemanager.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.MainActivityViewModel
import com.example.financemanager.R
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.databinding.CategoryItemBinding
import com.example.financemanager.databinding.FragmentChartBinding
import com.example.financemanager.utils.Utils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import com.example.financemanager.utils.Utils.setIcon
import com.example.financemanager.utils.Utils.setTint

@AndroidEntryPoint
class ChartFragment : Fragment(R.layout.fragment_chart) {

    private val binding: FragmentChartBinding by viewBinding()

    private val viewModel: ChartViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setupCollectors()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.date_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.select_date){
                    viewModel.selectDateClick()
                    true
                } else false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.categoryViews.collectLatest {
                updateChartData(it)
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
                    is ChartViewModel.Event.SelectDate -> {
                        if (getCurrentDestination() == this@ChartFragment.javaClass.name) {
                            findNavController().navigate(
                                ChartFragmentDirections.actionChartFragmentToSelectDateDialogFragment()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateChartData(categoryViews: List<CategoryView>) {

        if (categoryViews.isNotEmpty()) {
            val currency = activityViewModel.getCurrency()

            updateCategories(categoryViews, currency)

            var amount = 0.0
            val entries = ArrayList<PieEntry>()
            val entryColors = ArrayList<Int>()

            categoryViews.forEach { category ->
                if (category.amount != 0.0) {
                    entries.add(PieEntry(category.amount.toFloat()))
                    entryColors.add(Color.parseColor(category.iconColor))
                    amount += category.amount
                }
            }

            if (amount == 0.0) {
                entries.add(PieEntry(1f))
                entryColors.add(Color.parseColor(Utils.MAIN_COLOR))
                binding.chart.alpha = 0.3f
            } else binding.chart.alpha = 1f

            val dataSet = PieDataSet(entries, "").apply {
                colors = entryColors
                setDrawValues(false)
                sliceSpace = 2f
            }

            val amountString = amount.toAmountFormat(withMinus = false) + ' ' + currency
            binding.chart.apply {
                isDrawHoleEnabled = true
                holeRadius = 86f
                setHoleColor(Color.TRANSPARENT)
                setCenterTextColor(getThemeColor(com.google.android.material.R.attr.colorOnSecondary))
                centerText = "Expense\n$amountString"
                setCenterTextSize(20f)
                description.isEnabled = false
                legend.isEnabled = false
                data = PieData(dataSet)
            }

            binding.chart.invalidate()
            binding.chart.animateY(1000, Easing.EaseInOutQuad)
        }

    }

    private fun updateCategories(categoryViews: List<CategoryView>, currency: String?) {
        with(binding) {
            category1.setCategoryAttributes(categoryViews[0], currency)
            category2.setCategoryAttributes(categoryViews[1], currency)
            category3.setCategoryAttributes(categoryViews[2], currency)
            category4.setCategoryAttributes(categoryViews[3], currency)
            category5.setCategoryAttributes(categoryViews[4], currency)
            category6.setCategoryAttributes(categoryViews[5], currency)
            category7.setCategoryAttributes(categoryViews[6], currency)
            category8.setCategoryAttributes(categoryViews[7], currency)
            category9.setCategoryAttributes(categoryViews[8], currency)
            category10.setCategoryAttributes(categoryViews[9], currency)
            category11.setCategoryAttributes(categoryViews[10], currency)
            category12.setCategoryAttributes(categoryViews[11], currency)
        }
    }

    private fun CategoryItemBinding.setCategoryAttributes(
        category: CategoryView,
        currency: String?
    ) {
        this.name.text = category.name
        this.icon.setIcon(category.icon)
        this.iconBackground.setTint(category.iconColor)
        this.amount.text = category.amount.toAmountFormat(withMinus = false)
        this.currency.text = currency

        val color = Color.parseColor(category.iconColor)

        this.amount.setTextColor(color)
        this.currency.setTextColor(color)
        DrawableCompat.setTint(this.iconBackground.drawable, color)

        this.item.alpha = if (category.amount == 0.0) 0.3f else 1f
    }

    private var currentCurrency: String = ""

    override fun onStart() {
        super.onStart()
        if (currentCurrency != activityViewModel.getCurrency()) {
            updateChartData(viewModel.categoryViews.value)
        }
    }

    override fun onStop() {
        super.onStop()
        currentCurrency = activityViewModel.getCurrency()
    }

    private fun getThemeColor(color: Int): Int {
        val value = TypedValue()
        requireContext().theme.resolveAttribute(color, value, true)
        return value.data
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className

}