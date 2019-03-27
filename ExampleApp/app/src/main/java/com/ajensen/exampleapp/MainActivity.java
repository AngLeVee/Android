package com.ajensen.exampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //private Button tutorialButt;
    //private Button currencyButt;
    //private Button fadeButt;
    //private Button connectButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tutorialButt = (Button) findViewById(R.id.tutorialButt);
//        currencyButt = (Button) findViewById(R.id.currencyButt);
//        fadeButt = (Button) findViewById(R.id.fadeButt);
//        connectButt = (Button) findViewById(R.id.connectButt);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.tutorialButt:
                startActivity(new Intent(this, Tutorial.class));
                break;
            case R.id.currencyButt:
                startActivity(new Intent(this, CurrencyConverter.class));
                break;
            case R.id.fadeButt:
                startActivity(new Intent(this, Fade.class));
                break;
            case R.id.connectButt:
                startActivity(new Intent(view.getContext(), ConnectThree.class));
                break;
        }
    }


}
