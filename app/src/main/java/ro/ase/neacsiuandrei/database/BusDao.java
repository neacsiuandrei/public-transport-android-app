package ro.ase.neacsiuandrei.database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ro.ase.neacsiuandrei.models.Bus;

@Dao
public interface BusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addBus(Bus bus);

    @Query("SELECT * FROM buses WHERE userID=:id")
    Cursor getBuses(int id);

    @Delete
    void deleteBus(Bus bus);
}
