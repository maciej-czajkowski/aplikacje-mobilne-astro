package czajkowski.maciej.astro.viewmodels;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BundleViewModel extends ViewModel {
    private MutableLiveData<Bundle> bundleData;

    public void init()
    {
        this.bundleData = new MutableLiveData<>();
    }

    public void sendBundle(Bundle bundle)
    {
        this.bundleData.setValue(bundle);
    }

    public LiveData<Bundle> getBundle()
    {
        return this.bundleData;
    }
}
