package com.cyberkyj.movie_database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MovieActivity extends AppCompatActivity {

    EditText name, director, year, rating, nation;
    DB_Helper mydb;
    int value=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        name = (EditText)findViewById(R.id.editText_name);
        director = (EditText)findViewById(R.id.editText_director);
        year = (EditText)findViewById(R.id.editText_year);
        rating = (EditText)findViewById(R.id.editText_rate);
        nation = (EditText)findViewById(R.id.editText_nation);

        mydb = new DB_Helper(this);
        Intent intent = getIntent();
        value = intent.getIntExtra("id", 0);
        if(value > 0){
            Cursor cursor = mydb.getData(value);
            cursor.moveToNext();
            String n = cursor.getString(1);
            String y = cursor.getString(2);
            String d = cursor.getString(3);
            String r = cursor.getString(4);
            String na = cursor.getString(5);

            if(cursor.isClosed()){
                cursor.close();
            }

            Button save = (Button)findViewById(R.id.button_save);
            save.setVisibility(View.INVISIBLE);

            name.setText(n);
            year.setText(y);
            director.setText(d);
            rating.setText(r);
            nation.setText(na);
        }


    }

    public void insert(View v){
        if(mydb.insertMovie(name.getText().toString(), year.getText().toString(),
                director.getText().toString(), rating.getText().toString(), nation.getText().toString())){
            Toast.makeText(getApplicationContext()," 데이터가 추가되었음", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(getApplicationContext()," 데이터가 추가되지 않았음", Toast.LENGTH_LONG).show();
        }
        finish();
    }


    public void update(View v){
        if(value>0){
            if(mydb.updateMovie(value, name.getText().toString(), year.getText().toString(),
                    director.getText().toString(), rating.getText().toString(), nation.getText().toString())){
                Toast.makeText(getApplicationContext()," 데이터가 수정되었음", Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(getApplicationContext()," 데이터가 수정되지 않았음", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }
    public void delete(View v){
        if(value>0){
            mydb.deleteMovie(value);
            Toast.makeText(getApplicationContext()," 데이터가 삭제되었음", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext()," 데이터가 삭제되지 않았음", Toast.LENGTH_LONG).show();
        }

        finish();
    }


}

