package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;

public class Signup4Activity extends AppCompatActivity {

    private ImageView back;
    private AppCompatButton getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        back = (ImageView) findViewById(R.id.back);
        getStarted = (AppCompatButton) findViewById(R.id.create_account_btn);

        onclickFunctions();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),R.string.not_available,Toast.LENGTH_LONG).show();
            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),R.string.not_return,Toast.LENGTH_LONG).show();
    }

    public void createSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        session.saveEmailApplication(decodeString(getIntent().getStringExtra("email")));
    }

    public void ouvrirHomeActivity(){
        createSession();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public static String decodeString(String string) {
        return string.replace(",", ".");
    }
}