package czajkowski.maciej.astro.retrofit.data;

import com.google.gson.annotations.SerializedName;

public class Wind {
//"wind": {
//        "speed": 1.5,
//                "deg": 350
//    },

    @SerializedName("speed")
    double speed;

    @SerializedName("deg")
    double deg;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}
