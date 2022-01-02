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

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import czajkowski.maciej.astro.R;
import czajkowski.maciej.astro.ResourceHandler;
import czajkowski.maciej.astro.storage.Record;
import czajkowski.maciej.astro.viewmodels.RecordViewModel;
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

    private static final DecimalFormat df = new DecimalFormat("###.##");


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

        RecordViewModel recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        recordViewModel.getRecord().observe(getViewLifecycleOwner(), new Observer<Record>() {
            @Override
            public void onChanged(@Nullable Record record) {
                if ( record != null ) {
                    Log.e("MainWeatherFragment", "updating data");
                    ((TextView) v.findViewById(R.id.cityName))
                            .setText(record.getName());

                    ((TextView) v.findViewById(R.id.latitude))
                            .setText(df.format(record.getLat()));

                    ((TextView) v.findViewById(R.id.longitude))
                            .setText(df.format(record.getLon()));

                    ((TextView) v.findViewById(R.id.time))
                            .setText(record.getTime());

                    ((TextView) v.findViewById(R.id.temp))
                            .setText(df.format(record.getTemp()));

                    ((TextView) v.findViewById(R.id.pressure))
                            .setText(String.valueOf(record.getPressure()));

                    ((ImageView) v.findViewById(R.id.weatherIcon)).setImageResource(ResourceHandler.getIconResource(record.getIcon()));

                    ((TextView) v.findViewById(R.id.weatherDescription))
                            .setText(record.getDescription());
                }
            }
        });
    }
}