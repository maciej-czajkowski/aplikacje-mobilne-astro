package czajkowski.maciej.astro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SunFragment extends Fragment implements Updateable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int TIME_REFRESH_INTERVAL = 2000; //2 minutes


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView dusk;
    private TextView dawn;
    private TextView duskCivil;
    private AstroCalculator astroCalculator;
    private View view;
    private PhaseUpdater phaseUpdater;
    private final static int REFRESH_INTERVAL = 200;


    public SunFragment() {
        // Required empty public constructor
    }

    public SunFragment(AstroCalculator astroCalculator) {
        this.astroCalculator = astroCalculator;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SunFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SunFragment newInstance(String param1, String param2) {
        SunFragment fragment = new SunFragment(null);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sun_fragment, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        this.view = v;
        this.update();
        SunFragment sun = this;
        Handler m_Handler = new Handler();
        Runnable mRunnable = new Runnable(){
            @Override
            public void run() {
                sun.update();
                m_Handler.postDelayed(this, 1000);// move this inside the run method
            }
        };
        mRunnable.run();

    }

    @Override
    public void update() {
        TextView currentTime = this.view.findViewById(R.id.sunCurrentTime);
        String hours = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        String minutes = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        String seconds = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        currentTime.setText(String.join(":", hours, minutes, seconds));

        TextView sunDawnTime = this.view.findViewById(R.id.sunDawnTime);
        String sunDawnTimeHours = String.format("%02d", this.astroCalculator.getSunInfo().getTwilightMorning().getHour());
        String sunDawnTimeMinutes = String.format("%02d", this.astroCalculator.getSunInfo().getTwilightMorning().getMinute());
        sunDawnTime.setText(String.join(":", sunDawnTimeHours, sunDawnTimeMinutes));

        TextView sunDuskTime = this.view.findViewById(R.id.sunDuskTime);
        String sunDuskTimeHours = String.format("%02d", this.astroCalculator.getSunInfo().getTwilightEvening().getHour());
        String sunDuskTimeMinutes = String.format("%02d", this.astroCalculator.getSunInfo().getTwilightEvening().getMinute());
        sunDuskTime.setText(String.join(":", sunDuskTimeHours, sunDuskTimeMinutes));

        TextView sunSundownTime = this.view.findViewById(R.id.sunSundownTime);
        String sunSundownTimeHours = String.format("%02d", this.astroCalculator.getSunInfo().getSunset().getHour());
        String sunSundownTimeMinutes = String.format("%02d", this.astroCalculator.getSunInfo().getSunset().getMinute());
        sunSundownTime.setText(String.join(":", sunSundownTimeHours, sunSundownTimeMinutes));

        TextView sunSunupTime = this.view.findViewById(R.id.sunSunupTime);
        String sunSunupTimeHours = String.format("%02d", this.astroCalculator.getSunInfo().getSunrise().getHour());
        String sunSunupTimeMinutes = String.format("%02d", this.astroCalculator.getSunInfo().getSunrise().getMinute());
        sunSunupTime.setText(String.join(":", sunSunupTimeHours, sunSunupTimeMinutes));

    }
}