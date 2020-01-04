package ro.ase.neacsiuandrei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import ro.ase.neacsiuandrei.models.Subway;

public class AddSubwayActivity extends AppCompatActivity {

    private EditText etDestination;
    private Spinner subwayLine;
    private EditText etCapacity;
    private RatingBar subRating;
    private EditText noStations;
    private Switch recommend;
    private  Subway editSub;
    private static final String TAG = AddSubwayActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subway);

        etDestination=findViewById(R.id.editDestination);
        etCapacity=findViewById(R.id.editCapacity);
        subwayLine=findViewById(R.id.spinnerLine);
        subRating=findViewById(R.id.ratingBar);
        recommend=findViewById(R.id.switchRecommend);
        noStations=findViewById(R.id.editStations);

        Intent intent = getIntent();
        if(intent!=null){
            editSub = (Subway) intent.getParcelableExtra("subObj");
            if(editSub!=null){
                etDestination.setText(editSub.getDestination());
                etCapacity.setText(String.valueOf(editSub.getCapacity()));
                subRating.setRating(editSub.getRating());
                int position =0;
                switch (editSub.getSubwayLine()){
                    case "M1":
                        position=0;
                        break;
                    case "M2":
                        position=1;
                        break;
                    case "M3":
                        position=2;
                        break;
                    case "M4":
                        position=3;
                        break;
                }
                subwayLine.setSelection(position);
                recommend.setChecked(editSub.isRecommend());
                noStations.setText(String.valueOf(editSub.getNumberOfStations()));
            }
        }
    }

    public void btnSaveSub(View view){
        try{
            Subway sub = new Subway();

            String subwayDestination = etDestination.getText().toString();
            sub.setDestination(subwayDestination);
            int subwayCapacity = Integer.parseInt(etCapacity.getText().toString());
            sub.setCapacity(subwayCapacity);
            String subLine = subwayLine.getSelectedItem().toString();
            sub.setSubwayLine(subLine);
            float rating = subRating.getRating();
            sub.setRating(rating);
            boolean checked = recommend.isChecked();
            sub.setRecommend(checked);
            int subwayStations = Integer.parseInt(noStations.getText().toString());
            sub.setNumberOfStations(subwayStations);

            Intent intent = new Intent(this, SubwayList.class);
            intent.putExtra("subObj", sub);
            if(editSub!=null){
                intent.putExtra("editedSubway",editSub);
            }
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "NULL values were entered.", Toast.LENGTH_SHORT).show();
        }
    }
}
