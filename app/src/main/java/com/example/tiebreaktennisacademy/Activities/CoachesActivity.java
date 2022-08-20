package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import java.util.Calendar;

public class CoachesActivity extends AppCompatActivity {
    private ImageView back;
    private TextView copiright, jerridInormations, jerridDescriptions, kieronInformations, kieronDescriptions, malInformations, malDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        jerridInormations = (TextView) findViewById(R.id.title_desc_jarred);
        jerridDescriptions = (TextView) findViewById(R.id.desc_jarred);
        kieronInformations = (TextView) findViewById(R.id.title_desc_kieran);
        kieronDescriptions = (TextView) findViewById(R.id.desc_kieran);
        malInformations = (TextView) findViewById(R.id.title_desc_mal);
        malDescriptions = (TextView) findViewById(R.id.desc_mal);

        onclickFunctions();
        setCopyrightText();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirHomeActivity();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
            }
        });

        jerridInormations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationsCoach(jerridDescriptions, R.id.jarred);
            }
        });

        kieronInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationsCoach(kieronDescriptions, R.id.kieran);
            }
        });

        malInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationsCoach(malDescriptions, R.id.mal);
            }
        });
    }

    public void ouvrirHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public void showInformationsCoach(TextView text, int id){
        if(text.getVisibility() == View.INVISIBLE){
            text.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.BELOW, id);
            text.setLayoutParams(lp);
        }

        else{
            text.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0);
            text.setLayoutParams(lp);
        }
    }
}