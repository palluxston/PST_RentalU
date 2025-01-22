package com.rentalu.pst_rentalu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup_page extends AppCompatActivity {

    TextView login;
    EditText fullName, username, email, phNum, password, conpassword;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        login = (TextView) findViewById(R.id.to_login);
        fullName = (EditText) findViewById(R.id.txtFullname);
        username = (EditText) findViewById(R.id.txtUsername);
        email = (EditText) findViewById(R.id.txtEmail);
        phNum = (EditText) findViewById(R.id.txtPhnum);
        password = (EditText) findViewById(R.id.txtPassword);
        conpassword = (EditText) findViewById(R.id.txtconPassword);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        //go to login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signup_page.this, login_page.class);
                startActivity(i);
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(signup_page.this, "user_table", null, 1);
                String FN = fullName.getText().toString();
                String UN = username.getText().toString();
                String e = email.getText().toString();
                String ph = phNum.getText().toString();
                String pass = password.getText().toString();
                String cpass = conpassword.getText().toString();

                if (FN.trim().isEmpty() || UN.trim().isEmpty() || e.trim().isEmpty() || ph.trim().isEmpty() || pass.trim().isEmpty()) {
                    Toast.makeText(signup_page.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    if(dbHelper.usernameTaken(UN)){
                        Toast.makeText(signup_page.this, "Username is already taken!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!pass.equals(cpass)){
                            Toast.makeText(signup_page.this, "Password must match!", Toast.LENGTH_SHORT).show();
                        }
                        else if(!e.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                            Toast.makeText(signup_page.this, "Invalid Email Pattern!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            dbHelper.addUser(FN, UN, pass, e, ph);
                            dbHelper.close();
                            clear();
                            Toast.makeText(signup_page.this, "Signed UP!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(signup_page.this, login_page.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }
        });
    }

    //to clear the fields after successfully signed up
    public void clear(){
        fullName.setText("");
        username.setText("");
        email.setText("");
        phNum.setText("");
        password.setText("");
        conpassword.setText("");
    }
}