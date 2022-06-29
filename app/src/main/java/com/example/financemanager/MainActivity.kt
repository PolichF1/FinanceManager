package com.example.financemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.DateUtils.getCurrentLocalDate
import com.example.financemanager.UI.accounts.AccountsFragmentDirections
import com.example.financemanager.UI.chart.ChartFragmentDirections
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        var currentDestination: Int? = null

        binding.buttonSettings.setOnClickListener {
            viewModel.settingsButtonClick()
        }

        binding.toolbarInfoBox.setOnClickListener {
            viewModel.selectAccountButtonClick()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is MainActivityViewModel.Event.OpenSettingsScreen -> {
                        when (currentDestination) {
                            R.id.accounts_fragment -> navController.navigate(
                                AccountsFragmentDirections.actionAccountsFragmentToSettingsActivity()
                            )
                            R.id.chart_fragment -> navController.navigate(
                                ChartFragmentDirections.actionChartFragmentToSettingsActivity()
                            )

                        }
                    }
                    is MainActivityViewModel.Event.OpenSelectAccountDialog -> {
                        navController.navigate(R.id.action_dialog_fragment_account_filter)
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currentAccount.collectLatest { account ->
                binding.bankIcon.visibility = if (account != null) View.VISIBLE else View.GONE
                binding.toolbarTitle.text = account?.name ?: getString(R.string.all_accounts)
            }
        }

        val pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        lifecycleScope.launchWhenStarted {
            viewModel.currentDateRange.collectLatest {
                binding.toolbarSubtitle.text =
                    if (it.first == null && it.second == null)
                        getString(R.string.all_time)
                    else if (it.second == null)
                        "${it.first?.format(pattern)} - ${getCurrentLocalDate().format(pattern)}"
                    else
                        it.first?.format(pattern)
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.accounts_fragment, R.id.transactions_fragment, R.id.chart_fragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val isFragmentWithoutSettings = when (currentDestination) {
                R.id.account_add_fragment -> true
                R.id.account_edit_fragment -> true
                else -> false
            }

            binding.bottomNavigation.visibility =
                if (isFragmentWithoutSettings) View.GONE else View.VISIBLE
            binding.buttonSettings.visibility =
                if (isFragmentWithoutSettings) View.GONE else View.VISIBLE
            binding.toolbarInfoBox.visibility =
                if (isFragmentWithoutSettings) View.GONE else View.VISIBLE
            supportActionBar?.setDisplayShowTitleEnabled(isFragmentWithoutSettings)

            val isFragmentWithoutAccountsFilter = when (currentDestination) {
                R.id.accounts_fragment -> true
                R.id.account_actions_sheet_fragment -> true
                else -> false
            }

            binding.moreButton.visibility = if (isFragmentWithoutAccountsFilter) View.INVISIBLE else View.VISIBLE
            binding.toolbarInfoBox.isEnabled = !isFragmentWithoutAccountsFilter
        }
    }

    override fun onStart() {
        val nightMode = viewModel.getPreferences().getBoolean("night_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (nightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        super.onStart()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}