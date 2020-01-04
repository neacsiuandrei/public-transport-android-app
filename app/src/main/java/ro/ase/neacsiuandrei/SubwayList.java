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

import ro.ase.neacsiuandrei.adapters.SubwayAdapter;
import ro.ase.neacsiuandrei.database.DatabaseInstance;
import ro.ase.neacsiuandrei.database.SubwayDao;
import ro.ase.neacsiuandrei.database.SubwayService;
import ro.ase.neacsiuandrei.models.Subway;

public class SubwayList extends AppCompatActivity {

    private static final String TAG = SubwayList.class.getName();
    private ListView listView;
    private Handler handler;
    private SubwayAdapter subwayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_list);

        subwayAdapter = new SubwayAdapter(this);


        DatabaseInstance db = DatabaseInstance.getInstance(this);
        SubwayDao subwayDao = db.getSubwayDao();
        final SubwayService subService = new SubwayService(subwayDao);

        Intent intent = getIntent();
        Subway editedSubway = (Subway)intent.getParcelableExtra("editedSubway");
        if(editedSubway != null){
            subwayAdapter.subwayList.remove(editedSubway);
            subService.deleteSubwayFromDestination(editedSubway.getDestination(), MainActivity.userID);
            subwayAdapter.notifyDataSetChanged();
        }

        if(subwayAdapter.subwayList.isEmpty()){
            Cursor cursor = subService.getSubways(MainActivity.userID);

            if (cursor.moveToFirst()){
                do{
                    Subway newSubway = new Subway(cursor.getString(cursor.getColumnIndex("destination")),
                            cursor.getInt(cursor.getColumnIndex("capacity")),
                            cursor.getString(cursor.getColumnIndex("subwayLine")),
                            cursor.getInt(cursor.getColumnIndex("numberOfStations")),
                            cursor.getFloat(cursor.getColumnIndex("rating")),
                            cursor.getInt(cursor.getColumnIndex("recommend"))>0);
                    subwayAdapter.subwayList.add(newSubway);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        listView = findViewById(R.id.lvTransport);
        Subway subway = (Subway)intent.getParcelableExtra("subObj");
        if(subway!=null){
            subwayAdapter.subwayList.add(subway);
            subService.addSubway(subway);
        }

        listView.setAdapter(subwayAdapter);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(),
                                "New entries added.",
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
                        String destination = subwayAdapter.subwayList.get(position).getDestination();
                        subwayAdapter.subwayList.remove(position);
                        subService.deleteSubwayFromDestination(destination, MainActivity.userID);
                        subwayAdapter.notifyDataSetChanged();
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
                Subway sub = subwayAdapter.subwayList.get(position);
                Intent intent = new Intent(SubwayList.this, AddSubwayActivity.class);
                intent.putExtra("subObj", sub);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subway_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_subway_list:
                GetJsonSubway getJsonSubway = new GetJsonSubway(handler, subwayAdapter);
                getJsonSubway.execute("https://api.myjson.com/bins/nkaug");
                return true;
            case R.id.subway_create_report_btn:
                startActivity(new Intent(SubwayList.this, SubwayReport.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
