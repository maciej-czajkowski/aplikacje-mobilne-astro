package czajkowski.maciej.astro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final static int TIME_REFRESH_INTERVAL_1S = 1000;
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String REFRESH_RATE = "refreshRate";

    private static final double MAX_LONGITUDE = 180.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LATITUDE = -90.0;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private boolean init = false;
    private int refreshRate = 0;
    private TextView cordsTextView;
    private final Handler handler = new Handler();
    private Runnable refreshRunnable;
    private BundleViewModel bundleViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init = false;
        setContentView(R.layout.activity_main);
        this.cordsTextView = findViewById(R.id.cords);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            PagerAdapter adapter = new PagerAdapter(this);
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            viewPager.setAdapter(adapter);
        }
        this.bundleViewModel = new ViewModelProvider(this).get(BundleViewModel.class);
        this.bundleViewModel.init();

        Handler handler = new Handler();
        //timer for 1s
        try {
            Runnable timeRunnable = new Runnable(){
                @Override
                public void run() {
                    updateTime();
                    handler.postDelayed(this, TIME_REFRESH_INTERVAL_1S);// move this inside the run method
                }
            };
            timeRunnable.run();
        }
        catch (Exception e) {
            Log.e("Main Activity","Issue with timer thread!");
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(LONGITUDE) ) {
            this.longitude = savedInstanceState.getDouble(LONGITUDE);
            this.latitude = savedInstanceState.getDouble(LATITUDE);
            this.refreshRate = savedInstanceState.getInt(REFRESH_RATE);
            this.update();
        } else {
            this.cordsTextView.setText(R.string.cordsViewDefaultText);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            this.showSettingsPopupWindow();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateTime() {
        TextView currentTime = findViewById(R.id.currentTime);

        @SuppressLint("DefaultLocale") String hours = String.format("%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        @SuppressLint("DefaultLocale") String minutes = String.format("%02d", Calendar.getInstance().get(Calendar.MINUTE));
        @SuppressLint("DefaultLocale") String seconds = String.format("%02d", Calendar.getInstance().get(Calendar.SECOND));
        currentTime.setText(String.join(":", hours, minutes, seconds));
    }

    public void showSettingsPopupWindow() {
        Point size = new Point();
        //get window size
        getWindowManager().getDefaultDisplay().getSize(size);

        int width = (int) (size.x - 0.25*size.x);
        int height = (int) (size.y - 0.25*size.y);

        LayoutInflater inflater = this.getLayoutInflater();
        View pwView = inflater.inflate(R.layout.settings_popup, null,false);
        final PopupWindow pw = new PopupWindow(pwView ,width,height, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            pw.showAtLocation(findViewById(R.id.viewPager), Gravity.CENTER, 0, 0);
        } else {
            pw.showAtLocation(findViewById(R.id.fragmentWrapper), Gravity.CENTER, 0, 0);
        }
        Button okButton = pwView.findViewById(R.id.okButton);
        EditText longitudeView = pwView.findViewById(R.id.longitude);
        EditText latitudeView = pwView.findViewById(R.id.latitude);
        EditText refreshRateView = pwView.findViewById(R.id.refreshRate);

        okButton.setOnClickListener( view -> {
            try {
                this.init = true;
                this.latitude = Double.parseDouble(latitudeView.getText().toString());
                this.longitude = Double.parseDouble(longitudeView.getText().toString());
                this.refreshRate = Integer.parseInt(refreshRateView.getText().toString());
                if (!validateCords(this.longitude, this.latitude)) {
                    throw new Exception();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Błędne dane!", Toast.LENGTH_LONG).show();
                return;
            }

            if (latitudeView.getText().toString().isEmpty() ||
                    longitudeView.getText().toString().isEmpty() ||
                    refreshRateView.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Wypełnij pola!", Toast.LENGTH_LONG).show();
            } else {
                this.update();
                pw.dismiss();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (init) {
            outState.putDouble(LATITUDE, this.latitude);
            outState.putDouble(LONGITUDE, this.longitude);
            outState.putInt(REFRESH_RATE, this.refreshRate);
        }
    }

    private void update() {
        try {
            if (this.refreshRunnable != null) {
                this.handler.removeCallbacks(this.refreshRunnable);
            }
            this.refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    cordsTextView.setText(MessageFormat.format("Długość: {0}, Szerokość: {1}", longitude, latitude));
                    /* sending bundle to fragments */
                    Bundle bundle = new Bundle();
                    bundle.putDouble(LATITUDE, latitude);
                    bundle.putDouble(LONGITUDE, longitude);
                    bundleViewModel.sendBundle(bundle);
                    /* to imitate time */
                    longitude++;
                    latitude++;
                    if (!validateCords(longitude, latitude)) {
                        /* reset */
                        longitude = -180;
                        longitude = -90;
                    }
                    handler.postDelayed(this, refreshRate * 1000L);// move this inside the run method
                }
            };
            refreshRunnable.run();
        } catch (Exception e) {
            Log.e("MainActivity", "Issue with location simulation thread!: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static boolean validateCords(double longitude, double latitude) {
        return longitude <= MAX_LONGITUDE && longitude >= MIN_LONGITUDE &&
                latitude <= MAX_LATITUDE && latitude >= MIN_LATITUDE;
    }


    @Override
    protected void onDestroy () {
        super.onDestroy();
        this.handler.removeCallbacks(this.refreshRunnable);
    }

    private static class PagerAdapter extends FragmentStateAdapter{


        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public PagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return SunFragment.newInstance() ;
                default:
                    return MoonFragment.newInstance();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}