package czajkowski.maciej.astro.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecords(Record... records);

    @Delete
    void delete(Record record);

    @Query("SELECT * FROM cities")
    List<Record> getAll();
}
