//Program: LeVeeP2
//File: TipsterActivity
//Programmer: Angela LeVee (alevee@cnm.edu)
//Objective: Holds methods for activity_tipster

package mobiletipster.leveep2.cis2237.com.leveep2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class TipsterActivity extends AppCompatActivity {

    EditText totalET;
    EditText dinersET;
    TextView totalTV;
    TextView tipTV;
    TextView tipamtTV;
    TextView subtotalTV;
    TextView priceperTV;
    SeekBar tipseek;
    Button calculate;
    Button reset;

    TipsterCalc tipster;

    java.text.NumberFormat currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipster);

        currency = NumberFormat.getCurrencyInstance();
        tipster = new TipsterCalc();

        totalET = (EditText) findViewById(R.id.totalET);
        totalTV = (TextView) findViewById(R.id.totalTV);
        dinersET = (EditText) findViewById(R.id.dinersET);
        tipTV = (TextView) findViewById(R.id.tipTV);
        tipamtTV = (TextView) findViewById(R.id.tipamtTV);
        subtotalTV = (TextView) findViewById(R.id.subtotalTV);
        priceperTV = (TextView) findViewById(R.id.priceperTV);
        tipseek = (SeekBar) findViewById(R.id.tipseek);
        calculate = (Button) findViewById(R.id.calc);
        reset = (Button) findViewById(R.id.reset);

        totalET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String total = totalET.getText().toString();
                if (total.equals("")) {
                    totalTV.setText(currency.format(0));
                } else {
                    totalTV.setText(currency.format(Double.parseDouble(total)));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (tipTV.getText().equals("") && dinersET.getText().equals("")) {
                    Toast.makeText(totalET.getContext(), "Please set tip % and diners", Toast.LENGTH_SHORT).show();
                }else if (tipTV.getText().equals("")) {
                    Toast.makeText(totalET.getContext(), "Please set tip %", Toast.LENGTH_SHORT).show();
                } else if (dinersET.getText().equals("")) {
                    Toast.makeText(totalET.getContext(), "Please set diners", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dinersET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (tipTV.getText().equals("") && totalET.getText().equals("")) {
                    Toast.makeText(totalET.getContext(), "Please set tip % and total", Toast.LENGTH_SHORT).show();
                } else if (tipTV.getText().equals("")) {
                    Toast.makeText(totalET.getContext(), "Please set tip %", Toast.LENGTH_SHORT).show();
                } else if (totalET.getText().equals("")) {
                    Toast.makeText(totalET.getContext(), "Please set total", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dinersET.setText("1");

        tipseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tipTV.setText(String.valueOf(tipseek.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tipTV.setText(String.valueOf(tipseek.getProgress()));
                if (totalTV.getText().equals("") && dinersET.getText().equals("")) {
                    Toast.makeText(totalET.getContext(), "Please set total and diners", Toast.LENGTH_SHORT).show();
                }else if (totalTV.getText().equals("")) {
                    Toast.makeText(tipseek.getContext(), "Please set total", Toast.LENGTH_SHORT).show();
                } else if (dinersET.getText().equals("")) {
                    Toast.makeText(tipseek.getContext(), "Please set diners", Toast.LENGTH_SHORT).show();
                } else {
                    CalcTip();
                }
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalTV.getText().equals("") && tipTV.getText().equals("") && dinersET.getText().equals("")) {
                    Toast.makeText(totalET.getContext(), "Please set total, diners, and tip", Toast.LENGTH_SHORT).show();
                }else if (totalTV.getText().equals("")) {
                    Toast.makeText(tipseek.getContext(), "Please set total", Toast.LENGTH_SHORT).show();
                } else if (dinersET.getText().equals("")) {
                    Toast.makeText(tipseek.getContext(), "Please set diners", Toast.LENGTH_SHORT).show();
                } else if (tipTV.getText().equals("")){
                    Toast.makeText(tipseek.getContext(), "Please set tip", Toast.LENGTH_SHORT).show();
                }else {
                    CalcTip();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalET.setText("");
                totalTV.setText("");
                dinersET.setText("1");
                tipseek.setProgress(15);
                tipTV.setText("");
                tipamtTV.setText("");
                subtotalTV.setText("");
                priceperTV.setText("");
            }
        });
    }

    private void CalcTip() {
        int tipseeker = tipseek.getProgress();
        int diners = Integer.parseInt(dinersET.getText().toString());
        double total = Double.parseDouble(totalET.getText().toString());

        tipster.setInputData(tipseeker, diners, total);

        tipamtTV.setText(currency.format(tipster.getTipAmount()));
        subtotalTV.setText(currency.format(tipster.getSubtotal()));
        priceperTV.setText(currency.format(tipster.getPerDiner()));
    }
}