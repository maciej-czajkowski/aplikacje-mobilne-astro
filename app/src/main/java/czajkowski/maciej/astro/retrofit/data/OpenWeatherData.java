package czajkowski.maciej.astro.retrofit.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenWeatherData {

    @SerializedName("sys")
    private Sys sys;

    @SerializedName("coord")
    private Coord coord;

    @SerializedName("main")
    private Main main;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("name")
    private String name;

    @SerializedName("weather")
    private List<Weather> weatherList;

    public Sys getSys() { return sys; }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeatherList() { return weatherList; }

    public void setWeatherList(List<Weather> weatherList) { this.weatherList = weatherList; }

    public String getName() {
        return name;
    }

    public Wind getWind() {
        return wind;
    }
}
