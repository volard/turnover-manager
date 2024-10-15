package com.liberty.turnovermanagement.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.liberty.turnovermanagement.R;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchTheme;
    private Button btnRegenerateData;
    private CheckBox checkboxOption;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        switchTheme = root.findViewById(R.id.switch_theme);
        btnRegenerateData = root.findViewById(R.id.btn_regenerate_data);
        checkboxOption = root.findViewById(R.id.checkbox_option);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        setupThemeSwitch();
        setupRegenerateDataButton();
        setupCheckboxOption();

        return root;
    }

    private void setupCheckboxOption() {
        // Load the saved state of the checkbox

        boolean isChecked = sharedPreferences.getBoolean("isArchivedVisible", false);
        checkboxOption.setChecked(isChecked);

        checkboxOption.setOnCheckedChangeListener((buttonView, _isChecked) -> {
            // Save the new state of the checkbox
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isArchivedVisible", _isChecked);
            editor.apply();
        });
    }

    private void setupThemeSwitch() {
        // Get current theme
        boolean isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);
        switchTheme.setChecked(isDarkTheme);

        switchTheme.setOnCheckedChangeListener((buttonView, _isChecked) -> {
            if (_isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isDarkTheme", true);
                editor.apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isDarkTheme", false);
                editor.apply();
            }
        });
    }

    private void setupRegenerateDataButton() {
        btnRegenerateData.setOnClickListener(v -> {
            regenerateTestData();
            Toast.makeText(getContext(), "Test data regenerated", Toast.LENGTH_SHORT).show();
        });
    }

    private void regenerateTestData() {
        // TODO: Implement the logic to regenerate test data for orders, products, and customers
        // This will depend on how you're storing and managing your data
    }
}

