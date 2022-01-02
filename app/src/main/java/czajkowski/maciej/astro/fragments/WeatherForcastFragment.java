package czajkowski.maciej.astro.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import czajkowski.maciej.astro.R;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;
import czajkowski.maciej.astro.viewmodels.WeatherInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherForcastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherForcastFragment extends Fragment {

    private static final String WEATHER_ICON_URL_PREFIX = "https://openweathermap.org/img/w/";
    private static final String WEATHER_ICON_FILE_EXTENSION = ".png";

    private static final DecimalFormat df = new DecimalFormat("###.##");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");

    public WeatherForcastFragment() {
        // Required empty public constructor
    }

    public static WeatherForcastFragment newInstance() {
        WeatherForcastFragment fragment = new WeatherForcastFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_forcast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        WeatherInfoViewModel weatherInfoViewModel = new ViewModelProvider(requireActivity()).get(WeatherInfoViewModel.class);
        weatherInfoViewModel.getData().observe(getViewLifecycleOwner(), new Observer<WeatherInfo>() {
            @Override
            public void onChanged(@Nullable WeatherInfo weatherInfo) {
                if (weatherInfo != null) {
                    Date date = new Date((long) weatherInfo.getOneApiResponse().getDailyList().get(1).getDate() * 1000);
                    ((TextView) v.findViewById(R.id.forecastDate1)).setText(dateFormat.format(date));

                    date = new Date((long) weatherInfo.getOneApiResponse().getDailyList().get(2).getDate() * 1000);
                    ((TextView) v.findViewById(R.id.forecastDate2)).setText(dateFormat.format(date));

                    date = new Date((long) weatherInfo.getOneApiResponse().getDailyList().get(3).getDate() * 1000);
                    ((TextView) v.findViewById(R.id.forecastDate3)).setText(dateFormat.format(date));

                    date = new Date((long) weatherInfo.getOneApiResponse().getDailyList().get(4).getDate() * 1000);
                    ((TextView) v.findViewById(R.id.forecastDate4)).setText(dateFormat.format(date));

                    ((TextView) v.findViewById(R.id.forecastTemp1))
                            .setText(df.format(weatherInfo.getOneApiResponse().getDailyList().get(1).getTemp().getMax() + weatherInfo.getOneApiResponse().getDailyList().get(1).getTemp().getMin() / 2));

                    ((TextView) v.findViewById(R.id.forecastTemp2))
                            .setText(df.format(weatherInfo.getOneApiResponse().getDailyList().get(2).getTemp().getMax() + weatherInfo.getOneApiResponse().getDailyList().get(2).getTemp().getMin() / 2));

                    ((TextView) v.findViewById(R.id.forecastTemp3))
                            .setText(df.format(weatherInfo.getOneApiResponse().getDailyList().get(3).getTemp().getMax() + weatherInfo.getOneApiResponse().getDailyList().get(3).getTemp().getMin() / 2));

                    ((TextView) v.findViewById(R.id.forecastTemp4))
                            .setText(df.format(weatherInfo.getOneApiResponse().getDailyList().get(4).getTemp().getMax() + weatherInfo.getOneApiResponse().getDailyList().get(4).getTemp().getMin() / 2));

                    ((TextView) v.findViewById(R.id.forecastDesc1))
                            .setText(weatherInfo.getOneApiResponse().getDailyList().get(1).getWeatherList().get(0).getDescription());

                    ((TextView) v.findViewById(R.id.forecastDesc2))
                            .setText(weatherInfo.getOneApiResponse().getDailyList().get(2).getWeatherList().get(0).getDescription());

                    ((TextView) v.findViewById(R.id.forecastDesc3))
                            .setText(weatherInfo.getOneApiResponse().getDailyList().get(3).getWeatherList().get(0).getDescription());

                    ((TextView) v.findViewById(R.id.forecastDesc4))
                            .setText(weatherInfo.getOneApiResponse().getDailyList().get(4).getWeatherList().get(0).getDescription());

                    String iconUrl = WEATHER_ICON_URL_PREFIX +
                            weatherInfo.getOneApiResponse().getDailyList().get(1).getWeatherList().get(0).getIcon() +
                            WEATHER_ICON_FILE_EXTENSION;

                    Picasso.with(v.getContext()).load(iconUrl).into(((ImageView) v.findViewById(R.id.forecastIcon1)));

                    iconUrl = WEATHER_ICON_URL_PREFIX +
                            weatherInfo.getOneApiResponse().getDailyList().get(2).getWeatherList().get(0).getIcon() +
                            WEATHER_ICON_FILE_EXTENSION;

                    Picasso.with(v.getContext()).load(iconUrl).into(((ImageView) v.findViewById(R.id.forecastIcon2)));

                    iconUrl = WEATHER_ICON_URL_PREFIX +
                            weatherInfo.getOneApiResponse().getDailyList().get(3).getWeatherList().get(0).getIcon() +
                            WEATHER_ICON_FILE_EXTENSION;

                    Picasso.with(v.getContext()).load(iconUrl).into(((ImageView) v.findViewById(R.id.forecastIcon3)));

                    iconUrl = WEATHER_ICON_URL_PREFIX +
                            weatherInfo.getOneApiResponse().getDailyList().get(4).getWeatherList().get(0).getIcon() +
                            WEATHER_ICON_FILE_EXTENSION;

                    Picasso.with(v.getContext()).load(iconUrl).into(((ImageView) v.findViewById(R.id.forecastIcon4)));


                } else {
                    Log.e("Forecast call", "response is error");
                    Toast.makeText(v.getContext(),
                            "Could not connect with weather service!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
