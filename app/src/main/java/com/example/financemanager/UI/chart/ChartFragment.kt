package com.example.financemanager.UI.chart

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import com.example.financemanager.utils.PRIMARY_COLOR
import com.example.financemanager.utils.mapOfDrawables
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChartFragment : Fragment(R.layout.fragment_chart) {

    private val binding: FragmentChartBinding by viewBinding()

    private val viewModel: ChartViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launchWhenStarted {
            viewModel.categoryViews.collectLatest {
                if (it.isNotEmpty())
                    updateChartData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            activityViewModel.currentDateRange.collectLatest {
                viewModel.setDateRange(it.first, it.second)
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

    private fun getThemeColor(color: Int) : Int {
        val value = TypedValue()
        requireContext().theme.resolveAttribute(color, value, true)
        return  value.data
    }

    private fun updateChartData(categoryViews: List<CategoryView>) {
        val currency = viewModel.getPreferences().getString(
            "currency",
            requireContext().resources.getStringArray(R.array.currency_values)[0]
        )

        updateCategories(categoryViews, currency)

        var amount = 0.0
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        categoryViews.forEach { category ->
            if (category.amount != 0.0) {
                entries.add(PieEntry(category.amount.toFloat()))
                colors.add(Color.parseColor(category.iconColor))
                amount += category.amount
            }
        }

        if (amount == 0.0) {
            entries.add(PieEntry(1f))
            colors.add(Color.parseColor(PRIMARY_COLOR))
            binding.chart.alpha = 0.3f
        } else binding.chart.alpha = 1f

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.setDrawValues(false)
        dataSet.sliceSpace = 2f

        val amountString = amount.toAmountFormat(withMinus = false) + ' ' + currency
        binding.chart.apply {
            isDrawHoleEnabled = true
            holeRadius = 86f
            setHoleColor(getThemeColor(com.google.android.material.R.attr.colorOnPrimary))
            setCenterTextColor(getThemeColor(com.google.android.material.R.attr.colorOnSecondary))
            centerText = "Expenses\n$amountString"
            setCenterTextSize(20f)
            description.isEnabled = false
            legend.isEnabled = false

            data = PieData(dataSet)
        }

        binding.chart.invalidate()
        binding.chart.animateY(1000, Easing.EaseInOutQuad)
    }

    private fun updateCategories(categoryViews: List<CategoryView>, currency: String?) {
        binding.category1.setCategoryAttributes(categoryViews[0], currency)
        binding.category2.setCategoryAttributes(categoryViews[1], currency)
        binding.category3.setCategoryAttributes(categoryViews[2], currency)
        binding.category4.setCategoryAttributes(categoryViews[3], currency)
        binding.category5.setCategoryAttributes(categoryViews[4], currency)
        binding.category6.setCategoryAttributes(categoryViews[5], currency)
        binding.category7.setCategoryAttributes(categoryViews[6], currency)
        binding.category8.setCategoryAttributes(categoryViews[7], currency)
        binding.category9.setCategoryAttributes(categoryViews[8], currency)
        binding.category10.setCategoryAttributes(categoryViews[9], currency)
        binding.category11.setCategoryAttributes(categoryViews[10], currency)
        binding.category12.setCategoryAttributes(categoryViews[11], currency)
    }

    private fun CategoryItemBinding.setCategoryAttributes(
        categoryView: CategoryView,
        currency: String?
    ) {
        this.name.text = categoryView.name
        this.icon.setImageResource(mapOfDrawables[categoryView.icon] ?: 0)
        this.amount.text = categoryView.amount.toAmountFormat(withMinus = false)
        this.currency.text = currency

        val color = Color.parseColor(categoryView.iconColor)

        this.amount.setTextColor(color)
        this.currency.setTextColor(color)
        DrawableCompat.setTint(this.iconBackground.drawable, color)

        this.item.alpha = if (categoryView.amount == 0.0) 0.3f else 1f
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