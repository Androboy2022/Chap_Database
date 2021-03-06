package com.cyberkyj.databaseopenhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText db_Name;
    TextView status;
    ListView listView;
    String databaseName;
    int version = 2;
    String tableName = "singertb";
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db_Name = findViewById(R.id.editText);
        status = findViewById(R.id.textView);
        listView = findViewById(R.id.listView);
        Button query_Btn = findViewById(R.id.button);

        query_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseName = db_Name.getText().toString();
                boolean isOpen = createDatabase();
                if(isOpen){
                    Cursor cursor = executeQuery();
                    String[] columns = {"name", "age", "phone"};
                    int[] to={R.id.list_name, R.id.list_age, R.id.list_phone};
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.list_layout,cursor,columns,to);
                    listView.setAdapter(adapter);
                }

            }
        });
    }

    public Cursor executeQuery(){
        String query = "SELECT * FROM "+tableName;
        Cursor cursor = db.rawQuery(query,null);
        int recordCount = cursor.getCount();
        for(int i=0; i<recordCount; i++){
            cursor.moveToNext();
            String name = cursor.getString(1);
            int age = cursor.getInt(2);
            String phone = cursor.getString(3);

            println("Record #"+i+" : "+name+", "+age+", "+phone);
        }
        return cursor;
    }

    public void println(String msg){
        status.append("\n"+msg);
    }

    public boolean createDatabase(){
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context) {
            super(context, databaseName, null, version);
            println("["+databaseName+"] ????????????????????? ?????????????????????.");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE if EXISTS "+tableName);
            db.execSQL("CREATE TABLE IF NOt EXISTS "+tableName+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT, age INTEGER, phone TEXT)");
            println("["+tableName+"] ???????????? ?????????????????????.");

            db.execSQL("INSERT INTO "+tableName+"(name, age, phone) VALUES('??????', 26, '010-1000-1000')");
            db.execSQL("INSERT INTO "+tableName+"(name, age, phone) VALUES('??????', 26, '010-2000-2000')");
            db.execSQL("INSERT INTO "+tableName+"(name, age, phone) VALUES('??????', 23, '010-3000-3000')");
            println(tableName+"???????????? 3?????? ???????????? ?????????????????????.");


        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            println("["+databaseName+"] ????????????????????? ["+tableName+"] ???????????? ???????????????.");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println(oldVersion+"?????? "+newVersion+"?????? ???????????????,");
            println("itzy ???????????? ????????? ???????????????.");
            db.execSQL("CREATE TABLE IF NOt EXISTS itzy(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT, age INTEGER, phone TEXT)");
            db.execSQL("INSERT INTO itzy(name, age, phone) VALUES('??????', 22, '010-4000-4000')");
            db.execSQL("INSERT INTO itzy(name, age, phone) VALUES('??????', 22, '010-5000-5000')");
            db.execSQL("INSERT INTO itzy(name, age, phone) VALUES('??????', 21, '010-6000-6000')");
            println(tableName+"???????????? 3?????? ???????????? ?????????????????????.");

            String query = "SELECT * FROM itzy";
            Cursor cursor = db.rawQuery(query,null);
            int recordCount = cursor.getCount();
            for(int i=0; i<recordCount; i++){
                cursor.moveToNext();
                String name = cursor.getString(1);
                int age = cursor.getInt(2);
                String phone = cursor.getString(3);

                println("Record #"+i+" : "+name+", "+age+", "+phone);
            }

        }
    }
}