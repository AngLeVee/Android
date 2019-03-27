package com.ajensen.exampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Tutorial extends AppCompatActivity {
    private Button appButt;
    private EditText userET;
    private EditText passET;
    private ImageView catIV;
    private String name;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        Boolean thing = true;
        appButt = findViewById(R.id.appButt);
        userET = (EditText) findViewById(R.id.userET);
        passET = (EditText) findViewById(R.id.passET);
        catIV = (ImageView) findViewById(R.id.catIV);
        catIV.setTag("R.Drawable.cat");
    }

    //When Login button clicked
    public void login(View view){
        Log.i("appButt", "button clicked");
        name = userET.getText().toString();
        pass = passET.getText().toString();
        if (name.isEmpty() || name.trim().isEmpty())
        {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String string = "hello there";
            String[] split = string.split(" ");
            if (catIV.getTag() == "R.Drawable.cat") {
                catIV.setImageResource(R.drawable.cat2);
                catIV.setTag("R.Drawable.cat2");
            } else {
                catIV.setImageResource(R.drawable.cat);
                catIV.setTag("R.Drawable.cat");
            }
            Toast.makeText(this, "Hello " + name, Toast.LENGTH_SHORT).show();
        }
    }
}
