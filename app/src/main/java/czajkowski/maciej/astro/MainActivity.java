package czajkowski.maciej.astro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private double latitudude = 0.0;
    private double longitude = 0.0;
    private int refreshRate = 0;
    private PagerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.adapter = new PagerAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

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
            if (latitudeView.getText().toString().isEmpty() ||
                    longitudeView.getText().toString().isEmpty() ||
                    refreshRateView.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Wypełnij pola!", Toast.LENGTH_LONG).show();
            } else {
                try {
                this.latitudude = Double.parseDouble(latitudeView.getText().toString());
                this.longitude = Double.parseDouble(longitudeView.getText().toString());
                this.refreshRate = Integer.parseInt(refreshRateView.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Błędne dane!", Toast.LENGTH_LONG).show();
                    return;
                }

                this.adapter.getSunFragment().update(this.longitude, this.latitudude, this.refreshRate);
//                this.adapter.getMoonFragment().update(this.longitude, this.latitudude);
                pw.dismiss();
            }
        });
    }

    private class PagerAdapter extends FragmentStateAdapter{
        private SunFragment sunFragment = SunFragment.newInstance();

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
//                    this.sunFragment = SunFragment.newInstance();
                    return this.sunFragment ;
//                    return new SunFragment(this.astroCalculator.getSunInfo());
                default:
                    return new MoonFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public SunFragment getSunFragment() { return this.sunFragment; }
    }

}