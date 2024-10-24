package com.liberty.turnovermanagement.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.R;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchTheme;
    private Button btnRegenerateData;
    private CheckBox checkboxOption;
    private SettingsViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        switchTheme = root.findViewById(R.id.switch_theme);
        btnRegenerateData = root.findViewById(R.id.btn_regenerate_data);
        checkboxOption = root.findViewById(R.id.checkbox_option);

        setupThemeSwitch();
        setupRegenerateDataButton();
        setupCheckboxOption();

        return root;
    }


    private void setupCheckboxOption() {
        viewModel.getIsArchivedVisible().observe(getViewLifecycleOwner(), isChecked -> {
            checkboxOption.setChecked(isChecked);
        });

        checkboxOption.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setArchivedVisible(isChecked);
        });
    }

    private void setupThemeSwitch() {
        viewModel.getIsDarkTheme().observe(getViewLifecycleOwner(), isDarkTheme -> {
            switchTheme.setChecked(isDarkTheme);
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setDarkTheme(isChecked);
        });
    }

    private void setupRegenerateDataButton() {
        btnRegenerateData.setOnClickListener(v -> {
            viewModel.regenerateTestData();
            Toast.makeText(getContext(), "Test data regenerated", Toast.LENGTH_SHORT).show();
        });
    }
}

