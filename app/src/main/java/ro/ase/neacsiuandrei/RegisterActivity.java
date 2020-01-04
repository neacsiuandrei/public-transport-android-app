package ro.ase.neacsiuandrei;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ro.ase.neacsiuandrei.database.DatabaseInstance;
import ro.ase.neacsiuandrei.database.UserDao;
import ro.ase.neacsiuandrei.database.UserService;
import ro.ase.neacsiuandrei.models.User;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText firstName;
    EditText lastName;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.editNewUsername);
        password=findViewById(R.id.editNewPassword);
        firstName=findViewById(R.id.editNewFirstName);
        lastName=findViewById(R.id.editNewLastName);
        email=findViewById(R.id.editNewEmail);
    }

    public void btnNewRegister (View view){
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
        if(firstName.getText().toString().isEmpty()){
            setError("FIRST NAME ERROR", "First name is empty.");
            return;
        }
        user.setFirstName(firstName.getText().toString());
        if(lastName.getText().toString().isEmpty()){
            setError("LAST NAME ERROR", "Last name is empty.");
            return;
        }
        user.setLastName(lastName.getText().toString());
        if(email.getText().toString().isEmpty()){
            setError("EMAIL ERROR", "Email is empty.");
            return;
        }
        user.setEmail(email.getText().toString());

        DatabaseInstance db = DatabaseInstance.getInstance(this);
        UserDao userDao = db.getUserDao();
        final UserService userService = new UserService(userDao);

        Cursor users = userService.getUsers();
        if (users.moveToFirst()){
            do{
                if(users.getString(users.getColumnIndex("username")).equals(user.getUsername())){
                    setError("USER ERROR", "Username already exists");
                    return;
                }
            }while(users.moveToNext());
        }
        users.close();

        userService.addUser(user);

        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("remember", "false");
        finish();
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
}
