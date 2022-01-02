package czajkowski.maciej.astro.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import czajkowski.maciej.astro.R;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;
import czajkowski.maciej.astro.viewmodels.WeatherInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdditionalWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdditionalWeatherFragment extends Fragment {

    public AdditionalWeatherFragment() {
        // Required empty public constructor
    }


    public static AdditionalWeatherFragment newInstance( ) {
        AdditionalWeatherFragment fragment = new AdditionalWeatherFragment();

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
        return inflater.inflate(R.layout.fragment_additional_weather, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        WeatherInfoViewModel weatherInfoViewModel = new ViewModelProvider(requireActivity()).get(WeatherInfoViewModel.class);
        weatherInfoViewModel.getData().observe(getViewLifecycleOwner(), new Observer<WeatherInfo>() {
            @Override
            public void onChanged(@Nullable WeatherInfo weatherInfo) {
                if ( weatherInfo != null ) {
                    Log.e("AdditionalWeatherFragment", "updating data");
                    ((TextView) v.findViewById(R.id.windSpeed))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getWind().getSpeed()));

                    ((TextView) v.findViewById(R.id.windDeg))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getWind().getDeg()));

                    ((TextView) v.findViewById(R.id.humidity))
                            .setText(String.valueOf(weatherInfo.getOpenWeatherResponse().getMain().getHumidity()));
                }
            }
        });
    }
}