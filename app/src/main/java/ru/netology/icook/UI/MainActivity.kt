package ru.netology.icook.UI

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.icook.R
import ru.netology.icook.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        val toolbar = findViewById<Toolbar>(R.id.toolbarUp)
        setSupportActionBar(toolbar)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.recipeDetailedFragment) {
                toolbar.visibility = View.GONE
                bottomNavigationView.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
                bottomNavigationView.visibility = View.VISIBLE
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.feedFragment,
                R.id.newRecipeFragment,
                R.id.favoritesRecipeFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

}