package com.ajensen.exampleapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ConnectThree extends AppCompatActivity {

    //Player 1 = red; Player 2 = yellow
    int player = 1;
    int redNum = 0; //number of red tokens
    int yellNum = 0; //number of yellow tokens
    ImageView[][] grid;
    TextView winnerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect3);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Connect 3");
        winnerTV = (TextView) findViewById(R.id.winnerTV);
        grid = new ImageView[3][3];
        grid[0][0] = (ImageView) findViewById(R.id.connectA1);
        grid[0][1] = (ImageView) findViewById(R.id.connectA2);
        grid[0][2] = (ImageView) findViewById(R.id.connectA3);
        grid[1][0] = (ImageView) findViewById(R.id.connectB1);
        grid[1][1] = (ImageView) findViewById(R.id.connectB2);
        grid[1][2] = (ImageView) findViewById(R.id.connectB3);
        grid[2][0] = (ImageView) findViewById(R.id.connectC1);
        grid[2][1] = (ImageView) findViewById(R.id.connectC2);
        grid[2][2] = (ImageView) findViewById(R.id.connectC3);
    }

    public void resetBoard(View view) {
        winnerTV.setVisibility(View.GONE);
        player = 1;
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].setClickable(true);
                grid[i][j].animate().alpha(0);
                grid[i][j].setTag("none");
            }
    }

    public void placeToken(final View view) {
        ImageView imageView = (ImageView) view;
        imageView.setClickable(false);
        if (player == 1) {
            imageView.setTag("Red");
            imageView.setImageResource(R.drawable.red);
            redNum++;
            player = 2;
        } else {
            imageView.setTag("Yellow");
            imageView.setImageResource(R.drawable.yellow);
            yellNum++;
            player = 1;
        }
        imageView.animate().alpha(1).setDuration(1000);
        if (redNum > 2 || yellNum > 2) { //Only check for winner if enough pieces on board
            String winner = checkWin();
            if (winner != null && !winner.equals("none")) {
                winnerTV.setText(winner + " wins!");
                winnerTV.setVisibility(View.VISIBLE);
                for (int i = 0; i < grid.length; i++)
                    for (int j = 0; j < grid[0].length; j++)
                        grid[i][j].setClickable(false);
            }
        }
    }

    public String checkWin() {
        for (int i = 0; i < 3; i++)
            if (grid[i][0].getTag().toString() == grid[i][1].getTag().toString()
                    && grid[i][1].getTag().toString() == grid[i][2].getTag().toString())
                return grid[i][0].getTag().toString();

        for (int i = 0; i < 3; i++)
            if (grid[0][i].getTag().toString() == grid[1][i].getTag()
                    && grid[1][i].getTag().toString() == grid[2][i].getTag().toString())
                return grid[0][i].getTag().toString();

        if (grid[0][0].getTag().toString() == grid[1][1].getTag().toString()
                && grid[1][1].getTag().toString() == grid[2][2].getTag().toString())
            return grid[0][0].getTag().toString();

        if (grid[0][2].getTag().toString() == grid[1][1].getTag().toString()
                && grid[1][1].getTag().toString() == grid[2][0].getTag().toString())
            return grid[0][2].getTag().toString();

        return null;
    }
}
