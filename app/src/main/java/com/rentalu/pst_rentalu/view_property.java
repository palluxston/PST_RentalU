package com.rentalu.pst_rentalu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class view_property extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HouseViewAdapter.SelectListener {

    RecyclerView recView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    TextView username;
    NavigationView navigationView;
    Button new_upload;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(view_property.this, profile_layout.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        } else if (item.getItemId() == R.id.nav_home) {
        //


        }else if(item.getItemId() == R.id.nav_newsfeed){
            Intent i = new Intent(view_property.this, newsfeed_page.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        }  else if(item.getItemId() == R.id.nav_add_post){
            Intent i = new Intent(view_property.this, property_add_page.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        } else if(item.getItemId() == R.id.nav_setting){
            Intent i = new Intent(view_property.this, setting_layout.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        }
        else if(item.getItemId() == R.id.nav_about){
            Intent i = new Intent(view_property.this, about_us.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        }
        else if(item.getItemId() == R.id.nav_logout){
            //logout confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(view_property.this);
            builder.setMessage("Do you really want to logout?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(view_property.this, LoadingActivity.class);
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
        setContentView(R.layout.activity_view_property);

        showWelcomeDialog();

        recView = (RecyclerView) findViewById(R.id.items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



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

        MenuItem addPostMenuItem = navigationView.getMenu().findItem(R.id.nav_add_post);
        addPostMenuItem.setVisible(true);

        //to view profile
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Go to the profile page
                Intent i = new Intent(view_property.this, profile_layout.class);
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
        DBHelper dbHelper = new DBHelper(view_property.this, "user_table", null, 1);
        String Post_username = dbHelper.getUsername(propertyModel.getUser_id());
        //to inform if this is own post or not
        if(Post_username.equals(username.getText().toString())){
            int ref_list_num = propertyModel.getRef_list_num(); //get the id of specific property
            Intent i = new Intent(view_property.this, view_house.class);
            i.putExtra("Ref_Num", ref_list_num);
            i.putExtra("isOwn", true);
            i.putExtra("Username", username.getText().toString()); //to stay in the current user account
            startActivity(i);
            finish();
        }
        else{
            int ref_list_num = propertyModel.getRef_list_num(); //get the id of specific property
            Intent i = new Intent(view_property.this, view_house.class);
            i.putExtra("Ref_Num", ref_list_num);
            i.putExtra("Username", username.getText().toString()); //to stay in the current user account
            startActivity(i);
            finish();
        }


    }
    
    public void displayProperty(){
        //creating an recyclerview adapter object
        HouseViewAdapter adapter = new HouseViewAdapter(view_property.this, (HouseViewAdapter.SelectListener) this);
        recView.setLayoutManager(new LinearLayoutManager(view_property.this));

        //creating an arraylist to store the houses' information
        DBHelper dbHelper = new DBHelper(view_property.this, "property_table", null, 1);
        // Get the current username
        String username = getCurrentUsername(); // You need to implement a method to get the current username

        ArrayList<PropertyModel> houses = dbHelper.readPropertyByUsername(username);
        adapter.setHouses(houses);
        recView.setAdapter(adapter);
    }

    private String getCurrentUsername() {
        // Implement a method to get the current username, such as reading it from the Intent or SharedPreferences
        Intent intent = getIntent();
        return intent.getStringExtra("Username");
    }

    private void showWelcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You can update or delete the post by clicking that specific post.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}