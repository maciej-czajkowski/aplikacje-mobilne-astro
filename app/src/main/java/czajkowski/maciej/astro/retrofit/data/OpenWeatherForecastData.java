package czajkowski.maciej.astro.retrofit.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenWeatherForecastData {

    @SerializedName("list")
    private List<DailyForecast> dailyForecastList;

    public List<DailyForecast> getDailyForecastList() {
        return dailyForecastList;
    }
}
