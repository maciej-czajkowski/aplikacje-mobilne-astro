package czajkowski.maciej.astro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;

import java.util.Calendar;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SunFragment extends Fragment {

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String REFRESH_RATE = "refreshRate";

    private final static int TIME_REFRESH_INTERVAL_1S = 1000; //2 minutes

    private View view;
    private int id;
    //for purpose of simulation
    private double tLong;
    private double tLati;

    private int refreshRate;
    private boolean init = false;

    public SunFragment() {
        // Required empty public constructor
    }

    public static SunFragment newInstance() {
        SunFragment fragment = new SunFragment();
        Bundle args = new Bundle();
//        args.putDouble(LATITUDE, longitude);
//        args.putDouble(LONGITUDE, latitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            this.longitude = getArguments().getDouble(LONGITUDE);
//            this.latitude = getArguments().getDouble(LATITUDE);
        }
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
        Random rand = new Random();
        id = rand.nextInt(50);
        Log.e("id" ,String.valueOf(id));
        this.view = v;
        Log.e("view", v.toString());
    }

    public void update(double longitude, double latitude) {
        this.tLati = latitude;
        this.tLong = longitude;
        this.updateViews(tLong, tLati);
    }

    public void updateViews(double longitude, double latitude) {
        AstroCalculator astroCalculator = AstroCalculatorFactory.getCurrentAstroCalculator(latitude, longitude);
        Log.e("myid" ,String.valueOf(id));

        TextView sunDawnTime = this.view.findViewById(R.id.sunDawnTime);
        String sunDawnTimeHours = String.format("%02d", astroCalculator.getSunInfo().getTwilightMorning().getHour());
        String sunDawnTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getTwilightMorning().getMinute());
        sunDawnTime.setText(String.join(":", sunDawnTimeHours, sunDawnTimeMinutes));

        TextView sunDuskTime = this.view.findViewById(R.id.sunDuskTime);
        String sunDuskTimeHours = String.format("%02d", astroCalculator.getSunInfo().getTwilightEvening().getHour());
        String sunDuskTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getTwilightEvening().getMinute());
        sunDuskTime.setText(String.join(":", sunDuskTimeHours, sunDuskTimeMinutes));

        TextView sunSundownTime = this.view.findViewById(R.id.sunSundownTime);
        String sunSundownTimeHours = String.format("%02d", astroCalculator.getSunInfo().getSunset().getHour());
        String sunSundownTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getSunset().getMinute());
        sunSundownTime.setText(String.join(":", sunSundownTimeHours, sunSundownTimeMinutes));

        TextView sunSunupTime = this.view.findViewById(R.id.sunSunupTime);
        String sunSunupTimeHours = String.format("%02d", astroCalculator.getSunInfo().getSunrise().getHour());
        String sunSunupTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getSunrise().getMinute());
        sunSunupTime.setText(String.join(":", sunSunupTimeHours, sunSunupTimeMinutes));

        TextView sunDawnAzymut = this.view.findViewById(R.id.sunDawnAzymut);
        String sunDawnAzymutStr = String.format("%02f", Double.valueOf(astroCalculator.getSunInfo().getAzimuthRise()));
        sunDawnAzymut.setText(sunDawnAzymutStr);

        TextView sunDuskAzimut = this.view.findViewById(R.id.sunDuskAzimut);
        String sunDuskAzimutStr = String.format("%02f", Double.valueOf(astroCalculator.getSunInfo().getAzimuthSet()));
        sunDuskAzimut.setText(sunDuskAzimutStr);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(LATITUDE, this.tLati);
        outState.putDouble(LONGITUDE, this.tLong);
        outState.putInt(REFRESH_RATE, this.refreshRate);
    }

    public View isGetView() {
        return this.view;
    }
}