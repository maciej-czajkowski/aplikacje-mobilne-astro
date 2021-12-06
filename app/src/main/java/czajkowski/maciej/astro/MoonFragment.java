package czajkowski.maciej.astro;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.astrocalculator.AstroCalculator;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoonFragment extends Fragment {

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String REFRESH_RATE = "refreshRate";

    private final static int TIME_REFRESH_INTERVAL_1S = 1000; //2 minutes

    private View view;
    private Handler handler = new Handler();;
    private Runnable refreshRunnable = null;


    //for purpose of simulation
    private double tLong;
    private double tLati;

    private int refreshRate;
    private boolean init = false;

    public MoonFragment() {
        // Required empty public constructor
    }

    public static MoonFragment newInstance() {
        MoonFragment fragment = new MoonFragment();
        Bundle args = new Bundle();
//        args.putDouble(LATITUDE, longitude);
//        args.putDouble(LONGITUDE, latitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            this.longitude = getArguments().getDouble(LONGITUDE);
//            this.latitude = getArguments().getDouble(LATITUDE);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.moon_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        this.view = v;
        super.onViewCreated(v, savedInstanceState);

        if (savedInstanceState != null) {
            this.update(savedInstanceState.getDouble(LONGITUDE),
                    savedInstanceState.getDouble(LATITUDE),
                    savedInstanceState.getInt(REFRESH_RATE));
        }else {
//            cords.setText("Szerokość: " + 0 + ", Długość: " + 0);
        }
        this.updateTime();
        MoonFragment moon = this;
        Handler handler = new Handler();
        //timer for 1s
        try {
            Runnable timeRunnable = new Runnable(){
                @Override
                public void run() {
                    moon.updateTime();
                    handler.postDelayed(this, TIME_REFRESH_INTERVAL_1S);// move this inside the run method
                }
            };
            timeRunnable.run();
        }
        catch (Exception e) {
            Log.e("moonFragment","Issue with timer thread!");
        }
    }

    public void update(double longitude, double latitude, int refreshRate) {
        this.tLati = latitude;
        this.tLong = longitude;
        this.updateViews(tLong, tLati);


//        this.latitude = latitude;
//        this.longitude = longitude;
        this.refreshRate = refreshRate;

        MoonFragment moon = this;
        try {
            this.handler = new Handler();
            if (this.refreshRunnable != null) {
                this.handler.removeCallbacks(this.refreshRunnable);
            }

            this.refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    moon.updateViews(tLong, tLati);
                    tLati++;
                    tLong++;
                    handler.postDelayed(this, refreshRate * 1000L);// move this inside the run method
                }
            };
            refreshRunnable.run();
        } catch (Exception e) {
            Log.e("MoonFragment", "Issue with location simulation thread!");
        }
    }

    public void updateViews(double longitude, double latitude) {
//        TextView cords = this.view.findViewById(R.id.moonCords);
//        cords.setText("Długość: " + longitude + ", Szerokość: " + latitude);

        AstroCalculator astroCalculator = AstroCalculatorFactory.getCurrentAstroCalculator(latitude, longitude);

//        TextView moonDawnTime = this.view.findViewById(R.id.moonDawnTime);
//        String moonDawnTimeHours = String.format("%02d", astroCalculator.getSunInfo().getTwilightMorning().getHour());
//        String moonDawnTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getTwilightMorning().getMinute());
//        moonDawnTime.setText(String.join(":", moonDawnTimeHours, moonDawnTimeMinutes));
//
//        TextView moonDuskTime = this.view.findViewById(R.id.moonDuskTime);
//        String moonDuskTimeHours = String.format("%02d", astroCalculator.getSunInfo().getTwilightEvening().getHour());
//        String moonDuskTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getTwilightEvening().getMinute());
//        moonDuskTime.setText(String.join(":", moonDuskTimeHours, moonDuskTimeMinutes));
//
//        TextView moonSundownTime = this.view.findViewById(R.id.moonSundownTime);
//        String moonSundownTimeHours = String.format("%02d", astroCalculator.getSunInfo().getSunset().getHour());
//        String moonSundownTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getSunset().getMinute());
//        moonSundownTime.setText(String.join(":", moonSundownTimeHours, moonSundownTimeMinutes));
//
//        TextView moonSunupTime = this.view.findViewById(R.id.moonSunupTime);
//        String moonSunupTimeHours = String.format("%02d", astroCalculator.getSunInfo().getSunrise().getHour());
//        String moonSunupTimeMinutes = String.format("%02d", astroCalculator.getSunInfo().getSunrise().getMinute());
//        moonSunupTime.setText(String.join(":", moonSunupTimeHours, moonSunupTimeMinutes));
//
//        TextView moonDawnAzymut = this.view.findViewById(R.id.moonDawnAzymut);
//        String moonDawnAzymutStr = String.format("%02f", Double.valueOf(astroCalculator.getSunInfo().getAzimuthRise()));
//        moonDawnAzymut.setText(moonDawnAzymutStr);
//
//        TextView moonDuskAzimut = this.view.findViewById(R.id.moonDuskAzimut);
//        String moonDuskAzimutStr = String.format("%02f", Double.valueOf(astroCalculator.getSunInfo().getAzimuthSet()));
//        moonDuskAzimut.setText(moonDuskAzimutStr);
    }

    public void updateTime() {
//        TextView currentTime = this.view.findViewById(R.id.moonCurrentTime);

//        String hours = String.format("%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//        String minutes = String.format("%02d", Calendar.getInstance().get(Calendar.MINUTE));
//        String seconds = String.format("%02d", Calendar.getInstance().get(Calendar.SECOND));
//        currentTime.setText(String.join(":", hours, minutes, seconds));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(LATITUDE, this.tLati);
        outState.putDouble(LONGITUDE, this.tLong);
        outState.putInt(REFRESH_RATE, this.refreshRate);
    }
}