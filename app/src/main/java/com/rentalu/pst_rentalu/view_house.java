package com.rentalu.pst_rentalu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class view_house extends AppCompatActivity {

    ImageView house_img;
    ImageButton backtoHome;
    EditText ref_num, prop_type, bedroom, date_time, rent_price, remark, reporter_name;
    private String selectedFurnitureStatus;
    Button update, delete;
    LinearLayout buttons;

    private static final int IMAGE_PERMISSION = 100;
    private Uri imagePath;
    private Bitmap imageToStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house);

        house_img = (ImageView) findViewById(R.id.house_img);
        ref_num = (EditText) findViewById(R.id.listRef);
        prop_type = (EditText) findViewById(R.id.property_type);
        bedroom = (EditText) findViewById(R.id.bedroom);
        date_time = (EditText) findViewById(R.id.date_time);
        rent_price = (EditText) findViewById(R.id.rent_price);
        remark = (EditText) findViewById(R.id.Remark);
        reporter_name = (EditText) findViewById(R.id.reporter_Name);
        buttons = (LinearLayout) findViewById(R.id.buttons);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        backtoHome = (ImageButton) findViewById(R.id.backToHome);
        LinearLayout furnitureStatusContainer = findViewById(R.id.furniture_status_container);
        TextView furnitureStatusHint = findViewById(R.id.furniture_status_hint);



        Intent i = getIntent();
        int id = i.getIntExtra("Ref_Num", 0);
        boolean own = i.getBooleanExtra("isOwn", false);
        String currentUserName = i.getStringExtra("Username");
        Log.d("TAG", "onCreate: currentUserName" + currentUserName);
        DBHelper dbHelper = new DBHelper(view_house.this, "property_table", null, 1);
        ArrayList<PropertyModel> house = dbHelper.getSpecProperty(String.valueOf(id));

        if(own){
            buttons.setVisibility(View.VISIBLE);
        }
        else{
            buttons.setVisibility(View.INVISIBLE);
        }
        PropertyModel propertyModel = house.get(0);
        house_img.setImageBitmap(propertyModel.getImage());
        ref_num.setText(String.valueOf(propertyModel.getRef_list_num()));
        prop_type.setText(propertyModel.getProp_type());
        bedroom.setText(propertyModel.getBedroom());
        date_time.setText(String.valueOf(propertyModel.getDate_time()));
        rent_price.setText(String.valueOf(propertyModel.getRent_price()));
        selectedFurnitureStatus = (propertyModel.getFurniture());
        furnitureStatusHint.setText(selectedFurnitureStatus);
        remark.setText(propertyModel.getRemark());
        reporter_name.setText(propertyModel.getReporter_name());
//        user_id.setText(String.valueOf(propertyModel.getUser_id()));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view_house.this);
                builder.setMessage("Do you really want to delete this post?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteProperty(String.valueOf(propertyModel.getRef_list_num()));
                        Toast.makeText(view_house.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(view_house.this, view_property.class);
                        i.putExtra("Username", reporter_name.getText().toString());
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ref_list_num = Integer.parseInt(ref_num.getText().toString());
                int user_id = propertyModel.getUser_id();
                String PT = prop_type.getText().toString();
                String BD = bedroom.getText().toString();
                String DT = date_time.getText().toString();
                float RP = Float.parseFloat(rent_price.getText().toString());
                String F = selectedFurnitureStatus;
                String RE = remark.getText().toString();
                String RN = reporter_name.getText().toString();
                try{
                    //default as no new photo is selected
                    byte[] img = null;
                    //if new photo is selected
                    if(imageToStore!=null){
                        img = getByteArrayFromBitmap(imageToStore);
                    }
                        dbHelper.updateProperty(String.valueOf(ref_list_num), PT, BD, DT, RP, F, RE, RN,img, String.valueOf(user_id));
                        Toast.makeText(view_house.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();

                        //go back to newfeed page
                        Intent i = new Intent(view_house.this, view_property.class);
                        i.putExtra("Username", RN);
                        startActivity(i);
                        finish();

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });



        furnitureStatusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(view_house.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.furniture_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    selectedFurnitureStatus = item.getTitle().toString();
                    furnitureStatusHint.setText(selectedFurnitureStatus);
                    return true;
                });

                popupMenu.show();
            }
        });

        backtoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view_house.this, view_property.class);
                i.putExtra("Username", currentUserName);
                startActivity(i);
                finish();
            }
        });


    }
    
    public void chooseImage(View view){
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
                house_img.setImageBitmap(imageToStore);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return outputStream.toByteArray();
    }
}