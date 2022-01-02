package czajkowski.maciej.astro.retrofit.oneapi.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import czajkowski.maciej.astro.retrofit.data.Weather;

public class Daily {

    @SerializedName("dt")
    private int date;

    @SerializedName("temp")
    private Temp temp;

    @SerializedName("weather")
    private List<Weather> weatherList;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }
}
