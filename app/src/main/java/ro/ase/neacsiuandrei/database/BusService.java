package ro.ase.neacsiuandrei.database;

import android.database.Cursor;

import ro.ase.neacsiuandrei.models.Bus;


public class BusService implements BusDao {
    private final BusDao tableInstance;

    public BusService(BusDao tableInstance) {
        this.tableInstance = tableInstance;
    }

    @Override
    public Long addBus(Bus bus) {
        return tableInstance.addBus(bus);
    }

    @Override
    public Cursor getBuses(int id) {
        return tableInstance.getBuses(id);
    }

    @Override
    public void deleteBus(Bus bus) {
        tableInstance.deleteBus(bus);
    }
}
