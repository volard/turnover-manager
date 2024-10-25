package com.liberty.turnovermanagement.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.liberty.turnovermanagement.R;
import com.liberty.turnovermanagement.databinding.FragmentProductsBinding;
import com.liberty.turnovermanagement.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupThemeSwitch();
        setupRegenerateDataButton();
        setupCheckboxOption();
        setupClearButton();
    }

    private void setupClearButton() {
        binding.btnClearData.setOnClickListener(v -> {
            viewModel.clearData();
            Toast.makeText(getContext(), "All data cleared", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupCheckboxOption() {
        viewModel.getIsArchivedVisible().observe(getViewLifecycleOwner(), isChecked -> {
            binding.checkboxOption.setChecked(isChecked);
        });

        binding.checkboxOption.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setArchivedVisible(isChecked);
        });
    }

    private void setupThemeSwitch() {
        viewModel.getIsDarkTheme().observe(getViewLifecycleOwner(), isDarkTheme -> {
            binding.switchTheme.setChecked(isDarkTheme);
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        binding.switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setDarkTheme(isChecked);
        });
    }

    private void setupRegenerateDataButton() {
        binding.btnRegenerateData.setOnClickListener(v -> {
            viewModel.regenerateTestData();
            Toast.makeText(getContext(), "Test data regenerated", Toast.LENGTH_SHORT).show();
        });
    }
}

