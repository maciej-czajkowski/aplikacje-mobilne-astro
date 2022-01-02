package czajkowski.maciej.astro.storage;

import czajkowski.maciej.astro.retrofit.oneapi.data.OneApiResponse;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;

public class Record {
    private String name;
    private WeatherInfo weatherInfo;
    private OneApiResponse oneApiResponse;

    public Record(String name, WeatherInfo weatherInfo, OneApiResponse oneApiResponse) {
        this.name = name;
        this.weatherInfo = weatherInfo;
        this.oneApiResponse = oneApiResponse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public OneApiResponse getOneApiResponse() {
        return oneApiResponse;
    }

    public void setOneApiResponse(OneApiResponse oneApiResponse) {
        this.oneApiResponse = oneApiResponse;
    }
}
