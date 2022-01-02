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
import czajkowski.maciej.astro.ResourceHandler;
import czajkowski.maciej.astro.storage.Record;
import czajkowski.maciej.astro.viewmodels.RecordViewModel;
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

        RecordViewModel recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        recordViewModel.getRecord().observe(getViewLifecycleOwner(), new Observer<Record>() {
            @Override
            public void onChanged(@Nullable Record record) {
                if ( record != null ) {
                    ((TextView) v.findViewById(R.id.forecastDate1)).setText(record.getForecast1Date());
                    ((TextView) v.findViewById(R.id.forecastDate2)).setText(record.getForecast2Date());
                    ((TextView) v.findViewById(R.id.forecastDate3)).setText(record.getForecast3Date());
                    ((TextView) v.findViewById(R.id.forecastDate4)).setText(record.getForecast4Date());

                    ((TextView) v.findViewById(R.id.forecastTemp1))
                            .setText(df.format(record.getForecast1Temp()));
                    ((TextView) v.findViewById(R.id.forecastTemp2))
                            .setText(df.format(record.getForecast2Temp()));
                    ((TextView) v.findViewById(R.id.forecastTemp3))
                            .setText(df.format(record.getForecast3Temp()));
                    ((TextView) v.findViewById(R.id.forecastTemp4))
                            .setText(df.format(record.getForecast4Temp()));

                    ((TextView) v.findViewById(R.id.forecastDesc1))
                            .setText(record.getForecast1Description());
                    ((TextView) v.findViewById(R.id.forecastDesc2))
                            .setText(record.getForecast2Description());
                    ((TextView) v.findViewById(R.id.forecastDesc3))
                            .setText(record.getForecast3Description());
                    ((TextView) v.findViewById(R.id.forecastDesc4))
                            .setText(record.getForecast4Description());

                    ((ImageView) v.findViewById(R.id.forecastIcon1)).setImageResource(ResourceHandler.getIconResource(record.getForecast1Icon()));
                    ((ImageView) v.findViewById(R.id.forecastIcon2)).setImageResource(ResourceHandler.getIconResource(record.getForecast2Icon()));
                    ((ImageView) v.findViewById(R.id.forecastIcon3)).setImageResource(ResourceHandler.getIconResource(record.getForecast3Icon()));
                    ((ImageView) v.findViewById(R.id.forecastIcon4)).setImageResource(ResourceHandler.getIconResource(record.getForecast4Icon()));

                } else {
                    Log.e("Forecast call", "response is error");
                    Toast.makeText(v.getContext(),
                            "Could not connect with weather service!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
