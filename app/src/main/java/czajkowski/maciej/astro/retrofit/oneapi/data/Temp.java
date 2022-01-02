package czajkowski.maciej.astro.retrofit.oneapi.data;

import com.google.gson.annotations.SerializedName;

public class Temp {
//          "temp": {
//        "day": 28.94,
//                "min": 17.61,
//                "max": 30.95,
//                "night": 20.25,
//                "eve": 23.72,
//                "morn": 17.71
//    },
    @SerializedName("max")
    private double max;

    @SerializedName("min")
    private double min;

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}
