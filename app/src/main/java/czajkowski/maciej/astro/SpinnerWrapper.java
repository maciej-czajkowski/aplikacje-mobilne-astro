package czajkowski.maciej.astro;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

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
                Toast.makeText(parent.getContext(), "Selected: " + choice, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        if (this.cities.size() != 0) {
            this.selected = this.cities.get(0);
        } else {
            this.selected = "";
        }
    }

    public void add(Record record) {
//        this.records.removeIf(r -> r.getName().equals(record.getName()));
        for (Record r : this.records) {
            if (r.getName().equals(record.getName())) {

//                Toast.makeText(this.activity.getApplicationContext(), "This city is already in the list!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        this.records.add(record);
        this.setup(this.records, false);
        this.setSelection(getSelected().getUid());
    }

    public Record getSelected() {
        return this.records.stream().filter(r -> r.getName().equals(this.selected)).findFirst().get();
    }

    public List<Record> getRecords() {
        return this.records;
    }

    public void setSelection(int uid) {
        for (int i =0; i<this.records.size(); i++) {
            if (this.records.get(i).getUid() == uid) {
                this.spinner.setSelection(i);
            }
        }
    }
}
