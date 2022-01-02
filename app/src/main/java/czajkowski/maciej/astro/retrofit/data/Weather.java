package czajkowski.maciej.astro.retrofit.data;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("main")
    String main;

    @SerializedName("description")
    String description;

    @SerializedName("icon")
    String icon;


    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
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
}
