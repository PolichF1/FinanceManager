package com.example.financemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.ui.accounts.AccountsFragmentDirections
import com.example.financemanager.ui.chart.ChartFragmentDirections
import com.example.financemanager.ui.converter.CurrencyConverterFragmentDirections
import com.example.financemanager.ui.transactions.TransactionsFragmentDirections
import com.example.financemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(R.id.container)

    private val viewModel: MainActivityViewModel by viewModels()

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
    }

    private var currentDestination: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupEventCollector()
        setupSelectedValuesCollector()
        setupBottomNavigation()
        setupDestinationChangedListener()

        with(binding) {
            buttonSettings.setOnClickListener { viewModel.settingsButtonClick() }
            toolbarInfoBox.setOnClickListener { viewModel.selectAccountButtonClick() }
        }
    }

    private fun setupEventCollector() {
        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is MainActivityViewModel.Event.OpenTheSettingsScreen -> {
                        when (currentDestination) {
                            R.id.currency_converter_fragment -> {
                                CurrencyConverterFragmentDirections.actionCurrencyConverterFragmentToSettingsActivity()
                            }
                            R.id.accounts_fragment -> navController.navigate(
                                AccountsFragmentDirections.actionAccountsFragmentToSettingsActivity()
                            )
                            R.id.transactions_fragment -> navController.navigate(
                                TransactionsFragmentDirections.actionTransactionsFragmentToSettingsActivity()
                            )
                            R.id.chart_fragment -> navController.navigate(
                                ChartFragmentDirections.actionChartFragmentToSettingsActivity()
                            )
                        }
                    }
                    is MainActivityViewModel.Event.OpenTheSelectAccountDialog -> {
                        navController.navigate(R.id.action_dialog_fragment_account_filter)
                    }
                }
            }
        }
    }

    private fun setupSelectedValuesCollector() {
        lifecycleScope.launchWhenStarted {
            viewModel.selectedAccount.collectLatest {
                with(binding) {
                    bankIcon.visibility = if (it != null) View.VISIBLE else View.INVISIBLE
                    toolbarTitle.text = it?.name ?: getString(R.string.all_accounts)
                }
            }
        }

        val pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        lifecycleScope.launchWhenStarted {
            viewModel.selectedDateRange.collectLatest {
                binding.toolbarSubtitle.text =
                    if (it.first == null && it.second == null)
                        getString(R.string.all_time)
                    else if (it.second == null)
                        "${it.first?.format(pattern)} - ${getCurrentLocalDate().format(pattern)}"
                    else
                        it.first?.format(pattern)
            }
        }
    }

    private fun setupBottomNavigation() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.currency_converter_fragment,
                R.id.accounts_fragment,
                R.id.transactions_fragment,
                R.id.chart_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupDestinationChangedListener() {
        fun changeAppAndBottomBarrState(visibility: Int) {
            with(binding) {
                bottomNavigation.visibility = visibility
                buttonSettings.visibility = visibility
                toolbarInfoBox.visibility = visibility
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestination = destination.id
        }

        val isFragmentWithoutBars = when (currentDestination) {
            R.id.account_add_fragment -> true
            R.id.account_edit_fragment -> true
            else -> false
        }

        changeAppAndBottomBarrState(if (isFragmentWithoutBars) View.GONE else View.VISIBLE)
        supportActionBar?.setDisplayShowTitleEnabled(isFragmentWithoutBars)

        val isFragmentWithFilter = when (currentDestination) {
            R.id.currency_converter_fragment -> false
            R.id.accounts_fragment -> false
            R.id.account_actions_sheet_fragment -> false
            else -> true
        }

        with(binding) {
            moreButton.visibility = if (!isFragmentWithFilter) View.VISIBLE else View.GONE
            toolbarInfoBox.isEnabled = isFragmentWithFilter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}