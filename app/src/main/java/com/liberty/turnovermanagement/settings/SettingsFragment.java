package com.liberty.turnovermanagement.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.liberty.turnovermanagement.R;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchTheme;
    private Button btnRegenerateData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        switchTheme = root.findViewById(R.id.switch_theme);
        btnRegenerateData = root.findViewById(R.id.btn_regenerate_data);

        setupThemeSwitch();
        setupRegenerateDataButton();

        return root;
    }

    private void setupThemeSwitch() {
        // Get current theme
        boolean isDarkTheme = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        switchTheme.setChecked(isDarkTheme);

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

