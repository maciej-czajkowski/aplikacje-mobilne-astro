package czajkowski.maciej.astro.storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import czajkowski.maciej.astro.retrofit.oneapi.data.OneApiResponse;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;

@Entity(tableName = "cities", indices = {@Index(value = {"city"}, unique = true)})
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "city")
    private String name;

    @ColumnInfo(name = "lat")
    private double lat;

    @ColumnInfo(name = "lon")
    private double lon;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "temp")
    private double temp;

    @ColumnInfo(name = "pressure")
    private int pressure;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "wind_speed")
    private double windSpeed;

    @ColumnInfo(name = "wind_deg")
    private double windDeg;

    @ColumnInfo(name = "humidity")
    private int humidity;

    @ColumnInfo(name = "forecast1_date")
    private String forecast1Date;

    @ColumnInfo(name = "forecast1_icon")
    private String forecast1Icon;

    @ColumnInfo(name = "forecast1_description")
    private String forecast1Description;

    @ColumnInfo(name = "forecast1_temp")
    private double forecast1Temp;


    @ColumnInfo(name = "forecast2_date")
    private String forecast2Date;

    @ColumnInfo(name = "forecast2_icon")
    private String forecast2Icon;

    @ColumnInfo(name = "forecast2_description")
    private String forecast2Description;

    @ColumnInfo(name = "forecast2_temp")
    private double forecast2Temp;


    @ColumnInfo(name = "forecast3_date")
    private String forecast3Date;

    @ColumnInfo(name = "forecast3_icon")
    private String forecast3Icon;

    @ColumnInfo(name = "forecast3_description")
    private String forecast3Description;

    @ColumnInfo(name = "forecast3_temp")
    private double forecast3Temp;


    @ColumnInfo(name = "forecast4_date")
    private String forecast4Date;

    @ColumnInfo(name = "forecast4_icon")
    private String forecast4Icon;

    @ColumnInfo(name = "forecast4_description")
    private String forecast4Description;

    @ColumnInfo(name = "forecast4_temp")
    private double forecast4Temp;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(double windDeg) {
        this.windDeg = windDeg;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getForecast1Date() {
        return forecast1Date;
    }

    public void setForecast1Date(String forecast1Date) {
        this.forecast1Date = forecast1Date;
    }

    public String getForecast1Icon() {
        return forecast1Icon;
    }

    public void setForecast1Icon(String forecast1Icon) {
        this.forecast1Icon = forecast1Icon;
    }

    public String getForecast1Description() {
        return forecast1Description;
    }

    public void setForecast1Description(String forecast1Description) {
        this.forecast1Description = forecast1Description;
    }

    public double getForecast1Temp() {
        return forecast1Temp;
    }

    public void setForecast1Temp(double forecast1Temp) {
        this.forecast1Temp = forecast1Temp;
    }

    public String getForecast2Date() {
        return forecast2Date;
    }

    public void setForecast2Date(String forecast2Date) {
        this.forecast2Date = forecast2Date;
    }

    public String getForecast2Icon() {
        return forecast2Icon;
    }

    public void setForecast2Icon(String forecast2Icon) {
        this.forecast2Icon = forecast2Icon;
    }

    public String getForecast2Description() {
        return forecast2Description;
    }

    public void setForecast2Description(String forecast2Description) {
        this.forecast2Description = forecast2Description;
    }

    public double getForecast2Temp() {
        return forecast2Temp;
    }

    public void setForecast2Temp(double forecast2Temp) {
        this.forecast2Temp = forecast2Temp;
    }

    public String getForecast3Date() {
        return forecast3Date;
    }

    public void setForecast3Date(String forecast3Date) {
        this.forecast3Date = forecast3Date;
    }

    public String getForecast3Icon() {
        return forecast3Icon;
    }

    public void setForecast3Icon(String forecast3Icon) {
        this.forecast3Icon = forecast3Icon;
    }

   public double getForecast3Temp() {
        return forecast3Temp;
    }

    public void setForecast3Temp(double forecast3Temp) {
        this.forecast3Temp = forecast3Temp;
    }

    public String getForecast4Date() {
        return forecast4Date;
    }

    public void setForecast4Date(String forecast4Date) {
        this.forecast4Date = forecast4Date;
    }

    public String getForecast4Icon() {
        return forecast4Icon;
    }

    public void setForecast4Icon(String forecast4Icon) {
        this.forecast4Icon = forecast4Icon;
    }

   public double getForecast4Temp() {
        return forecast4Temp;
    }

    public void setForecast4Temp(double forecast4Temp) {
        this.forecast4Temp = forecast4Temp;
    }

    public String getForecast3Description() {
        return forecast3Description;
    }

    public void setForecast3Description(String forecast3Description) {
        this.forecast3Description = forecast3Description;
    }

    public String getForecast4Description() {
        return forecast4Description;
    }

    public void setForecast4Description(String forecast4Description) {
        this.forecast4Description = forecast4Description;
    }

    @Override
    public String toString() {
        return "Record{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", time='" + time + '\'' +
                '}';
    }
}
