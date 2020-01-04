package ro.ase.neacsiuandrei;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ro.ase.neacsiuandrei.models.User;

public class MainActivity extends AppCompatActivity {


    public static int userID;
    private TextView logInAs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logInAs = findViewById(R.id.tvLoginAs);
        Intent intent = getIntent();
        User user = (User)intent.getParcelableExtra("userObj");
        userID = user.getId();
        logInAs.setText("Logged in as " + user.getUsername());
    }

    public void btnAddSub(View view){
        startActivity(new Intent(MainActivity.this, AddSubwayActivity.class));
    }

    public void btnSeeSubList(View view){
        startActivity(new Intent(MainActivity.this, SubwayList.class));
    }

    public void btnSeeBusList (View view){
        startActivity(new Intent(MainActivity.this, BusList.class));
    }

    public void btnAddBus (View view){
        startActivity(new Intent(MainActivity.this, AddBusActivity.class));
    }

    public void btnMaps (View view){
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getBaseContext(),
                "Can't go back, log out from the menu.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("remember", "false");
                editor.apply();

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
