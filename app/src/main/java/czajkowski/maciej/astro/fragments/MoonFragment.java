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

import com.astrocalculator.AstroCalculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import czajkowski.maciej.astro.AstroCalculatorFactory;
import czajkowski.maciej.astro.viewmodels.BundleViewModel;
import czajkowski.maciej.astro.R;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;
import czajkowski.maciej.astro.viewmodels.WeatherInfoViewModel;

public class MoonFragment extends Fragment {

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private View view;

    //for purpose of simulation
    private double tLong;
    private double tLati;



    public MoonFragment() {
        // Required empty public constructor
    }

    public static MoonFragment newInstance() {
        MoonFragment fragment = new MoonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        this.view = inflater.inflate(R.layout.moon_fragment, container, false);
        Log.d("onCreateView", this.view.toString());
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        WeatherInfoViewModel weatherInfoViewModel = new ViewModelProvider(requireActivity()).get(WeatherInfoViewModel.class);
        weatherInfoViewModel.getData().observe(getViewLifecycleOwner(), new Observer<WeatherInfo>() {
            @Override
            public void onChanged(@Nullable WeatherInfo weatherInfo) {
                if ( weatherInfo != null ) {
                    Log.e("MoonFragment", "updating data");
                    double latitude =  weatherInfo.getOpenWeatherResponse().getCoord().getLat();
                    double longitude =  weatherInfo.getOpenWeatherResponse().getCoord().getLon();
                    update(longitude, latitude);
                }
            }
        });
    }

    public void update(double longitude, double latitude) {
        this.tLati = latitude;
        this.tLong = longitude;
        this.updateViews(tLong, tLati);
    }

    public void updateViews(double longitude, double latitude) {
         AstroCalculator astroCalculator = AstroCalculatorFactory.getCurrentAstroCalculator(latitude, longitude);

        TextView moonDawnTime = this.view.findViewById(R.id.moonDawnTime);
        String moonDawnTimeHours = String.format("%02d", astroCalculator.getMoonInfo().getMoonrise().getHour());
        String moonDawnTimeMinutes = String.format("%02d", astroCalculator.getMoonInfo().getMoonrise().getMinute());
        moonDawnTime.setText(String.join(":", moonDawnTimeHours, moonDawnTimeMinutes));

        TextView moonDuskTime = this.view.findViewById(R.id.moonDuskTime);
        String moonDuskTimeHours = String.format("%02d", astroCalculator.getMoonInfo().getMoonset().getHour());
        String moonDuskTimeMinutes = String.format("%02d", astroCalculator.getMoonInfo().getMoonset().getMinute());
        moonDuskTime.setText(String.join(":", moonDuskTimeHours, moonDuskTimeMinutes));

        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        TextView moonPhase = this.view.findViewById(R.id.moonPhase);
        double moonPhasePercent = astroCalculator.getMoonInfo().getIllumination();
        moonPhase.setText(decimalFormat.format(moonPhasePercent*100));

        TextView moonSynod = this.view.findViewById(R.id.synodicDay);
        double moonSynodAge = astroCalculator.getMoonInfo().getAge();
        moonSynod.setText(decimalFormat.format((moonSynodAge)));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(LATITUDE, this.tLati);
        outState.putDouble(LONGITUDE, this.tLong);
    }
}