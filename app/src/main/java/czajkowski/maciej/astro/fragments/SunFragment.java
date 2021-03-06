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
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;

import czajkowski.maciej.astro.AstroCalculatorFactory;
import czajkowski.maciej.astro.storage.Record;
import czajkowski.maciej.astro.viewmodels.BundleViewModel;
import czajkowski.maciej.astro.R;
import czajkowski.maciej.astro.viewmodels.RecordViewModel;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;
import czajkowski.maciej.astro.viewmodels.WeatherInfoViewModel;

public class SunFragment extends Fragment {

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private View view;

    //for purpose of simulation
    private double tLong;
    private double tLati;



    public SunFragment() {
        // Required empty public constructor
    }

    public static SunFragment newInstance() {
        SunFragment fragment = new SunFragment();
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
        this.view = inflater.inflate(R.layout.sun_fragment, container, false);
        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        RecordViewModel recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        recordViewModel.getRecord().observe(getViewLifecycleOwner(), new Observer<Record>() {
            @Override
            public void onChanged(@Nullable Record record) {
                if ( record != null ) {
                    Log.e("SunFragment", "updating data");
                    update(record.getLon(), record.getLat());
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


        TextView sunTwighliteSet = this.view.findViewById(R.id.sunTwighliteSet);
        String sunTwighliteSetHours = String.format("%02d", astroCalculator.getSunInfo().getTwilightMorning().getHour());
        String sunTwighliteSetMinutes = String.format("%02d", astroCalculator.getSunInfo().getTwilightMorning().getMinute());
        sunTwighliteSet.setText(String.join(":", sunTwighliteSetHours, sunTwighliteSetMinutes));

        TextView sunTwighliteRise = this.view.findViewById(R.id.sunTwighliteRise);
        String sunTwighliteRiseHours = String.format("%02d", astroCalculator.getSunInfo().getSunrise().getHour());
        String sunTwighliteRiseMinutes = String.format("%02d", astroCalculator.getSunInfo().getSunrise().getMinute());
        sunTwighliteRise.setText(String.join(":", sunTwighliteRiseHours, sunTwighliteRiseMinutes));

        TextView sunDawnTime = this.view.findViewById(R.id.sunDawnTime);
        String sunDawnTimeHours = String.format("%02d", astroCalculator.getSunInfo().getTwilightMorning().getHour());
        String sunDawnTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getTwilightMorning().getMinute());
        sunDawnTime.setText(String.join(":", sunDawnTimeHours, sunDawnTimeMinutes));

        TextView sunDuskTime = this.view.findViewById(R.id.sunDuskTime);
        String sunDuskTimeHours = String.format("%02d", astroCalculator.getSunInfo().getTwilightEvening().getHour());
        String sunDuskTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getTwilightEvening().getMinute());
        sunDuskTime.setText(String.join(":", sunDuskTimeHours, sunDuskTimeMinutes));


        TextView sunDawnAzymut = this.view.findViewById(R.id.sunDawnAzymut);
        String sunDawnAzymutStr = String.format("%02f", astroCalculator.getSunInfo().getAzimuthRise());
        sunDawnAzymut.setText(sunDawnAzymutStr);

        TextView sunDuskAzimut = this.view.findViewById(R.id.sunDuskAzimut);
        String sunDuskAzimutStr = String.format("%02f", astroCalculator.getSunInfo().getAzimuthSet());
        sunDuskAzimut.setText(sunDuskAzimutStr);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(LATITUDE, this.tLati);
        outState.putDouble(LONGITUDE, this.tLong);
    }
}