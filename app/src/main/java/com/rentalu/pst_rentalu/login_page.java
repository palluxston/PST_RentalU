package com.rentalu.pst_rentalu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login_page extends AppCompatActivity {

    TextView signup;
    EditText name, pass;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        name  = (EditText) findViewById(R.id.txtUsername);
        pass = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        signup = (TextView) findViewById(R.id.Signup);

        //logging into the account
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String password = pass.getText().toString();
                DBHelper dbHelper = new DBHelper(login_page.this, "user_table", null, 1);
                
                //check the user is in database of not
                if(dbHelper.authenticationUser(username, password)){
                    Intent i = new Intent(login_page.this, view_property.class);
                    i.putExtra("Username", username);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(login_page.this, "User not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //go to sign up
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login_page.this, signup_page.class);
                startActivity(i);
                finish();
            }
        });
    }
}