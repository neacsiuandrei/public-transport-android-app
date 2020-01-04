package ro.ase.neacsiuandrei.database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ro.ase.neacsiuandrei.models.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addUser(User user);

    @Query("SELECT * FROM users")
    Cursor getUsers();

    @Query("SELECT * FROM users WHERE id=:id")
    User getUserByID(int id);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM users WHERE username=:username")
    void deleteUserFromUsername(String username);
}
