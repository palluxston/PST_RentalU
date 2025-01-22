package com.rentalu.pst_rentalu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class newsfeed_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HouseViewAdapter.SelectListener {

    RecyclerView recView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    TextView username,newsfeed;
    NavigationView navigationView;
    Button new_upload;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(newsfeed_page.this, profile_layout.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        } else if (item.getItemId() == R.id.nav_home) {
        //
            Intent i = new Intent(newsfeed_page.this, view_property.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();

        } else if(item.getItemId() == R.id.nav_newsfeed){
            //
        } else if(item.getItemId() == R.id.nav_setting){
            Intent i = new Intent(newsfeed_page.this, setting_layout.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        }
        else if(item.getItemId() == R.id.nav_about){
            Intent i = new Intent(newsfeed_page.this, about_us.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        }
        else if(item.getItemId() == R.id.nav_logout){
            //logout confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(newsfeed_page.this);
            builder.setMessage("Do you really want to logout?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(newsfeed_page.this, LoadingActivity.class);
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
        return true;
    }

    //close drawer on back pressed
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_page);


        recView = (RecyclerView) findViewById(R.id.items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        newsfeed = (TextView) findViewById(R.id.txtView);
        newsfeed.setText("Newsfeed Page");

        //setting the toolbar as the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //to enable the default title of toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get the current username
        Intent i = getIntent();
        View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
        username = headerView.findViewById(R.id.userName);
        username.setText(i.getStringExtra("Username"));

        //to view profile
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Go to the profile page
                Intent i = new Intent(newsfeed_page.this, profile_layout.class);
                i.putExtra("Username", username.getText().toString());
                startActivity(i);
                finish();
            }
        });

        Intent u = getIntent();
        username.setText(u.getStringExtra("Username"));

        displayProperty();

    }

    @Override
    public void onItemClicked(PropertyModel propertyModel) {
        DBHelper dbHelper = new DBHelper(newsfeed_page.this, "user_table", null, 1);
        String postUsername = dbHelper.getUsername(propertyModel.getUser_id());
        String postPhoneNumber = dbHelper.getPhoneNumber(propertyModel.getUser_id());

        String toastMessage = "Posted By: " + postUsername + "\nPhone Number: " + postPhoneNumber;

        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    public void displayProperty(){
        //creating an recyclerview adapter object
        HouseViewAdapter adapter = new HouseViewAdapter(newsfeed_page.this, (HouseViewAdapter.SelectListener) this);
        recView.setLayoutManager(new LinearLayoutManager(newsfeed_page.this));

        //creating an arraylist to store the houses' information
        DBHelper dbHelper = new DBHelper(newsfeed_page.this, "property_table", null, 1);
        ArrayList<PropertyModel> houses = dbHelper.readProperty();

        adapter.setHouses(houses);
        recView.setAdapter(adapter);
    }

}