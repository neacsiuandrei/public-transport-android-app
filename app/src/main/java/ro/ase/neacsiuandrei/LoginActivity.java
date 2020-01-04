package ro.ase.neacsiuandrei;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import ro.ase.neacsiuandrei.database.DatabaseInstance;
import ro.ase.neacsiuandrei.database.UserDao;
import ro.ase.neacsiuandrei.database.UserService;
import ro.ase.neacsiuandrei.models.User;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.editUsername);
        password=findViewById(R.id.editPassword);
        remember=findViewById(R.id.cbRememberMe);

        DatabaseInstance db = DatabaseInstance.getInstance(this);
        UserDao userDao = db.getUserDao();
        final UserService userService = new UserService(userDao);

        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String check = sharedPreferences.getString("remember", "");
        if(!check.isEmpty()){
            if(!check.equals("false")){
                Cursor users = userService.getUsers();
                if (users.moveToFirst()){
                    do{
                        if(users.getInt(users.getColumnIndex("id"))==Integer.valueOf(check)){
                            User u = new User(users.getInt(users.getColumnIndex("id")),
                                    users.getString(users.getColumnIndex("username")),
                                    users.getString(users.getColumnIndex("firstName")),
                                    users.getString(users.getColumnIndex("lastName")),
                                    users.getString(users.getColumnIndex("email")),
                                    users.getString(users.getColumnIndex("password")));
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("userObj", u);
                            startActivity(intent);
                            return;
                        }
                    }while(users.moveToNext());
                }
                users.close();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }


        User root = new User("root", "root", "root", "root@root", "root");
        userService.addUser(root);

    }

    public void btnLogin(View view){
        User user = new User();
        if(username.getText().toString().isEmpty()){
            setError("USER ERROR", "Username is empty.");
            return;
        }
        user.setUsername(username.getText().toString());
        if(password.getText().toString().isEmpty()){
            setError("PASSWORD ERROR", "Password is empty.");
            return;
        }
        user.setPassword(password.getText().toString());

        DatabaseInstance db = DatabaseInstance.getInstance(this);
        UserDao userDao = db.getUserDao();
        final UserService userService = new UserService(userDao);
        Cursor users = userService.getUsers();
        if (users.moveToFirst()){
            do{
                if(users.getString(users.getColumnIndex("username")).equals(user.getUsername()) && users.getString(users.getColumnIndex("password")).equals(user.getPassword())){
                    User u = new User(users.getInt(users.getColumnIndex("id")),
                            users.getString(users.getColumnIndex("username")),
                            users.getString(users.getColumnIndex("firstName")),
                            users.getString(users.getColumnIndex("lastName")),
                            users.getString(users.getColumnIndex("email")),
                            users.getString(users.getColumnIndex("password")));
                    if(remember.isChecked()){
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", Integer.toString(u.getId()));
                        editor.apply();
                    }
                    else if(!remember.isChecked()){
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                        editor.apply();
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("userObj", u);
                    startActivity(intent);
                    return;
                }
            }while(users.moveToNext());
        }
        users.close();

        setError("LOGIN ERROR", "User or password incorrect.");
    }

    public void btnRegister(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void setError(String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void onBackPressed() {

    }
}
