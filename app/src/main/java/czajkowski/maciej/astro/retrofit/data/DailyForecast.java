package czajkowski.maciej.astro.retrofit.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyForecast {
    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weatherList;

    @SerializedName("weather")
    private int date;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
