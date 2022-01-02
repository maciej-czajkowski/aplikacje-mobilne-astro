package czajkowski.maciej.astro.retrofit.wrappers;


import czajkowski.maciej.astro.retrofit.data.OpenWeatherData;
import czajkowski.maciej.astro.retrofit.data.OpenWeatherForecastData;
import czajkowski.maciej.astro.retrofit.oneapi.data.OneApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherInterfaceMetric {
    @GET("weather?APPID=d2747024af512c513aaa0aa2cf399f24&units=metric")
    Call<OpenWeatherData> getWeatherData(@Query("q") String name);

    @GET("forecast?APPID=d2747024af512c513aaa0aa2cf399f24&units=metric")
    Call<OpenWeatherForecastData> getWeatherForecastData(@Query("q") String name);

    @GET("onecall?APPID=d2747024af512c513aaa0aa2cf399f24&units=metric&exclude=current,minutely,hourly,alerts")
    Call<OneApiResponse> getOneApiCall(@Query("lat") String lat, @Query("lon") String lon);
}