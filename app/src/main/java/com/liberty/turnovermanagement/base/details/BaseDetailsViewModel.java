package com.liberty.turnovermanagement.base.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liberty.turnovermanagement.AppDatabase;

import java.util.List;

public abstract class BaseDetailsViewModel<T, H> extends AndroidViewModel {
    protected final MutableLiveData<T> selectedItem = new MutableLiveData<>();
    protected final AppDatabase db;

    public BaseDetailsViewModel(Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
    }

    public LiveData<T> getSelectedItem() {
        return selectedItem;
    }

    public abstract void loadItem(long itemId);

    public abstract void updateItem(T item);

    public abstract void softDelete(T item);

    public abstract void addNewItem(T item);
}
