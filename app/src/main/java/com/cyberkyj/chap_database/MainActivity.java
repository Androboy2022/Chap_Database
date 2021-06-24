package com.cyberkyj.chap_database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText db_Name, tb_Name;
    TextView status;
    String database_Name, table_Name;
    SQLiteDatabase db;
    boolean table_created = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db_Name = findViewById(R.id.edtDbName);
        tb_Name = findViewById(R.id.edtTbName);
        status = findViewById(R.id.status);
        Button database_Btn = findViewById(R.id.createDb);
        Button table_Btn = findViewById(R.id.createTb);

        database_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database_Name = db_Name.getText().toString();
                createDatabase(database_Name);
            }
        });

        table_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_Name = tb_Name.getText().toString();
                createTable(table_Name);
            }
        });

    }

    public void createDatabase(String name){
        if(name.isEmpty()){
            println("데이터베이스 이름을 먼저 입력해주세요");
        }else {
            db = openOrCreateDatabase(name, MODE_PRIVATE, null);
            println("[ "+name+" ] 데이터베이스 생성이 완료되었습니다.");
        }
    }

    public void createTable(String name){
        if(db==null){
            println("데이터베이스를 먼저 생성하세요");
            return;
        }else{
            if(name.isEmpty()){
                println("테이블 이름을 먼저 입력해주세요");
            }else{
                db.execSQL("DROP TABLE IF EXISTS "+name);
                db.execSQL("CREATE TABLE IF NOT EXISTS "+name+"(_id integer PRIMARY KEY AUTOINCREMENT,"
                        +" name text, age integer, phone text)");
                if(table_created==false) {
                    println("[ " + name + " ] 테이블 생성이 완료되었습니다.");
                    insertRecord(table_Name);
                }
                table_created = true;



            }
        }
    }

    public void insertRecord(String name){
        db.execSQL("INSERT INTO "+name+"(name, age, phone) VALUES('정연', 26, '010-1000-1000')");
        db.execSQL("INSERT INTO "+name+"(name, age, phone) VALUES('모모', 26, '010-2000-2000')");
        db.execSQL("INSERT INTO "+name+"(name, age, phone) VALUES('채영', 23, '010-2000-2000')");
        println(name+"테이블에 3개의 레코드를 입력하였습니다.");
    }

    public void insert_Click(View view) {
        ContentValues recordValue = new ContentValues();
        recordValue.put("name","다현");
        recordValue.put("age",24);
        recordValue.put("phone","010-4000-4000");
        db.insert(table_Name,null,recordValue);
        println(table_Name+"테이블에 1개의 레코드를 입력하였습니다.");
    }

    public void update_Click(View view) {
        //db.execSQL("UPDATE "+table_Name+" SET age=age+1 WHERE name='다현';");
        ContentValues recordValue = new ContentValues();
        recordValue.put("age", 25);
        String[] args = {"다현"};
        db.update(table_Name,recordValue,"name=?",args);
        println(table_Name+"테이블에 1개의 레코드를 수정하였습니다.");
    }

    public void delete_Click(View view) {
        //db.execSQL("DELETE FROM  "+table_Name+" WHERE age>=25");
        String[] args={"25"};
        int count = db.delete(table_Name,"age>=?",args);
        println("["+table_Name+"] 테이블에 "+count+"개의 데이터를 삭제하였습니다.");
    }

    public void println(String msg){
        status.append("\n"+msg);
    }
}