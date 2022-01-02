package czajkowski.maciej.astro.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherInfoViewModel extends ViewModel {
    private MutableLiveData<WeatherInfo> data;

    public void init()
    {
        this.data = new MutableLiveData<>();
    }

    public void sendWeatherInfo(WeatherInfo weatherInfo)
    {
        this.data.setValue(weatherInfo);
    }

    public LiveData<WeatherInfo> getData()
    {
        return this.data;
    }
}
