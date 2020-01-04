package ro.ase.neacsiuandrei;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ro.ase.neacsiuandrei.adapters.BusAdapter;
import ro.ase.neacsiuandrei.database.DatabaseInstance;
import ro.ase.neacsiuandrei.database.BusDao;
import ro.ase.neacsiuandrei.database.BusService;
import ro.ase.neacsiuandrei.models.Bus;

public class BusList extends AppCompatActivity {

    private static final String TAG = BusList.class.getName();
    private ListView listView;
    private Handler handler;
    private BusAdapter busAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        busAdapter = new BusAdapter(this);

        DatabaseInstance db = DatabaseInstance.getInstance(this);
        BusDao busDao = db.getBusDao();
        final BusService busService = new BusService(busDao);

        Intent intent = getIntent();
        Bus editedBus = (Bus)intent.getParcelableExtra("editedBus");
        if(editedBus != null){
            busAdapter.busList.remove(editedBus);
            busService.deleteBus(editedBus);
            busAdapter.notifyDataSetChanged();
        }

        if(busAdapter.busList.isEmpty()){
            Cursor cursor = busService.getBuses(MainActivity.userID);

            if (cursor.moveToFirst()){
                do{
                    Bus newBus = new Bus(cursor.getInt(cursor.getColumnIndex("number")),
                            cursor.getInt(cursor.getColumnIndex("capacity")),
                            cursor.getInt(cursor.getColumnIndex("avgDuration")),
                            cursor.getFloat(cursor.getColumnIndex("rating")),
                            cursor.getInt(cursor.getColumnIndex("recommend"))>0);
                    busAdapter.busList.add(newBus);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        listView = findViewById(R.id.lvBuses);
        Bus bus = (Bus)intent.getParcelableExtra("busObj");
        if(bus != null){
            busAdapter.busList.add(bus);
            busService.addBus(bus);
        }

        listView.setAdapter(busAdapter);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(),
                                "New entries added",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(parent.getContext());
                alertBuilder.setTitle("Do you want to delete this item?");
                alertBuilder.setPositiveButton("Yes.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bus newBus = busAdapter.busList.get(position);
                        busAdapter.busList.remove(position);
                        busService.deleteBus(newBus);
                        busAdapter.notifyDataSetChanged();
                    }
                });
                alertBuilder.setNegativeButton("No.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bus bus = busAdapter.busList.get(position);
                Intent intent = new Intent(BusList.this, AddBusActivity.class);
                intent.putExtra("busObj", bus);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bus_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_list_buses:
                GetJsonBuses getJsonBuses = new GetJsonBuses(handler, busAdapter);
                getJsonBuses.execute("https://api.myjson.com/bins/18k398");
                return true;
            case R.id.bus_create_report_btn:
                startActivity(new Intent(BusList.this, BusReport.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
