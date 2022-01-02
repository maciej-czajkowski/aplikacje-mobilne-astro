package czajkowski.maciej.astro;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SpinnerWrapper {
    private Spinner spinner;
    private List<String> cities = new ArrayList<>();
    private MainActivity activity;
    private String selected;

    public SpinnerWrapper(Spinner spinner, MainActivity mainActivity) {
        this.spinner = spinner;
        this.activity = mainActivity;
    }

    public void setup(List<String> cities) {
        this.cities = cities;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.activity, android.R.layout.simple_spinner_item, cities);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();
                selected = choice;
                activity.update();
                Toast.makeText(parent.getContext(), "Selected: " + choice, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        this.selected = this.cities.get(0);
    }

    public void add(String city) {
        this.cities.add(city);
        this.setup(this.cities);
    }

    public String getSelected() {
        return selected;
    }
}
