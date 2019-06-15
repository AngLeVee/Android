package com.ajensen.exampleapp;

import android.animation.Animator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Animation extends AppCompatActivity {

    AnimateListener animateListener;
    ImageView catAnimIV;
    Boolean fin;
    int animNum = 2; //set to Fade
    int moveNum;
    Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Animations");
        buttons = new Button[]{findViewById(R.id.allButt), findViewById(R.id.fadeButt),
                findViewById(R.id.fmButt), findViewById(R.id.frButt), findViewById(R.id.moveButt),
                findViewById(R.id.msButt), findViewById(R.id.rotateButt), findViewById(R.id.rsButt),
                findViewById(R.id.scaleButt)};

        animateListener = new AnimateListener();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        moveNum = size.x + 5; //screen width + 5

        catAnimIV = (ImageView) findViewById(R.id.catAnimIV);
        catAnimIV.setTag("R.drawable.cat");
    }

    public void setAnim(View view) {
        animNum = Integer.parseInt(view.getTag().toString());
        startAnim(view);
    }

    public void startAnim(View view) {
        fin = false;
        view.setClickable(false); //User can't interrupt animation
        for (int i = 0; i < buttons.length; i++)
            buttons[i].setClickable(false);

        switch (animNum) {
            case 1: //All
                //Duration is in milliseconds
                catAnimIV.animate().alpha(0).translationXBy(moveNum).rotation(360).scaleX(.5f).scaleY(.5f)
                        .setDuration(1500).setListener(animateListener);
                break;
            case 2: //Fade
                catAnimIV.animate().alpha(0).setDuration(1500).setListener(animateListener);
                break;
            case 3: //Fade & Move
                catAnimIV.animate().alpha(0).translationXBy(moveNum).setDuration(1500).setListener(animateListener);
                break;
            case 4: //Fade & Rotate
                catAnimIV.animate().alpha(0).rotation(360).setDuration(1500).setListener(animateListener);
                break;
            case 5: //Move
                catAnimIV.animate().translationXBy(moveNum).setDuration(1500).setListener(animateListener);
                break;
            case 6: //Move & Scale
                catAnimIV.animate().translationXBy(moveNum).scaleX(.5f).scaleY(.5f).setDuration(1500).setListener(animateListener);
                break;
            case 7: //Rotate
                catAnimIV.animate().rotation(360).setDuration(1500).setListener(animateListener);
                break;
            case 8: //Rotate & Scale
                catAnimIV.animate().rotation(360).scaleX(.5f).scaleY(.5f).setDuration(1500).setListener(animateListener);
                break;
            case 9: //Scale
                catAnimIV.animate().scaleX(.5f).scaleY(.5f).setDuration(1500).setListener(animateListener);
                break;
        }
    }

    class AnimateListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (!fin) {
                if (animNum < 7) //if image is no longer visible mid-animation, switch pictures
                    if (catAnimIV.getTag() == "R.drawable.cat") {
                        catAnimIV.setImageResource(R.drawable.cat2);
                        catAnimIV.setTag("R.drawable.cat2");
                    } else {
                        catAnimIV.setImageResource(R.drawable.cat);
                        catAnimIV.setTag("R.drawable.cat");
                    }
                fin = true;
                switch (animNum) {
                    case 1: //All
                        catAnimIV.animate().alpha(1).translationXBy(-moveNum).rotation(0).scaleX(1).scaleY(1)
                                .setDuration(1500).setListener(animateListener);
                        break;
                    case 2: //Fade
                        catAnimIV.animate().alpha(1).setDuration(1500).setListener(animateListener);
                        break;
                    case 3: //Fade & Move
                        catAnimIV.animate().alpha(1).translationXBy(-moveNum).setDuration(1500).setListener(animateListener);
                        break;
                    case 4: //Fade & Rotate
                        catAnimIV.animate().alpha(1).rotation(0).setDuration(1500).setListener(animateListener);
                        break;
                    case 5: //Move
                        catAnimIV.animate().translationXBy(-moveNum).setDuration(1500).setListener(animateListener);
                        break;
                    case 6: //Move & Scale
                        catAnimIV.animate().translationXBy(-moveNum).scaleX(1).scaleY(1).setDuration(1500).setListener(animateListener);
                        break;
                    case 7: //Rotate
                        catAnimIV.animate().rotation(0).setDuration(1500).setListener(animateListener);
                        break;
                    case 8: //Rotate & Scale
                        catAnimIV.animate().rotation(0).scaleX(1).scaleY(1).setDuration(1500).setListener(animateListener);
                        break;
                    case 9: //Scale
                        catAnimIV.animate().scaleX(1).scaleY(1).setDuration(1500).setListener(animateListener);
                        break;
                }
            } else { //if final anim, allow user to click again
                catAnimIV.setClickable(true);
                for (int i = 0; i < buttons.length; i++)
                    buttons[i].setClickable(true);
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }
}


