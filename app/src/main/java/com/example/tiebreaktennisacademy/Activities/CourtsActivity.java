package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import com.squareup.picasso.Picasso;
import java.util.Calendar;

public class CourtsActivity extends AppCompatActivity {
    private ImageView back, court1, court2, court3;
    private TextView copiright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courts);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        court1 = (ImageView) findViewById(R.id.court1);
        court2 = (ImageView) findViewById(R.id.court2);
        court3 = (ImageView) findViewById(R.id.court3);

        onclickFunctions();
        setCopyrightText();
        setCourtesImages();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirHomeActivity();
    }

    public void onclickFunctions() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
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

    public void setCourtesImages(){
        Picasso.with(getApplicationContext()).load(R.drawable.court1).fit().into(court1);
        Picasso.with(getApplicationContext()).load(R.drawable.court2).fit().into(court2);
        Picasso.with(getApplicationContext()).load(R.drawable.court3).fit().into(court3);
    }

}