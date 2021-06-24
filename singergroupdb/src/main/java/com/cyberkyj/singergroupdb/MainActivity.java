package com.cyberkyj.singergroupdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtNumber, edtNameResult, edtNumberResult;
    String db_Name = "girlgroup.db";
    int version = 1;
    myDBhelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.editText);
        edtNumber = findViewById(R.id.editText2);
        edtNameResult = findViewById(R.id.editText3);
        edtNumberResult = findViewById(R.id.editText4);

        Button btnInit = findViewById(R.id.button);
        Button btnInsert = findViewById(R.id.button2);
        Button btnUpdate = findViewById(R.id.button3);
        final Button btnDelete = findViewById(R.id.button4);
        final Button btnSelect = findViewById(R.id.button5);

        dbHelper = new myDBhelper(this);
        db = dbHelper.getWritableDatabase();

        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.onUpgrade(db,version,2);
                btnSelect.callOnClick();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString();
                int num = 0 ;
                if(!(edtNumber.getText().toString().isEmpty())){
                    num = Integer.parseInt(edtNumber.getText().toString());

                }

                if(!(name.isEmpty() || num==0)) {
                    db.execSQL("insert into groupTBL values('" + name + "', " + num + ");");
                    Toast.makeText(getApplicationContext(), "데이터가 입력되었습니다.", Toast.LENGTH_LONG).show();
                    edtName.setText("");
                    edtNumber.setText("");
                    btnSelect.callOnClick();
                }

            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("select * from groupTBL;",null);
                String strNames = "그룹이름"+"\n"+"---------------"+"\n";
                String strNumbers = "인원"+"\n"+"---------------"+"\n";

                while (cursor.moveToNext()){
                    strNames+=cursor.getString(0)+"\n";
                    strNumbers+=cursor.getInt(1)+"\n";
                }

                edtNameResult.setText(strNames);
                edtNumberResult.setText(strNumbers);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString();
                int num = 0 ;
                if(!(edtNumber.getText().toString().isEmpty())){
                    num = Integer.parseInt(edtNumber.getText().toString());

                }

                if(!(name.isEmpty() || num==0)) {
                    db.execSQL("update groupTBL set gNumber="+num+" where gName='"+name+"';");
                    Toast.makeText(getApplicationContext(), "데이터가 수정되었습니다.", Toast.LENGTH_LONG).show();
                    edtName.setText("");
                    edtNumber.setText("");
                    btnSelect.callOnClick();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                if(!(name.isEmpty())){
                    db.execSQL("delete from groupTBL where gName='"+name+"';");
                    Toast.makeText(getApplicationContext(), "데이터가 삭제되었습니다.", Toast.LENGTH_LONG).show();
                    edtName.setText("");
                    edtNumber.setText("");
                    btnSelect.callOnClick();
                }
            }
        });

    }

    public class myDBhelper extends SQLiteOpenHelper {
        public myDBhelper(Context context) {
            super(context, db_Name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table groupTBL(gName varchar(20) PRIMARY KEY, gNumber integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists groupTBL");
            onCreate(db);
            Toast.makeText(getApplicationContext(),"테이블이 초기화되었습니다.",Toast.LENGTH_LONG).show();
        }
    }
}
