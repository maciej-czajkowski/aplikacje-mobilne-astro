package czajkowski.maciej.astro.viewmodels;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InternetViewModel extends ViewModel {
    private MutableLiveData<Boolean> available;

    public void init()
    {
        this.available = new MutableLiveData<>();
    }

    public void alertFragments(Boolean available)
    {
        this.available.setValue(available);
    }

    public LiveData<Boolean> getAvaiblitity()
    {
        return this.available;
    }
}

