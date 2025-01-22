package com.rentalu.pst_rentalu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static String DBName = "RentalDB.db"; //database name
    //table names
    private static String User_Table = "user_table";
    private static String Prop_Table = "property_table";

    //userTable data
    private static String User_Id = "user_id";
    private static String FullName = "fullname";
    private static String User_Name = "username";
    private static String Password = "password";
    private static String Email = "email";
    private static String Phnumber = "phone_number";

    //property table data
    private static String Ref_List_Num = "ref_list_num";
    private static String Prop_Type = "prop_type";
    private static String Bedroom = "bedroom";
    private static String Date_Time = "date_time";
    private static String Rent_Price = "rent_price";
    private static String Furniture = "furniture_status";
    private static String Remark = "remark";
    private static String Reporter_Name = "reporter_name";
    private static String Image = "image";
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBName, factory, version);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //increasing page size
        db.execSQL("PRAGMA page_size = 8192;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating user table
        String user_table_create = "create table " + User_Table + "(" +
                User_Id + " integer primary key autoincrement," +
                FullName + " varchar(50)," + User_Name + " varchar(20)," +
                Password + " varchar(20)," + Email + " varchar(100), " +
                Phnumber + " varchar(20))";

        db.execSQL(user_table_create);

        //creating property table
        String property_table_create = "create table " + Prop_Table + "(" +
                Ref_List_Num + " integer primary key autoincrement," + User_Id + " integer," +
                Prop_Type + " varchar(20)," + Bedroom + " varchar(100)," +
                Date_Time + " varchar(20)," + Rent_Price + " float," +
                Furniture + " varchar(50)," + Remark + " varchar(100)," +
                Reporter_Name + " varchar(50)," + Image + " BLOB, " +
                " FOREIGN KEY (" + User_Id + ") REFERENCES " + User_Table + "(" + User_Id + "))";

        db.execSQL(property_table_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + User_Table);
        db.execSQL("drop table if exists " + Prop_Table);
    }

    public void addUser(String fullName, String username, String password, String email, String Phnum){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FullName, fullName);
        cv.put(User_Name, username);
        cv.put(Password, password);
        cv.put(Email, email);
        cv.put(Phnumber, Phnum);
        db.insert(User_Table, null, cv);
        db.close();
    }

    //to get the data of only current user
    public ArrayList<UserModel> readUser(String username){
        SQLiteDatabase db = getReadableDatabase();
        Cursor user_cursor = db.rawQuery("select * from " + User_Table + " where " + User_Name + " = ?", new String[]{username});
        ArrayList<UserModel> userModelArrayList = new ArrayList<>();

        if(user_cursor.moveToFirst()){
            do {
                userModelArrayList.add(new UserModel(
                        user_cursor.getInt(0),
                        user_cursor.getString(1),
                        user_cursor.getString(2),
                        user_cursor.getString(3),
                        user_cursor.getString(4),
                        user_cursor.getString(5)
                ));
            }while(user_cursor.moveToNext());
        }
        return userModelArrayList;
    }

    public String getUsername(int user_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + User_Name + " from " + User_Table + " where " + User_Id + " =? ", new String[]{String.valueOf(user_id)});
        String username = null;

        if(cursor.moveToFirst()){
            username = cursor.getString(0);
        }
        cursor.close();
        return username;
    }

    public String getPhoneNumber(int user_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Phnumber + " from " + User_Table + " where " + User_Id + " =? ", new String[]{String.valueOf(user_id)});
        String phone_number = null;

        if(cursor.moveToFirst()){
            phone_number = cursor.getString(0);
        }
        cursor.close();
        return phone_number;
    }

    public void addProperty(String propType, String bedroom, String datetime, Float rentprice, String furniture, String remark, String reporter_name, byte[] image, int user_id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        //get the current date and time
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //format the date
        String formattedDateTime = date.format(new Date());

        cv.put(Prop_Type, propType);
        cv.put(Bedroom, bedroom);
        cv.put(Date_Time, formattedDateTime);
        cv.put(Rent_Price, rentprice);
        cv.put(Furniture, furniture);
        cv.put(Remark, remark);
        cv.put(Reporter_Name, reporter_name);
        cv.put(Image, image);
        cv.put(User_Id, user_id);
        db.insert(Prop_Table, null, cv);
        db.close();
    }

    //to get all the property data from the database
    public ArrayList<PropertyModel> readProperty(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor prop_cursor = db.rawQuery("select * from " + Prop_Table, null);
        ArrayList<PropertyModel> propModelArrayList = new ArrayList<>();

        if(prop_cursor.moveToFirst()){
            do {
                // Get the image as a byte array
                byte[] imageByteArray = prop_cursor.getBlob(9);

                // Convert byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                propModelArrayList.add(new PropertyModel(
                        prop_cursor.getInt(0),
                        prop_cursor.getInt(1),
                        prop_cursor.getString(2),
                        prop_cursor.getString(3),
                        prop_cursor.getString(4),
                        prop_cursor.getFloat(5),
                        prop_cursor.getString(6),
                        prop_cursor.getString(7),
                        prop_cursor.getString(8),
                        imageBitmap

                ));
            }while(prop_cursor.moveToNext());
        }
        return propModelArrayList;
    }


    public ArrayList<PropertyModel> readPropertyByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor userCursor = db.rawQuery("SELECT " + User_Id + " FROM " + User_Table + " WHERE " + User_Name + " = ?", new String[]{username});

        if (!userCursor.moveToFirst()) {
            // No user found with the given username
            userCursor.close();
            return new ArrayList<>(); // Return an empty list
        }

        int userId = userCursor.getInt(0);

        Cursor propCursor = db.rawQuery("SELECT * FROM " + Prop_Table + " WHERE " + User_Id + " = ?", new String[]{String.valueOf(userId)});
        ArrayList<PropertyModel> propModelArrayList = new ArrayList<>();

        if (propCursor.moveToFirst()) {
            do {
                // Get the image as a byte array
                byte[] imageByteArray = propCursor.getBlob(9);

                // Convert byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                propModelArrayList.add(new PropertyModel(
                        propCursor.getInt(0),
                        propCursor.getInt(1),
                        propCursor.getString(2),
                        propCursor.getString(3),
                        propCursor.getString(4),
                        propCursor.getFloat(5),
                        propCursor.getString(6),
                        propCursor.getString(7),
                        propCursor.getString(8),
                        imageBitmap
                ));
            } while (propCursor.moveToNext());
        }

        userCursor.close();
        propCursor.close();
        return propModelArrayList;
    }

    //to get specific property to update
    public ArrayList<PropertyModel> getSpecProperty(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor prop_cursor = db.rawQuery("select * from " + Prop_Table + " where " + Ref_List_Num + " = ?", new String[]{id});
        ArrayList<PropertyModel> propModelArrayList = new ArrayList<>();

        if(prop_cursor.moveToFirst()){
            do {
                // Get the image as a byte array
                byte[] imageByteArray = prop_cursor.getBlob(9);

                // Convert byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                propModelArrayList.add(new PropertyModel(
                        prop_cursor.getInt(0),
                        prop_cursor.getInt(1),
                        prop_cursor.getString(2),
                        prop_cursor.getString(3),
                        prop_cursor.getString(4),
                        prop_cursor.getFloat(5),
                        prop_cursor.getString(6),
                        prop_cursor.getString(7),
                        prop_cursor.getString(8),
                        imageBitmap

                ));
            }while(prop_cursor.moveToNext());
        }
        return propModelArrayList;
    }

    //for authentication of user
    public boolean authenticationUser(String username, String password){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + User_Table + " where " + User_Name + " =? and " + Password + " =?", new String[]{username, password});

        boolean userExists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return userExists;
    }

    //to check if username is already taken or not
    public boolean usernameTaken(String username){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + User_Table + " where " + User_Name + " = ?", new String[]{username});

        boolean usernameAlreadyTaken = cursor.moveToFirst();
        cursor.close();
        db.close();

        return usernameAlreadyTaken;
    }

    public void deleteUser(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(User_Table, "user_id = ?", new String[]{id});
    }

    public void updateUser(String user_id, String fullName, String username, String password, String email, String Phnum){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(User_Id, user_id);
        cv.put(FullName, fullName);
        cv.put(User_Name, username);
        cv.put(Password, password);
        cv.put(Email, email);
        cv.put(Phnumber, Phnum);
        db.update(User_Table, cv, "user_id = ?", new String[]{user_id});

        // Update the Property_Table with the new username
        ContentValues propertyCv = new ContentValues();
        propertyCv.put(Reporter_Name, username);
        db.update(Prop_Table, propertyCv, "user_id = ?", new String[]{user_id});
        db.close();
    }

    public void deleteProperty(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Prop_Table, "ref_list_num = ?", new String[]{id});
    }

    public void updateProperty(String ref_list_num, String propType, String bedroom, String datetime, Float rentprice, String furniture, String remark, String reporter_name, byte[] image, String user_id){
        SQLiteDatabase db  = getWritableDatabase();
        ContentValues cv = new ContentValues();
        //get the current date and time
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //format the date
        String formattedDateTime = date.format(new Date());

        cv.put(Ref_List_Num, ref_list_num);
        cv.put(User_Id, user_id);
        cv.put(Prop_Type, propType);
        cv.put(Bedroom, bedroom);
        cv.put(Date_Time, formattedDateTime);
        cv.put(Rent_Price, rentprice);
        cv.put(Furniture, furniture);
        cv.put(Remark, remark);
        cv.put(Reporter_Name, reporter_name);

        //if the value is null, then the photo is not changed
        //if not photo will be updated too
        if(image!=null){
            cv.put(Image, image);
        }

        db.update(Prop_Table, cv, Ref_List_Num + " = ?", new String[]{ref_list_num});
        db.close();
    }
}
