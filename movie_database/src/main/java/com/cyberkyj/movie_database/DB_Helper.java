package com.cyberkyj.movie_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB_Helper extends SQLiteOpenHelper {

    public static final String Database_Name ="MyMovies.db";
    public static final String Table_Name="movies";
    public static int version =1;

    public DB_Helper(Context context) {

        super(context, Database_Name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+Table_Name+"(id integer PRIMARY KEY, name text, year text, director text, rating text, nation text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+Table_Name+";");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

        super.onOpen(db);
    }

    public boolean insertMovie(String name, String year, String director, String rating, String nation){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("director", director);
        contentValues.put("year", year);
        contentValues.put("rating", rating);
        contentValues.put("nation", nation);

        db.insert(Table_Name,null,contentValues);

        return  true;
    }

    public boolean updateMovie(Integer id, String name, String year, String director, String rating, String nation){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("director", director);
        contentValues.put("year", year);
        contentValues.put("rating", rating);
        contentValues.put("nation", nation);


        db.update(Table_Name,contentValues,"id = ?", new String[]{Integer.toString(id)} );

        return  true;
    }

    public Integer deleteMovie(Integer id){

        SQLiteDatabase db = this.getWritableDatabase();

        return  db.delete(Table_Name, "id = ?", new String[]{Integer.toString(id)} );
    }

    public Cursor getData(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+Table_Name+" where id ="+id+";", null);
        return  cursor;

    }

    public ArrayList getAllMovies(){

        ArrayList arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from movies;", null );
        cursor.moveToNext();
        while (cursor.isAfterLast()==false){
            arrayList.add(cursor.getString(1));
            cursor.moveToNext();
        }

        return arrayList;
    }
}
