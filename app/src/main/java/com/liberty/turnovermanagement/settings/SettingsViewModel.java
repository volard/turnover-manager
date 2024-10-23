package com.liberty.turnovermanagement.settings;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;

public class SettingsViewModel extends AndroidViewModel {
    private final AppDatabase appDatabase;
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<Boolean> isDarkTheme = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isArchivedVisible = new MutableLiveData<>();

    public SettingsViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        loadSettings();
    }

    private void loadSettings() {
        isDarkTheme.setValue(sharedPreferences.getBoolean("isDarkTheme", false));
        isArchivedVisible.setValue(sharedPreferences.getBoolean("isArchivedVisible", false));
    }

    public LiveData<Boolean> getIsDarkTheme() {
        return isDarkTheme;
    }

    public LiveData<Boolean> getIsArchivedVisible() {
        return isArchivedVisible;
    }

    public void setDarkTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isDarkTheme", darkTheme);
        editor.apply();
        isDarkTheme.setValue(darkTheme);
    }

    public void setArchivedVisible(boolean archivedVisible) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isArchivedVisible", archivedVisible);
        editor.apply();
        isArchivedVisible.setValue(archivedVisible);
    }

    public void regenerateTestData() {
        appDatabase.generateTestData();
    }
}

