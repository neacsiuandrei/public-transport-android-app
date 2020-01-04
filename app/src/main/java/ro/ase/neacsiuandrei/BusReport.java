package ro.ase.neacsiuandrei;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ro.ase.neacsiuandrei.database.BusDao;
import ro.ase.neacsiuandrei.database.BusService;
import ro.ase.neacsiuandrei.database.DatabaseInstance;
import ro.ase.neacsiuandrei.models.Bus;

public class BusReport extends AppCompatActivity {

    private List<Bus> busList = new ArrayList<>();
    private LineChart lineChart;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_report);

        DatabaseInstance db = DatabaseInstance.getInstance(this);
        BusDao busDao = db.getBusDao();
        final BusService busService = new BusService(busDao);

        Cursor cursor = busService.getBuses(MainActivity.userID);

        if (cursor.moveToFirst()){
            do{
                Bus newBus = new Bus(cursor.getInt(cursor.getColumnIndex("number")),
                        cursor.getInt(cursor.getColumnIndex("capacity")),
                        cursor.getInt(cursor.getColumnIndex("avgDuration")),
                        cursor.getFloat(cursor.getColumnIndex("rating")),
                        cursor.getInt(cursor.getColumnIndex("recommend"))>0);
                busList.add(newBus);
            }while(cursor.moveToNext());
        }
        cursor.close();

        lineChart = findViewById(R.id.lineChart);

        ArrayList<Entry> ratings = new ArrayList<>();
        ArrayList<Entry> avgDuration = new ArrayList<>();
        ArrayList<Entry> capacity = new ArrayList<>();

        int i=0;
        for(Bus b : busList){
            avgDuration.add(new Entry(i, b.getAvgDuration()));
            ratings.add(new Entry(i, b.getRating()));
            capacity.add(new Entry(i, b.getCapacity()));
            i=i+1;
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(ratings,"ratings");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.BLUE);

        LineDataSet lineDataSet2 = new LineDataSet(avgDuration,"average duration");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.RED);

        LineDataSet lineDataSet3 = new LineDataSet(capacity,"capacity");
        lineDataSet3.setDrawCircles(false);
        lineDataSet3.setColor(Color.GREEN);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);

        lineChart.setData(new LineData(lineDataSets));
    }

    public void btnSaveToCSV(View view){
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents");
        dir.mkdir();
        File file = new File(dir, "BusReport.csv");

        try {
            StringBuilder data = new StringBuilder();
            data.append("Bus Number,Average Duration,Capacity,Ratings");
            for(Bus b : busList){
                data.append("\n"+b.getNumber()+","+b.getAvgDuration()+","+b.getCapacity()+","+b.getRating());
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write((data.toString()).getBytes());
            out.close();
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("FILE HAS BEEN CREATED");
            alertDialog.setMessage("The CSV file can be found inside the Documents directory.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void btnExport(View view){
        StringBuilder data = new StringBuilder();
        data.append("Bus Number,Average Duration,Capacity,Ratings");
        for(Bus b : busList){
            data.append("\n"+b.getNumber()+","+b.getAvgDuration()+","+b.getCapacity()+","+b.getRating());
        }

        try{
            FileOutputStream out = openFileOutput("BusReport.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "BusReport.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "BusReport");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Export CSV File"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
