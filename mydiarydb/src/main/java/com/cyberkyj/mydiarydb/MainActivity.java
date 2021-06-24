package com.cyberkyj.mydiarydb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatePicker datePicker;
    EditText edtDiary;
    DbHelper dbHelper;
    String databaseName = "myDB";
    SQLiteDatabase db;
    Button button;
    String date_name;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datePicker = findViewById(R.id.datePicker);
        edtDiary = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        date_name = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
        str = readDiary(date_name);
        edtDiary.setText(str);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date_name = Integer.toString(year)+"-"+Integer.toString(monthOfYear+1)+"-"+Integer.toString(dayOfMonth);
                str = readDiary(date_name);
                edtDiary.setText(str);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.getText().toString().equals("새로 저장")){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("diaryDate",date_name);
                    contentValues.put("content", edtDiary.getText().toString());
                    db.insert("myDiary",null,contentValues);
                    Toast.makeText(getApplicationContext(),"새로운 데이터가 입력됨",Toast.LENGTH_LONG).show();
                }else{
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("content", edtDiary.getText().toString());
                    String[] args={date_name};
                    db.update("myDiary", contentValues, "diaryDate=?",args);
                    Toast.makeText(getApplicationContext(),"입력된 데이터가 수정됨",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public String readDiary(String name){
        String diaryStr = null;
        Cursor cursor = db.rawQuery("select * from myDiary where diaryDate='"+name+"';",null);
        if(cursor==null){
            edtDiary.setHint("일기 없음");
            button.setText("새로 저장");
        }else{
            if(cursor.moveToNext()){
                diaryStr = cursor.getString(1);
                button.setText("수정하기");
            }else{
                edtDiary.setHint("일기 없음");
                button.setText("새로 저장");
            }
        }
        return diaryStr;
    }

    class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, databaseName, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table if not exists myDiary(diaryDate char(10), content varchar(500));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
