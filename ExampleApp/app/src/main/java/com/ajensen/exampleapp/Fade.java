package com.ajensen.exampleapp;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.BoringLayout;
import android.view.View;
import android.widget.ImageView;

public class Fade extends AppCompatActivity {

    ImageView catFadeIV;
    Boolean running;
    Boolean next;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fade);
        running = false;

        catFadeIV = (ImageView) findViewById(R.id.catFadeIV);
        catFadeIV.setTag("R.drawable.cat");
        catFadeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!running) {
                    running = true;
                    next = false;
                    view.animate().alpha(0).setDuration(2000).setListener(new animateListener());
                    //2 seconds; duration is in milliseconds
                }
            }
        });
    }

    class animateListener implements Animator.AnimatorListener {
        @Override public void onAnimationStart(Animator animator) { }
        @Override
        public void onAnimationEnd(Animator animator) {
            if (catFadeIV.getTag() == "R.drawable.cat")
            {
                if (!next) {
                    catFadeIV.setImageResource(R.drawable.cat2);
                    catFadeIV.setTag("R.drawable.cat2");
                    next = true;
                    catFadeIV.animate().alpha(1).setDuration(2000).setListener(new animateListener());
                } else
                    running = false;
            } else {
                if (!next) {
                    catFadeIV.setImageResource(R.drawable.cat);
                    catFadeIV.setTag("R.drawable.cat");
                    next = true;
                    catFadeIV.animate().alpha(1).setDuration(2000).setListener(new animateListener());
                } else
                    running = false;
            }
        }
        @Override public void onAnimationCancel(Animator animator) { }
        @Override public void onAnimationRepeat(Animator animator) { }
    }
}


