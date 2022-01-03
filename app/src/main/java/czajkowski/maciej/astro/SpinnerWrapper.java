package czajkowski.maciej.astro;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import czajkowski.maciej.astro.storage.Record;

public class SpinnerWrapper {
    private Spinner spinner;
    private List<Record> records = new ArrayList<>();
    private List<String> cities;
    private MainActivity activity;
    private String selected;

    public SpinnerWrapper(Spinner spinner, MainActivity mainActivity) {
        this.spinner = spinner;
        this.activity = mainActivity;
    }

    public void setup(List<Record> records, boolean update) {
        this.records = records;
        this.cities = new ArrayList<>();
        this.records.forEach(record -> this.cities.add(record.getName()));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.activity, android.R.layout.simple_spinner_item, this.cities);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();
                selected = choice;
                activity.showRecord(getSelected());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (this.cities.size() != 0) {
            this.selected = this.cities.get(0);
        } else {
            this.selected = "";
        }
    }

    public void add(Record record) {
        for (Record r : this.records) {
            if (r.getName().equals(record.getName())) {
                return;
            }
        }

        this.records.add(record);
        this.setup(this.records, false);
        this.setSelection(getSelected().getName());
    }

    public Record getSelected() {
        try {
            return this.records.stream().filter(r -> r.getName().equals(this.selected)).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<Record> getRecords() {
        return this.records;
    }

    public void setSelection(String city) {
        for (int i = 0; i < this.records.size(); i++) {
            if (this.records.get(i).getName().equals(city)) {
                Log.e(TAG, "setSelection: selecting " + i);
                this.spinner.setSelection(i);
                this.selected = city;
            }
        }
    }
}
