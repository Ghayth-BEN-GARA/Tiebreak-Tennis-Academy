package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.tiebreaktennisacademy.R;

public class HelpActivity extends AppCompatActivity {
    private ImageView back;
    private Animation animationBottom;
    private LinearLayout linearLayout1,linearLayout2,linearLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        back = (ImageView) findViewById(R.id.back);
        linearLayout1 = (LinearLayout) findViewById(R.id.linear_layout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linear_layout2);
        linearLayout3 = (LinearLayout) findViewById(R.id.linear_layout3);

        onclickFunctions();
        setAnimationsWithTimeFunctions();
    }

    @Override
    public void onBackPressed() {
        ouvrirSignInActivity();
    }

    public void ouvrirSignInActivity(){
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirSignInActivity();
            }
        });
    }

    public void setAnimation (LinearLayout linearLayout){
        animationBottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_top2);
        linearLayout.setAnimation(animationBottom);
    }

    public void setAnimationsWithTimeFunctions(){
        setAnimation(linearLayout1);
        setAnimation(linearLayout2);
        setAnimation(linearLayout3);
    }
}