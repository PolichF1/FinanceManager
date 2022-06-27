package com.example.financemanager.UI.transactions.add.date_pick

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.*
import com.example.financemanager.databinding.FragmentSelectDateDialogBinding
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SelectDateDialogFragment : DialogFragment(R.layout.fragment_select_date_dialog) {

    private val binding: FragmentSelectDateDialogBinding by viewBinding()

    private val viewModel: SelectDateDialogViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectDate.setOnClickListener {
            viewModel.selectDateClick()
        }
        binding.today.setOnClickListener {
            viewModel.selectTodayClick()
        }
        binding.week.setOnClickListener {
            viewModel.selectWeekClick()
        }
        binding.month.setOnClickListener {
            viewModel.selectMonthClick()
        }
        binding.year.setOnClickListener {
            viewModel.selectYearClick()
        }
        binding.allTime.setOnClickListener {
            viewModel.selectAllTimeClick()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is SelectDateDialogViewModel.Event.SelectDate -> {
                        val datePicker = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select Date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build()

                        datePicker.addOnPositiveButtonClickListener { milliseconds ->
                            val date = milliseconds.toLocalDate()
                            activityViewModel.setCurrentDateRange(date, date)
                            dismiss()
                        }
                        datePicker.show(childFragmentManager, DATE_PICKER_TAG)
                    }
                    is SelectDateDialogViewModel.Event.SelectToday -> {
                        val date = getCurrentLocalDate()
                        activityViewModel.setCurrentDateRange(date,date)
                        dismiss()
                    }
                    is SelectDateDialogViewModel.Event.SelectWeek -> {
                        val from = (getCurrentLocalDate().toMilliseconds() - (7 * DAY_IN_MS))
                            .toLocalDate()

                        activityViewModel.setCurrentDateRange(from, getCurrentLocalDate())
                        dismiss()
                    }
                    is SelectDateDialogViewModel.Event.SelectMonth -> {
                        val from = (getCurrentLocalDate().toMilliseconds() - (30 * DAY_IN_MS))
                            .toLocalDate()

                        activityViewModel.setCurrentDateRange(from, getCurrentLocalDate())
                        dismiss()
                    }
                    is SelectDateDialogViewModel.Event.SelectYear -> {
                        val from =
                            (getCurrentLocalDate().toMilliseconds() - (365 * DAY_IN_MS)).
                            toLocalDate()

                        activityViewModel.setCurrentDateRange(from, getCurrentLocalDate())
                        dismiss()
                    }
                    is SelectDateDialogViewModel.Event.SelectAllTime -> {
                        activityViewModel.setCurrentDateRange(null, null)
                        dismiss()
                    }
                }
            }
        }
    }







    companion object {
        private const val DATE_PICKER_TAG = "date_picker_tag"
    }
}