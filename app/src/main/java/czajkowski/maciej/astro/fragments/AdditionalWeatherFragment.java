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

import java.text.DecimalFormat;

import czajkowski.maciej.astro.R;
import czajkowski.maciej.astro.storage.Record;
import czajkowski.maciej.astro.viewmodels.RecordViewModel;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;
import czajkowski.maciej.astro.viewmodels.WeatherInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdditionalWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdditionalWeatherFragment extends Fragment {
    private static final DecimalFormat df = new DecimalFormat("###.##");

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

        RecordViewModel recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        recordViewModel.getRecord().observe(getViewLifecycleOwner(), new Observer<Record>() {
            @Override
            public void onChanged(@Nullable Record record) {
                if ( record != null ) {
                    Log.e("AdditionalWeatherFragment", "updating data");
                    ((TextView) v.findViewById(R.id.windSpeed))
                            .setText(df.format(record.getWindSpeed()));

                    ((TextView) v.findViewById(R.id.windDeg))
                            .setText(df.format(record.getWindDeg()));

                    ((TextView) v.findViewById(R.id.humidity))
                            .setText(String.valueOf(record.getHumidity()));
                }
            }
        });
    }
}