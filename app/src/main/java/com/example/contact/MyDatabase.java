package com.example.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Contact_Manager";

    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "create table Contact(id INTEGER primary Key,name TEXT,phone TEXT,uri TEXT)";
        String script2 = "create table Favourites(id INTEGER primary Key,name TEXT,phone TEXT,uri TEXT)";
        db.execSQL(script);
        db.execSQL(script2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public ArrayList<Contact> getAllContact(){
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String script = "select*from contact";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(script,null);
        while(cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setUriAvatar(cursor.getString(3));
            contacts.add(contact);
        }
        return contacts;
    }
    public ArrayList<Contact> getAllFavourites(){
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String script = "select*from favourites";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(script,null);
        while(cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setUriAvatar(cursor.getString(3));
            contacts.add(contact);
        }
        return contacts;
    }
    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",contact.getName());
        values.put("phone",contact.getPhone());
        values.put("uri",contact.getUriAvatar());
        db.insert("contact",null,values);
        db.close();
    }

    public void addFavourites(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",contact.getName());
        values.put("phone",contact.getPhone());
        values.put("uri",contact.getUriAvatar());
        db.insert("favourites",null,values);
        db.close();
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contact","id = ?",new String[]{String.valueOf(contact.getId())});
    }

    public void deleteFavourites(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favourites","id = ?",new String[]{String.valueOf(contact.getId())});
    }
}
