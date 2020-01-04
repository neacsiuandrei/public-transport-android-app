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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ro.ase.neacsiuandrei.database.DatabaseInstance;
import ro.ase.neacsiuandrei.database.SubwayDao;
import ro.ase.neacsiuandrei.database.SubwayService;
import ro.ase.neacsiuandrei.models.Subway;

public class SubwayReport extends AppCompatActivity {

    private List<Subway> subwayList = new ArrayList<>();
    private BarChart barChart;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_report);

        DatabaseInstance db = DatabaseInstance.getInstance(this);
        SubwayDao subwayDao = db.getSubwayDao();
        final SubwayService subService = new SubwayService(subwayDao);

        Cursor cursor = subService.getSubways(MainActivity.userID);

        if (cursor.moveToFirst()){
            do{
                Subway newSubway = new Subway(cursor.getString(cursor.getColumnIndex("destination")),
                        cursor.getInt(cursor.getColumnIndex("capacity")),
                        cursor.getString(cursor.getColumnIndex("subwayLine")),
                        cursor.getInt(cursor.getColumnIndex("numberOfStations")),
                        cursor.getFloat(cursor.getColumnIndex("rating")),
                        cursor.getInt(cursor.getColumnIndex("recommend"))>0);
                subwayList.add(newSubway);
            }while(cursor.moveToNext());
        }
        cursor.close();

        barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> avgRatingsM1 = new ArrayList<>();
        ArrayList<BarEntry> avgRatingsM2 = new ArrayList<>();
        ArrayList<BarEntry> avgRatingsM3 = new ArrayList<>();
        ArrayList<BarEntry> avgRatingsM4 = new ArrayList<>();

        avgRatingsM1.add(new BarEntry(0, getAvgRating("M1")));
        avgRatingsM2.add(new BarEntry(1, getAvgRating("M2")));
        avgRatingsM3.add(new BarEntry(2, getAvgRating("M3")));
        avgRatingsM4.add(new BarEntry(3, getAvgRating("M4")));

        ArrayList<IBarDataSet> lineDataSets = new ArrayList<>();

        BarDataSet barDataSetM1 = new BarDataSet(avgRatingsM1,"M1");
        barDataSetM1.setColor(Color.YELLOW);
        BarDataSet barDataSetM2 = new BarDataSet(avgRatingsM2,"M2");
        barDataSetM2.setColor(Color.BLUE);
        BarDataSet barDataSetM3 = new BarDataSet(avgRatingsM3,"M3");
        barDataSetM3.setColor(Color.RED);
        BarDataSet barDataSetM4 = new BarDataSet(avgRatingsM4,"M4");
        barDataSetM4.setColor(Color.GREEN);

        lineDataSets.add(barDataSetM1);
        lineDataSets.add(barDataSetM2);
        lineDataSets.add(barDataSetM3);
        lineDataSets.add(barDataSetM4);

        barChart.setData(new BarData(lineDataSets));
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
        File file = new File(dir, "SubwayReport.csv");

        try {
            StringBuilder data = new StringBuilder();
            data.append("SubwayLine,AverageRating");
            data.append("\n"+"M1"+","+getAvgRating("M1"));
            data.append("\n"+"M2"+","+getAvgRating("M2"));
            data.append("\n"+"M3"+","+getAvgRating("M3"));
            data.append("\n"+"M4"+","+getAvgRating("M4"));
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
        data.append("SubwayLine,AverageRating");
        data.append("\n"+"M1"+","+getAvgRating("M1"));
        data.append("\n"+"M2"+","+getAvgRating("M2"));
        data.append("\n"+"M3"+","+getAvgRating("M3"));
        data.append("\n"+"M4"+","+getAvgRating("M4"));

        try{
            FileOutputStream out = openFileOutput("SubwayAvgRatingReport.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "SubwayAvgRatingReport.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "SubwayAvgRatingReport");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Export CSV File"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public float getAvgRating(String subwayLine){

        float avgRating=0;
        int count=0;

        for(Subway s : subwayList){
            if(s.getSubwayLine().equals(subwayLine)){
                avgRating += s.getRating();
                count += 1;
            }
        }
        return avgRating/count;
    }


}
