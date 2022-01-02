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

import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;

import czajkowski.maciej.astro.R;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;
import czajkowski.maciej.astro.viewmodels.WeatherInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainWeatherFragment extends Fragment {

    private static final String WEATHER_ICON_URL_PREFIX = "https://openweathermap.org/img/w/";
    private static final String WEATHER_ICON_FILE_EXTENSION = ".png";

    public MainWeatherFragment() {
        // Required empty public constructor
    }

    public static MainWeatherFragment newInstance() {
        MainWeatherFragment fragment = new MainWeatherFragment();
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
        return inflater.inflate(R.layout.main_weather_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        WeatherInfoViewModel weatherInfoViewModel = new ViewModelProvider(requireActivity()).get(WeatherInfoViewModel.class);
        weatherInfoViewModel.getData().observe(getViewLifecycleOwner(), new Observer<WeatherInfo>() {
            @Override
            public void onChanged(@Nullable WeatherInfo weatherInfo) {
                if ( weatherInfo != null ) {
                    Log.e("MainWeatherFragment", "updating data");
                    ((TextView) v.findViewById(R.id.cityName))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getName()));

                    ((TextView) v.findViewById(R.id.latitude))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getCoord().getLat()));

                    ((TextView) v.findViewById(R.id.longitude))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getCoord().getLon()));

                    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                    ((TextView) v.findViewById(R.id.time))
                            .setText(String.valueOf(timeFormat.format(weatherInfo.getLocalDateTime())));

                    ((TextView) v.findViewById(R.id.temp))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getMain().getTemp()));

                    ((TextView) v.findViewById(R.id.pressure))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getMain().getPressure()));

                    String iconUrl = WEATHER_ICON_URL_PREFIX +
                            weatherInfo.getOpenWeatherResponse().getWeatherList().get(0).getIcon() +
                            WEATHER_ICON_FILE_EXTENSION;

                    Log.e("MainFragment", "Icon url = " + iconUrl);

                    Picasso.with(v.getContext()).load(iconUrl).into(((ImageView) v.findViewById(R.id.weatherIcon)));

                    ((ImageView) v.findViewById(R.id.weatherIcon)).setVisibility(View.VISIBLE);

                    ((TextView) v.findViewById(R.id.weatherDescription))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getWeatherList().get(0).getDescription()));
                }
            }
        });
    }
}