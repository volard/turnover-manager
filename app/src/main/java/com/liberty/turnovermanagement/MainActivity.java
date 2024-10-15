package com.liberty.turnovermanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.liberty.turnovermanagement.databinding.ActivityMainBinding;

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

        NavController navController = Navigation.findNavController(
                this,
                R.id.nav_host_fragment_activity_main
        );

        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}