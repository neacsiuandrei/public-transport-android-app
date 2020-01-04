package ro.ase.neacsiuandrei.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ro.ase.neacsiuandrei.models.Bus;
import ro.ase.neacsiuandrei.models.Subway;
import ro.ase.neacsiuandrei.models.User;

@Database(version = 1, entities = {Bus.class, Subway.class, User.class}, exportSchema = false)
public abstract class DatabaseInstance extends RoomDatabase {

    private static volatile DatabaseInstance dbInstance;

    public abstract BusDao getBusDao();
    public abstract SubwayDao getSubwayDao();
    public abstract UserDao getUserDao();

    public static DatabaseInstance getInstance(Context context)
    {
        if(dbInstance == null)
        {
            synchronized (DatabaseInstance.class)
            {
                if(dbInstance == null)
                {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseInstance.class, "PTApp.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return dbInstance;
    }
}
