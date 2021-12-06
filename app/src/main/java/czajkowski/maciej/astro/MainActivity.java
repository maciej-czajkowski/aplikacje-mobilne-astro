package czajkowski.maciej.astro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
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

    private final static int TIME_REFRESH_INTERVAL_1S = 1000; //2 minutes
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String REFRESH_RATE = "refreshRate";

    private double latitude = 0.0;
    private double longitude = 0.0;
    private int refreshRate = 0;
    private PagerAdapter adapter;
    private TextView cordsTextView;
    private final Handler handler = new Handler();
    private Runnable refreshRunnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.cordsTextView = findViewById(R.id.cords);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.adapter = new PagerAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);



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

        if (savedInstanceState != null) {
            this.longitude = savedInstanceState.getDouble(LONGITUDE);
            this.latitude = savedInstanceState.getDouble(LATITUDE);
            this.refreshRate = savedInstanceState.getInt(REFRESH_RATE);
            getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                    update();
                }
            }, false);
//            this.update();
        } else {
            this.cordsTextView.setText(R.string.cordsViewDefaultText);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        pw.showAtLocation(findViewById(R.id.viewPager), Gravity.CENTER, 0, 0);
        Button okButton = pwView.findViewById(R.id.okButton);
        EditText longitudeView = pwView.findViewById(R.id.longitude);
        EditText latitudeView = pwView.findViewById(R.id.latitude);
        EditText refreshRateView = pwView.findViewById(R.id.refreshRate);

        okButton.setOnClickListener( view -> {
            try {
                this.latitude = Double.parseDouble(latitudeView.getText().toString());
                this.longitude = Double.parseDouble(longitudeView.getText().toString());
                this.refreshRate = Integer.parseInt(refreshRateView.getText().toString());
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
        outState.putDouble(LATITUDE, this.latitude);
        outState.putDouble(LONGITUDE, this.longitude);
        outState.putInt(REFRESH_RATE, this.refreshRate);
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

                    adapter.getSunFragment().update(longitude, latitude);
                    longitude++;
                    latitude++;
                    handler.postDelayed(this, refreshRate * 1000L);// move this inside the run method
                }
            };
            refreshRunnable.run();
        } catch (Exception e) {
            Log.e("MainActivity", "Issue with location simulation thread!: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private static class PagerAdapter extends FragmentStateAdapter{
        private final SunFragment sunFragment = SunFragment.newInstance();
        private final MoonFragment moonFragment = MoonFragment.newInstance();


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
                    return this.sunFragment ;
                default:
                    return this.moonFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public SunFragment getSunFragment() { return this.sunFragment; }
    }



}