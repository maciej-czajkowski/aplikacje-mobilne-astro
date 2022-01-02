package czajkowski.maciej.astro.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import czajkowski.maciej.astro.storage.Record;

public class RecordViewModel extends ViewModel {
    private MutableLiveData<Record> data;

    public void init()
    {
        this.data = new MutableLiveData<>();
    }

    public void sendRecord(Record record)
    {
        this.data.setValue(record);
    }

    public LiveData<Record> getRecord()
    {
        return this.data;
    }
}
