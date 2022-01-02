package czajkowski.maciej.astro.retrofit.data;

import com.google.gson.annotations.SerializedName;

public class Coord {
//     "coord": {
//        "lon": -0.13,
//                "lat": 51.51
//    },

    @SerializedName("lon")
    double lon;

    @SerializedName("lat")
    double lat;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
