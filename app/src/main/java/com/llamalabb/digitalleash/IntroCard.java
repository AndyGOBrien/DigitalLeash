package com.llamalabb.digitalleash;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


/**
 * Created by andy on 9/26/17.
 */

public class IntroCard extends CardView {
    private int direction;
    private String msg;
    private TextView textView;


    public IntroCard(Context context) {
        super(context);
        initialize(context);
    }

    public IntroCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public IntroCard(Context context,int direction, String msg) {
        super(context);
        this.msg = msg;
        this.direction = direction;
        initialize(context);
    }

    private void initialize(Context context){

        Log.d("MESSAGE CARD", msg);

        Animation animSlideRight = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left);
        Animation animSlideLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right);

        if(direction == ItemTouchHelper.LEFT)
            LayoutInflater.from(context).inflate(R.layout.intro_card, this).startAnimation(animSlideLeft);
        else
            LayoutInflater.from(context).inflate(R.layout.intro_card, this).startAnimation(animSlideRight);

        textView = (TextView) findViewById(R.id.intro_text);
        textView.setText(msg);

    }


}
