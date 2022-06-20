package com.example.financemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.financemanager.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(R.id.container)

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.buttonSettings.setOnClickListener {
            Toast.makeText(applicationContext, "settings", Toast.LENGTH_SHORT).show()
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.accounts_fragment,
                R.id.transactions_fragment,
                R.id.chart_fragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_, destination, _ ->

            val isAddOrEditFragment =
                destination.id == R.id.account_add_fragment || destination.id == R.id.account_edit_fragment

            binding.buttonSettings.visibility = if (isAddOrEditFragment) View.GONE else View.VISIBLE
            binding.toolbarInfoBox.visibility = if (isAddOrEditFragment) View.GONE else View.VISIBLE

            supportActionBar?.setDisplayShowTitleEnabled(isAddOrEditFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}