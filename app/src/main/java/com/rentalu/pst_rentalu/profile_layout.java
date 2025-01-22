package com.rentalu.pst_rentalu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class profile_layout extends AppCompatActivity{
    EditText username, fullName, email, password, phoneNumber;
    ImageButton back;
    Button updateProfile, deleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        
        username = (EditText) findViewById(R.id.userName);
        fullName = (EditText) findViewById(R.id.fullName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        back = (ImageButton) findViewById(R.id.backToHome);
        updateProfile = (Button) findViewById(R.id.updateProfile);
        deleteAccount = (Button) findViewById(R.id.deleteAccount);

        //current username
        Intent i = getIntent();
        username.setText(i.getStringExtra("Username"));

        //back to newfeed layout
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profile_layout.this, view_property.class);
                i.putExtra("Username", username.getText().toString()); //in case the user changed the username
                startActivity(i);
                finish();
            }
        });

        DBHelper dbHelper = new DBHelper(profile_layout.this, "user_table", null, 1);
        ArrayList<UserModel> user = dbHelper.readUser(username.getText().toString()); //to get the data of current user
        //set the data of current user into the fields
        for(UserModel data : user){
            username.setText(data.getUsername());
            fullName.setText(data.getFullname());
            email.setText(data.getEmail());
            phoneNumber.setText(data.getPhnum());
            password.setText(data.getPassword());
        }

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user_id;
                for(UserModel data : user){
                    user_id = data.getUser_id();
                    AlertDialog.Builder builder = new AlertDialog.Builder(profile_layout.this);
                    int finalUser_id = user_id;
                    builder.setMessage("Do you really want to delete your account?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.deleteUser(String.valueOf(finalUser_id));
                            Toast.makeText(profile_layout.this, "Account Deleted!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(profile_layout.this, LoadingActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    });
                    AlertDialog mDialog = builder.create();
                    mDialog.show();
                }
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user_id;
                for(UserModel data : user){
                    user_id = data.getUser_id();
                    String fn = fullName.getText().toString();
                    String un = username.getText().toString();
                    String pass = password.getText().toString();
                    String E = email.getText().toString();
                    String pn = phoneNumber.getText().toString();
                    
                    //update the data in database
                    dbHelper.updateUser(String.valueOf(user_id), fn, un, pass, E, pn);
                    Toast.makeText(profile_layout.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}