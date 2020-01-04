package ro.ase.neacsiuandrei.database;

import android.database.Cursor;

import ro.ase.neacsiuandrei.models.Subway;

public class SubwayService implements SubwayDao {
    private final SubwayDao tableInstance;

    public SubwayService(SubwayDao tableInstance) {
        this.tableInstance = tableInstance;
    }

    @Override
    public Long addSubway(Subway subway) {
        return tableInstance.addSubway(subway);
    }

    @Override
    public Cursor getSubways(int id) {
        return tableInstance.getSubways(id);
    }

    @Override
    public void deleteSubway(Subway subway) {
        tableInstance.deleteSubway(subway);
    }

    @Override
    public void deleteSubwayFromDestination(String destination, int id) {
        tableInstance.deleteSubwayFromDestination(destination, id);
    }
}
