package czajkowski.maciej.astro.viewmodels;

import java.time.LocalDateTime;

import czajkowski.maciej.astro.retrofit.data.OpenWeatherData;
import czajkowski.maciej.astro.retrofit.data.OpenWeatherForecastData;
import czajkowski.maciej.astro.retrofit.oneapi.data.OneApiResponse;

public class WeatherInfo {
    private OpenWeatherData openWeatherData;
    private OneApiResponse oneApiResponse;
    private LocalDateTime localDateTime;

    public WeatherInfo(OpenWeatherData openWeatherData, LocalDateTime localDateTime ) {
        this.openWeatherData = openWeatherData;
        this.localDateTime = localDateTime;
    }

    public WeatherInfo(OpenWeatherData openWeatherData, OneApiResponse oneApiResponse, LocalDateTime localDateTime ) {
        this.openWeatherData = openWeatherData;
        this.oneApiResponse = oneApiResponse;
        this.localDateTime = localDateTime;
    }

    public OpenWeatherData getOpenWeatherResponse() {
        return openWeatherData;
    }

    public void setOpenWeatherResponse(OpenWeatherData openWeatherData) {
        this.openWeatherData = openWeatherData;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public OpenWeatherData getOpenWeatherData() {
        return openWeatherData;
    }

    public void setOpenWeatherData(OpenWeatherData openWeatherData) {
        this.openWeatherData = openWeatherData;
    }


    public OneApiResponse getOneApiResponse() {
        return oneApiResponse;
    }

    public void setOneApiResponse(OneApiResponse oneApiResponse) {
        this.oneApiResponse = oneApiResponse;
    }
}
