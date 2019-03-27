package com.ajensen.exampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyConverter extends AppCompatActivity {
    Button convertButt;
    EditText dollarET;
    TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        convertButt = (Button) findViewById(R.id.convertButt);
        dollarET = (EditText) findViewById(R.id.dollarET);
        resultTV = (TextView) findViewById(R.id.resultTV);
        convertButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] number = {1,2,3,4,5};
                DecimalFormat df = new DecimalFormat("#,###.00");
                resultTV.setText(df.format(Float.parseFloat(dollarET.getText().toString()) * 0.76));
            }
        });
    }
}
