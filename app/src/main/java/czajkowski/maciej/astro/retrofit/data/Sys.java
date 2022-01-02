package czajkowski.maciej.astro.retrofit.data;

import com.google.gson.annotations.SerializedName;

public class Sys {
    //    "sys": {
//        "type": 1,
//                "id": 5091,
//                "message": 0.0103,
//                "country": "GB",
//                "sunrise": 1485762037,
//                "sunset": 1485794875
//    },

    @SerializedName("type")
    int type;

    @SerializedName("id")
    int id;

    @SerializedName("message")
    double message;

    @SerializedName("country")
    String country;

    @SerializedName("sunrise")
    int sunrise;

    @SerializedName("sunset")
    int sunset;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

}
