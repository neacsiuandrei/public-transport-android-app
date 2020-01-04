package ro.ase.neacsiuandrei;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import ro.ase.neacsiuandrei.models.Bus;
import ro.ase.neacsiuandrei.models.User;


public class AddBusActivity extends AppCompatActivity {

    private EditText etNumber;
    private Spinner busCapacity;
    private RatingBar busRating;
    private SeekBar avgTime;
    private Switch recommend;
    private Bus editedBus;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        etNumber=findViewById(R.id.editNumber);
        busCapacity=findViewById(R.id.editCapacity);
        busRating=findViewById(R.id.editRating);
        avgTime=findViewById(R.id.editAvgTime);
        recommend=findViewById(R.id.switch1);

        Intent intent = getIntent();
        if(intent!=null){
            editedBus = (Bus)intent.getParcelableExtra("busObj");
            if(editedBus!=null){
                etNumber.setTextKeepState(String.valueOf(editedBus.getNumber()));
                int position =0;
                switch (editedBus.getCapacity()){
                    case 20:
                        position=0;
                        break;
                    case 30:
                        position=1;
                        break;
                    case 40:
                        position=2;
                        break;
                    case 50:
                        position=3;
                        break;
                }
                busCapacity.setSelection(position);
                busRating.setRating(editedBus.getRating());
                avgTime.setProgress(editedBus.getAvgDuration());
                recommend.setChecked(editedBus.isRecommend());
            }
        }


        avgTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Average Time: " + seekBar.getProgress() + " minutes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btnSave(View view){
        try{
            Bus bus = new Bus();
            int busNumber = Integer.parseInt(etNumber.getText().toString());
            bus.setNumber(busNumber);

            int busCap = Integer.parseInt(busCapacity.getSelectedItem().toString());
            bus.setCapacity(busCap);

            float rating = busRating.getRating();
            bus.setRating(rating);

            int avgRouteTime = avgTime.getProgress();
            bus.setAvgDuration(avgRouteTime);

            boolean checked = recommend.isChecked();
            bus.setRecommend(checked);

            Intent intent = new Intent(this, BusList.class);
            intent.putExtra("busObj", bus);
            if(editedBus!=null){
                intent.putExtra("editedBus",editedBus);
            }
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Bus number can not be NULL.", Toast.LENGTH_SHORT).show();
        }
    }
}

