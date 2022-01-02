package czajkowski.maciej.astro.retrofit.oneapi.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OneApiResponse {

    @SerializedName("daily")
    private List<Daily> dailyList;

    public List<Daily> getDailyList() {
        return dailyList;
    }

    public void setDailyList(List<Daily> dailyList) {
        this.dailyList = dailyList;
    }
}
