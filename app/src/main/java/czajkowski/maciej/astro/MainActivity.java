package czajkowski.maciej.astro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import czajkowski.maciej.astro.fragments.AdditionalWeatherFragment;
import czajkowski.maciej.astro.fragments.MainWeatherFragment;
import czajkowski.maciej.astro.fragments.MoonFragment;
import czajkowski.maciej.astro.fragments.SunFragment;
import czajkowski.maciej.astro.fragments.WeatherForcastFragment;
import czajkowski.maciej.astro.retrofit.data.OpenWeatherData;
import czajkowski.maciej.astro.retrofit.oneapi.data.OneApiResponse;
import czajkowski.maciej.astro.retrofit.wrappers.OpenWeatherApi;
import czajkowski.maciej.astro.retrofit.wrappers.OpenWeatherInterfaceMetric;
import czajkowski.maciej.astro.viewmodels.BundleViewModel;
import czajkowski.maciej.astro.viewmodels.InternetViewModel;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;
import czajkowski.maciej.astro.viewmodels.WeatherInfoViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String INTERNET_AVAILABILITY = "isInternetAvailable";

    private final static int TIME_REFRESH_INTERVAL_1S = 1000;
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String REFRESH_RATE = "refreshRate";

    private static final double MAX_LONGITUDE = 180.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LATITUDE = -90.0;

    private PopupWindow pw;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private boolean init = false;
    private int refreshRate = 0;
    private TextView cordsTextView;
    private final Handler handler = new Handler();
    private Runnable refreshRunnable;
    private BundleViewModel bundleViewModel;
    private WeatherInfoViewModel weatherInfoViewModel;
    private InternetViewModel internetViewModel;

    private boolean internetAvailability = false;
    private boolean lastOpSuccesfull = false;

    private SpinnerWrapper spinnerWrapper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            PagerAdapter adapter = new PagerAdapter(this);
            ViewPager2 viewPager = findViewById(R.id.mainViewPager);
            viewPager.setAdapter(adapter);
        }
        this.bundleViewModel = new ViewModelProvider(this).get(BundleViewModel.class);
        this.bundleViewModel.init();

        this.internetViewModel = new ViewModelProvider(this).get(InternetViewModel.class);
        this.internetViewModel.init();

        this.weatherInfoViewModel = new ViewModelProvider(this).get(WeatherInfoViewModel.class);
        this.weatherInfoViewModel.init();


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

        // bundle for communication with fragments
        this.internetAvailability = this.isInternetAvailable();
        Log.e("MainActivity", "is internet available = " + this.isInternetAvailable());
        this.internetViewModel.alertFragments(this.internetAvailability);

        // checks whether internet connection is constant, alert fragments if not
        Runnable internetConnectionRunnable = new Runnable() {
            @Override
            public void run() {
                boolean currentAvailability = isInternetAvailable();
                if (currentAvailability != internetAvailability) {
                    Log.e("internetConnectionRunnable", "internet connection changed from " +
                            internetAvailability + " to " + currentAvailability);
                    internetAvailability = currentAvailability;
                    if (!internetAvailability) {
                        findViewById(R.id.warningIcon).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.warningIcon).setVisibility(View.INVISIBLE);
                    }
                    internetViewModel.alertFragments(internetAvailability);
                }
                handler.postDelayed(this, 1000);
            }
        };
        this.handler.post(internetConnectionRunnable);

        findViewById(R.id.refreshIcon).setOnClickListener( x -> this.update());


        // spinner setup
        Spinner spinner = findViewById(R.id.citySpinner);
        this.spinnerWrapper = new SpinnerWrapper(spinner, this);
        ArrayList<String> cities = new ArrayList<>();
        cities.add("Łódź");
        cities.add("Warszawa");
        this.spinnerWrapper.setup(cities);


        Button addButton = findViewById(R.id.addCityButton);
        addButton.setOnClickListener(e -> this.showSettingsPopupWindow());

        if (savedInstanceState != null && savedInstanceState.containsKey(LONGITUDE) ) {
            this.longitude = savedInstanceState.getDouble(LONGITUDE);
            this.latitude = savedInstanceState.getDouble(LATITUDE);
            this.refreshRate = savedInstanceState.getInt(REFRESH_RATE);
            this.updateMoonPhases();
        } else {
//            this.cordsTextView.setText(R.string.cordsViewDefaultText);
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
        pw = new PopupWindow(pwView ,width,height, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            pw.showAtLocation(findViewById(R.id.mainViewPager), Gravity.CENTER, 0, 0);
        } else {
//            pw.showAtLocation(findViewById(R.id.fragmentWrapper), Gravity.CENTER, 0, 0);
        }
        Button okButton = pwView.findViewById(R.id.okButton);
        EditText cityInputText = pwView.findViewById(R.id.inputCity);

        EditText refreshRateView = pwView.findViewById(R.id.refreshRate);

        okButton.setOnClickListener( view -> {
                if (this.isInternetAvailable()) {
                    if (refreshRateView.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_LONG).show();
                    } else {
                        addCity(cityInputText.getText().toString());
                    }
                } else {
                    pw.dismiss();
                    Toast.makeText(getApplicationContext(), "Could not add city without internet connection!", Toast.LENGTH_LONG).show();
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

    public void update() {
        this.getWeatherInfoAsync(this.spinnerWrapper.getSelected());
    }

    private void updateMoonPhases() {
        try {
            if (this.refreshRunnable != null) {
                this.handler.removeCallbacks(this.refreshRunnable);
            }
            this.refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    /* sending bundle to fragments */
                    Bundle bundle = new Bundle();
                    bundle.putDouble(LATITUDE, latitude);
                    bundle.putDouble(LONGITUDE, longitude);
                    bundleViewModel.sendBundle(bundle);
                    handler.postDelayed(this, refreshRate * 1000L);// move this inside the run method
                }
            };
            refreshRunnable.run();
        } catch (Exception e) {
            Log.e("MainActivity", "Issue with location simulation thread!: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void addCity(String city) {
        if (pw == null) {
            return;
        }
        OpenWeatherInterfaceMetric apiInterface = OpenWeatherApi.getClient().create(OpenWeatherInterfaceMetric.class);

        Call<OpenWeatherData> call = apiInterface.getWeatherData(city);

        // standard data
        call.enqueue(new Callback<OpenWeatherData>() {
            @Override
            public void onResponse(@NonNull Call<OpenWeatherData> call, @NonNull Response<OpenWeatherData> response) {
                if (response.isSuccessful()) {
                    spinnerWrapper.add(
                            ((EditText) pw.getContentView().findViewById(R.id.inputCity)).getText().toString());
                    pw.dismiss();
                } else {
                    Log.e("Weather", "response is error");
                    Toast.makeText(getApplicationContext(),
                            "City is incorrect", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OpenWeatherData> call, @NonNull Throwable t) {
                Log.e("Weather", "failed");
                Toast.makeText(getApplicationContext(),
                        "Could not connect with weather service!", Toast.LENGTH_LONG).show();

            }
        });
    }


    private void getWeatherInfoAsync(String city) {
        OpenWeatherInterfaceMetric apiInterface = OpenWeatherApi.getClient().create(OpenWeatherInterfaceMetric.class);

        Call<OpenWeatherData> call = apiInterface.getWeatherData(city);

        // standard data
        call.enqueue(new Callback<OpenWeatherData>() {
            @Override
            public void onResponse(@NonNull Call<OpenWeatherData> call, @NonNull Response<OpenWeatherData> response) {
                if (response.isSuccessful()) {
                    Log.e("Weather", response.body().toString());

                    double lat = response.body().getCoord().getLat();
                    double lon = response.body().getCoord().getLon();

                    // make one api call
                    OpenWeatherInterfaceMetric apiInterface = OpenWeatherApi.getClient().create(OpenWeatherInterfaceMetric.class);

                    Call<OneApiResponse> callForecast = apiInterface.getOneApiCall(String.valueOf(lat), String.valueOf(lon));

                    // standard data
                    callForecast.enqueue(new Callback<OneApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<OneApiResponse> call, @NonNull Response<OneApiResponse> response2) {
                            if (response2.isSuccessful()) {
                                dispatchWeatherInfo(response.body(), response2.body());
                            } else {
                                Log.e("Forecast call", "response is error");
                                Toast.makeText(getApplicationContext(),
                                        "Could not connect with weather service!", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<OneApiResponse> call, @NonNull Throwable t) {
                            Log.e("Forecast call", "failed");
                            Toast.makeText(getApplicationContext(),
                                    "Could not connect with weather service!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Log.e("Weather", "response is error");
                    Toast.makeText(getApplicationContext(),
                            "Could not connect with weather service!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OpenWeatherData> call, @NonNull Throwable t) {
                Log.e("Weather", "failed");
                Toast.makeText(getApplicationContext(),
                        "Could not connect with weather service!", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void dispatchWeatherInfo(@NonNull OpenWeatherData response, @NonNull OneApiResponse response2) {
        WeatherInfo weatherInfo = new WeatherInfo(response, response2, LocalDateTime.now());
        this.weatherInfoViewModel.sendWeatherInfo(weatherInfo);
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                    return SunFragment.newInstance();
                case 1:
                    return MoonFragment.newInstance();
                case 2:
                    return MainWeatherFragment.newInstance();
                case 3:
                    return AdditionalWeatherFragment.newInstance();
                default:
                    return WeatherForcastFragment.newInstance();
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}