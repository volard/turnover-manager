package com.liberty.turnovermanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.liberty.turnovermanagement.customers.list.CustomersFragment;
import com.liberty.turnovermanagement.databinding.ActivityMainBinding;
import com.liberty.turnovermanagement.orders.list.OrdersFragment;
import com.liberty.turnovermanagement.products.list.ProductsFragment;
import com.liberty.turnovermanagement.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove app title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        // Get shared settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Set theme
        boolean isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);

        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Set locale
        String languageCode = sharedPreferences.getString("language", "en");
        LocaleHelper.setLocale(this, languageCode);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_orders) {
                selectedFragment = new OrdersFragment();
            } else if (itemId == R.id.navigation_products) {
                selectedFragment = new ProductsFragment();
            } else if (itemId == R.id.navigation_customers) {
                selectedFragment = new CustomersFragment();
            } else if (itemId == R.id.navigation_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                navigateToFragment(selectedFragment);
            }

            return true;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            binding.navView.setSelectedItemId(R.id.navigation_orders);
        }

    }

    // Helper function to navigate to a fragment
    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment); // Replace with your fragment container ID
        transaction.commit();
    }
}