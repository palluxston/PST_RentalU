package com.rentalu.pst_rentalu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class property_add_page extends AppCompatActivity {

    ImageView homeImg;
    EditText list_num, prop_type, bedroom, date_time, rent_price, remark, reporter_name, user_id;
    private String selectedFurnitureStatus;
    Button upload, cancel;
    private static final int IMAGE_PERMISSION = 100;
    private Uri imagePath;
    private Bitmap imageToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_add_page);

        homeImg = (ImageView) findViewById(R.id.house_img_choose);
        list_num = (EditText) findViewById(R.id.listRef);
        prop_type = (EditText) findViewById(R.id.property_type);
        bedroom = (EditText) findViewById(R.id.bedroom);
        date_time = (EditText) findViewById(R.id.date_time);
        rent_price = (EditText) findViewById(R.id.rent_price);
        remark = (EditText) findViewById(R.id.Remark);
        reporter_name = (EditText) findViewById(R.id.reporter_Name);
        user_id = (EditText) findViewById(R.id.user_id);
        upload = (Button) findViewById(R.id.upload);
        cancel = (Button) findViewById(R.id.Cancel);

        //to make the reporter name to be the name of current user
        Intent j = getIntent();
        reporter_name.setText(j.getStringExtra("Username"));

        //to get the user_id
        DBHelper dbHelper = new DBHelper(property_add_page.this, "property_table", null, 1);
        ArrayList<UserModel> user = dbHelper.readUser(reporter_name.getText().toString());
        user_id.setText(String.valueOf(user.get(0).getUser_id()));



        //go back when cancel is clicked
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to get the username shown in the navigation view
                Intent u = getIntent();
                String username = u.getStringExtra("Username");
                Intent i = new Intent(property_add_page.this, view_property.class);
                i.putExtra("Username", username);
                startActivity(i);
                finish();
            }
        });

        //upload the information to database
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: get the data and upload it to the database
                DBHelper dbHelper = new DBHelper(property_add_page.this, "property_table", null, 1);
                String PT = prop_type.getText().toString();
                String Bed = bedroom.getText().toString();
                String DT = date_time.getText().toString();
                String rentPriceString = rent_price.getText().toString();
                String F = selectedFurnitureStatus;
                String RM = remark.getText().toString();
                String RN = reporter_name.getText().toString();
                int UID = Integer.parseInt(user_id.getText().toString());
                //to check all the required fields are filled or not
                try {
                    if (PT.trim().isEmpty() || Bed.trim().isEmpty() || DT.trim().isEmpty() || rentPriceString.trim().isEmpty() || F.trim().isEmpty() || RM.trim().isEmpty() || RN.trim().isEmpty()){
                        Toast.makeText(property_add_page.this, "Please fill all the required fields!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        float RP = Float.parseFloat(rentPriceString);
                        if(imageToStore != null){
                            byte[] Img = getByteArrayFromBitmap(imageToStore);

                            //when all the fields are filled, add to the database
                            dbHelper.addProperty(PT, Bed, DT, RP, F, RM, RN, Img, UID);
                            Toast.makeText(property_add_page.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                            //go back to the newfeed page
                            Intent k = getIntent();
                            String username = k.getStringExtra("Username");
                            Intent i = new Intent(property_add_page.this, view_property.class);
                            i.putExtra("Username", username);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast.makeText(property_add_page.this, "Please choose an Image of your property", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        LinearLayout furnitureStatusContainer = findViewById(R.id.furniture_status_container);
        TextView furnitureStatusHint = findViewById(R.id.furniture_status_hint);
        ImageView furnitureStatusIcon = findViewById(R.id.furniture_status_icon);

        furnitureStatusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(property_add_page.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.furniture_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    selectedFurnitureStatus = item.getTitle().toString();
                    furnitureStatusHint.setText(selectedFurnitureStatus);
                    return true;
                });

                popupMenu.show();
            }
        });

    }

    //function to choose image from the device
    public void chooseImage(View objectView){
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*"); //set the type of intent
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, IMAGE_PERMISSION);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == IMAGE_PERMISSION && resultCode == RESULT_OK && data != null && data.getData() != null){
                imagePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                homeImg.setImageBitmap(imageToStore);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //get byteArray
    private byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return outputStream.toByteArray();
    }



}