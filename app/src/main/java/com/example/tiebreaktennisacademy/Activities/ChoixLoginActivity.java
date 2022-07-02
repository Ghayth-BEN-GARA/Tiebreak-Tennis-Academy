package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.tiebreaktennisacademy.R;

public class ChoixLoginActivity extends AppCompatActivity {
    private AppCompatButton signin,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_login);

        signin = (AppCompatButton) findViewById(R.id.btn_signin);
        signup = (AppCompatButton) findViewById(R.id.btn_signup);

        onclickFunctions();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"You cannot return to the previous screen !",Toast.LENGTH_LONG).show();
    }

    public void ouvrirSignInActivity(){
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirSignUpActivity(){
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void onclickFunctions(){
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirSignInActivity();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirSignUpActivity();
            }
        });
    }
}