package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.tiebreaktennisacademy.R;

public class ForgetPassword2Activity extends AppCompatActivity {

    private ImageView back;
    private AppCompatButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password2);

        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);

        onclickFunctions();
    }

    @Override
    public void onBackPressed() {
        ouvrirForgetPassword1Activity();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirForgetPassword1Activity();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirForgetPassword3Activity();
            }
        });
    }

    public void ouvrirForgetPassword1Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirForgetPassword3Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}