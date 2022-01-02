package czajkowski.maciej.astro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import czajkowski.maciej.astro.fragments.AdditionalWeatherFragment;
import czajkowski.maciej.astro.fragments.MainWeatherFragment;
import czajkowski.maciej.astro.fragments.MoonFragment;
import czajkowski.maciej.astro.fragments.SunFragment;
import czajkowski.maciej.astro.fragments.WeatherForcastFragment;
import czajkowski.maciej.astro.retrofit.data.OpenWeatherData;
import czajkowski.maciej.astro.retrofit.oneapi.data.OneApiResponse;
import czajkowski.maciej.astro.retrofit.wrappers.OpenWeatherApi;
import czajkowski.maciej.astro.retrofit.wrappers.OpenWeatherInterfaceMetric;
import czajkowski.maciej.astro.storage.AppDataBase;
import czajkowski.maciej.astro.storage.Record;
import czajkowski.maciej.astro.storage.RecordDao;
import czajkowski.maciej.astro.viewmodels.BundleViewModel;
import czajkowski.maciej.astro.viewmodels.InternetViewModel;
import czajkowski.maciej.astro.viewmodels.RecordViewModel;
import czajkowski.maciej.astro.viewmodels.WeatherInfo;
import czajkowski.maciej.astro.viewmodels.WeatherInfoViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final static int TIME_REFRESH_INTERVAL_1S = 1000;
    private static final String REFRESH_RATE = "refreshRate";
    private static final String UID = "uid";

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");

    private PopupWindow pw;

    private int refreshRate = 300;
    private final Handler handler = new Handler();
    private Runnable refreshRunnable;
    private RecordViewModel recordViewModel;

    private boolean internetAvailability = false;

    private SpinnerWrapper spinnerWrapper;
    private RecordDao recordDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            PagerAdapter adapter = new PagerAdapter(this);
            ViewPager2 viewPager = findViewById(R.id.mainViewPager);
            viewPager.setAdapter(adapter);
        }


        this.recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        this.recordViewModel.init();

        /* init timer */
        Handler handler = new Handler();
        //timer for 1s
        try {
            Runnable timeRunnable = new Runnable() {
                @Override
                public void run() {
                    updateTime();
                    handler.postDelayed(this, TIME_REFRESH_INTERVAL_1S);// move this inside the run method
                }
            };
            timeRunnable.run();
        } catch (Exception e) {
            Log.e("Main Activity", "Issue with timer thread!");
        }

        // alert fragment regarding internet connection
        this.internetAvailability = this.isInternetAvailable();
        Log.e("MainActivity", "is internet available = " + this.isInternetAvailable());

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
//                    internetViewModel.alertFragments(internetAvailability);
                }
                handler.postDelayed(this, 1000);
            }
        };
        this.handler.post(internetConnectionRunnable);

        findViewById(R.id.refreshIcon).setOnClickListener(x -> this.update());

        /* setup db */
        AppDataBase db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "cities").allowMainThreadQueries().build();
        this.recordDao = db.recordDao();
        List<Record> records;
        records = recordDao.getAll();
        records.forEach(record -> Log.e("Database record", record.toString()));

        // spinner setup
        Spinner spinner = findViewById(R.id.citySpinner);
        this.spinnerWrapper = new SpinnerWrapper(spinner, this);
        this.spinnerWrapper.setup(records, true);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(UID)) {
                int uid = savedInstanceState.getInt(UID);
                spinnerWrapper.setSelection(uid);
            }
            int refreshRate = savedInstanceState.getInt(REFRESH_RATE);
            if (refreshRate != 0) {
                startRefreshRunnable(refreshRate);
            }
        }

        Button addButton = findViewById(R.id.addCityButton);
        addButton.setOnClickListener(e -> this.showSettingsPopupWindow());

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


    public void showSettingsPopupWindow() {
        Point size = new Point();
        //get window size
        getWindowManager().getDefaultDisplay().getSize(size);

        int width = (int) (size.x - 0.25 * size.x);
        int height = (int) (size.y - 0.25 * size.y);

        LayoutInflater inflater = this.getLayoutInflater();
        View pwView = inflater.inflate(R.layout.settings_popup, null, false);
        pw = new PopupWindow(pwView, width, height, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            pw.showAtLocation(findViewById(R.id.mainViewPager), Gravity.CENTER, 0, 0);
        } else {
//            pw.showAtLocation(findViewById(R.id.fragmentWrapper), Gravity.CENTER, 0, 0);
        }
        Button addButton = pwView.findViewById(R.id.addButton);
        EditText cityInputText = pwView.findViewById(R.id.inputCity);


        addButton.setOnClickListener(view -> {
            if (this.isInternetAvailable()) {
                if (cityInputText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Podaj nazwe miasta!", Toast.LENGTH_LONG).show();
                } else {
                    addCity(cityInputText.getText().toString());
                    Toast.makeText(getApplicationContext(), "Dodano miasto", Toast.LENGTH_LONG).show();

                }
            } else {
//                pw.dismiss();
                Toast.makeText(getApplicationContext(), "Could not add city without internet connection!", Toast.LENGTH_LONG).show();
            }
        });

        Button refreshRateButton = pwView.findViewById(R.id.refreshRateButton);

        EditText refreshRateView = pwView.findViewById(R.id.refreshRate);
        refreshRateView.setText(String.valueOf(this.refreshRate));

        refreshRateButton.setOnClickListener(view -> {
            refreshRate = Integer.parseInt(refreshRateView.getText().toString());
            startRefreshRunnable(refreshRate);
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (spinnerWrapper.getRecords().size() > 0) {
            outState.putInt(UID, spinnerWrapper.getSelected().getUid());
        }
        outState.putInt(REFRESH_RATE, this.refreshRate);
    }

    public void update() {
        if (this.isInternetAvailable()) {
            this.getWeatherInfoAsync(this.spinnerWrapper.getSelected().getName());
        }
    }

    public void showRecord(Record record) {
        recordViewModel.sendRecord(record);
    }

    private void startRefreshRunnable(int refreshRate) {
        try {
            if (this.refreshRunnable != null) {
                this.handler.removeCallbacks(this.refreshRunnable);
            }
            this.refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isInternetAvailable()) {
//                        spinnerWrapper.getRecords().forEach(r -> getWeatherInfoAsync(r.getName()));
//                        spinnerWrapper.setSelection(spinnerWrapper.getSelected().getUid());
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

    private void addCity(String city) {
        if (pw == null || !isInternetAvailable()) {
            return;
        }
        OpenWeatherInterfaceMetric apiInterface = OpenWeatherApi.getClient().create(OpenWeatherInterfaceMetric.class);

        Call<OpenWeatherData> call = apiInterface.getWeatherData(city);

        // standard data
        call.enqueue(new Callback<OpenWeatherData>() {
            @Override
            public void onResponse(@NonNull Call<OpenWeatherData> call, @NonNull Response<OpenWeatherData> response) {
                if (response.isSuccessful()) {
                    getWeatherInfoAsync(((EditText) pw.getContentView().findViewById(R.id.inputCity)).getText().toString());
//                    pw.dismiss();
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
        if (!isInternetAvailable()) {
            return;
        }
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
                                Record record = parseWeatherInfo(city, response.body(), response2.body());
                                recordDao.insertRecords(record);
                                spinnerWrapper.add(record);
                                if (record.getUid() == spinnerWrapper.getSelected().getUid()) {
                                    recordViewModel.sendRecord(record);
                                }
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

    private Record parseWeatherInfo(@NonNull String name, @NonNull OpenWeatherData response, @NonNull OneApiResponse response2) {
        Record record = new Record();
        record.setName(name);
        record.setLat(response.getCoord().getLat());
        record.setLon(response.getCoord().getLon());
        record.setDescription(response.getWeatherList().get(0).getDescription());
        record.setIcon(response.getWeatherList().get(0).getIcon());
        record.setTemp(response.getMain().getTemp());
        record.setHumidity(response.getMain().getHumidity());
        record.setPressure(response.getMain().getPressure());
        record.setWindSpeed(response.getWind().getSpeed());
        record.setWindDeg(response.getWind().getDeg());
        record.setTime(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));

        Date date = new Date((long) response2.getDailyList().get(1).getDate() * 1000);
        record.setForecast1Date(dateFormat.format(date));
        record.setForecast1Description(response2.getDailyList().get(1).getWeatherList().get(0).getDescription());
        record.setForecast1Icon(response2.getDailyList().get(1).getWeatherList().get(0).getIcon());
        record.setForecast1Temp(response2.getDailyList().get(1).getTemp().getMax() + response2.getDailyList().get(1).getTemp().getMin() / 2);

        date = new Date((long) response2.getDailyList().get(2).getDate() * 1000);
        record.setForecast2Date(dateFormat.format(date));
        record.setForecast2Description(response2.getDailyList().get(2).getWeatherList().get(0).getDescription());
        record.setForecast2Icon(response2.getDailyList().get(2).getWeatherList().get(0).getIcon());
        record.setForecast2Temp(response2.getDailyList().get(2).getTemp().getMax() + response2.getDailyList().get(2).getTemp().getMin() / 2);

        date = new Date((long) response2.getDailyList().get(3).getDate() * 1000);
        record.setForecast3Date(dateFormat.format(date));
        record.setForecast3Description(response2.getDailyList().get(3).getWeatherList().get(0).getDescription());
        record.setForecast3Icon(response2.getDailyList().get(3).getWeatherList().get(0).getIcon());
        record.setForecast3Temp(response2.getDailyList().get(3).getTemp().getMax() + response2.getDailyList().get(3).getTemp().getMin() / 2);

        date = new Date((long) response2.getDailyList().get(4).getDate() * 1000);
        record.setForecast4Date(dateFormat.format(date));
        record.setForecast4Description(response2.getDailyList().get(4).getWeatherList().get(0).getDescription());
        record.setForecast4Icon(response2.getDailyList().get(4).getWeatherList().get(0).getIcon());
        record.setForecast4Temp(response2.getDailyList().get(4).getTemp().getMax() + response2.getDailyList().get(4).getTemp().getMin() / 2);

        return record;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void updateTime() {
        TextView currentTime = findViewById(R.id.currentTime);

        @SuppressLint("DefaultLocale") String hours = String.format("%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        @SuppressLint("DefaultLocale") String minutes = String.format("%02d", Calendar.getInstance().get(Calendar.MINUTE));
        @SuppressLint("DefaultLocale") String seconds = String.format("%02d", Calendar.getInstance().get(Calendar.SECOND));
        currentTime.setText(String.join(":", hours, minutes, seconds));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.refreshRunnable);
    }

    private static class PagerAdapter extends FragmentStateAdapter {


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