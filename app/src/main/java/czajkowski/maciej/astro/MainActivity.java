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

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    double latitudude = 0.0;
    double longitude = 0.0;
    private AstroCalculator astroCalculator;
    private final static Calendar calendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        AstroDateTime astroDateTime = new AstroDateTime(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.ZONE_OFFSET),
                true);

        this.astroCalculator = new AstroCalculator(astroDateTime, new AstroCalculator.Location(this.latitudude, this.longitude));

        FragmentStateAdapter adapter = new PagerAdapter(this, this.astroCalculator);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

//        SunFragment fragment =  (SunFragment) getSupportFragmentManager().findFragmentById(R.id.sun_fragment);
//        Handler m_Handler = new Handler();
//        Runnable mRunnable = new Runnable(){
//            @Override
//            public void run() {
//                fragment.update();
//                m_Handler.postDelayed(this, 3000);// move this inside the run method
//            }
//        };
//        mRunnable.run();

    }

    private class PagerAdapter extends FragmentStateAdapter{
        private AstroCalculator astroCalculator;
        private SunFragment sunFragment;

        public PagerAdapter(@NonNull FragmentActivity fragmentActivity, AstroCalculator astroCalculator) {
            super(fragmentActivity);
            this.astroCalculator = astroCalculator;
            this.sunFragment = new SunFragment(this.astroCalculator);

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
                    return this.sunFragment;
//                    return new SunFragment(this.astroCalculator.getSunInfo());
                default:
                    return new MoonFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
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
        okButton.setOnClickListener( view -> {
//            this.latitudude = 0.0;
//            this.longitude = 0.0;
            pw.dismiss();
        });
    }

}