package ro.ase.neacsiuandrei.database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ro.ase.neacsiuandrei.models.Subway;

@Dao
public interface SubwayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addSubway(Subway subway);

    @Query("SELECT * FROM subways WHERE userID=:id")
    Cursor getSubways(int id);

    @Delete
    void deleteSubway(Subway subway);

    @Query("DELETE FROM subways WHERE destination=:destination AND userID=:id")
    void deleteSubwayFromDestination(String destination, int id);
}
