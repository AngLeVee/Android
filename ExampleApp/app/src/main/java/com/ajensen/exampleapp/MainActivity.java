package com.ajensen.exampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Click listener for all buttons in main activity
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tutorialButt:
                startActivity(new Intent(this, Tutorial.class));
                break;
            case R.id.currencyButt:
                startActivity(new Intent(this, CurrencyConverter.class));
                break;
            case R.id.animButt:
                startActivity(new Intent(this, Animation.class));
                break;
            case R.id.connectButt:
                startActivity(new Intent(view.getContext(), ConnectThree.class));
                break;
            case R.id.videoButt:
                startActivity(new Intent(view.getContext(), Video.class));
                break;
        }
    }
}
