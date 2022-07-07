package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;

public class ForgetPassword1Activity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton next;
    private TextView erreurEmail;
    private ScrollView scrollView;
    private TextInputEditText email;
    private Boolean isEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password1);

        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);
        email = (TextInputEditText) findViewById(R.id.email);
        erreurEmail = (TextView) findViewById(R.id.erreur_email);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        onclickFunctions();
        onChangeFunctions();
        onFocusFunctions();
    }

    @Override
    public void onBackPressed() {
        ouvrirChoixLoginActivity();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirChoixLoginActivity();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormForgetPassword1();
            }
        });
    }

    public void validateFormForgetPassword1(){
        if(isEmpty(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_required));
        }

        else if(isEmail == true){
            setErreurNull(erreurEmail);
            ouvrirForgetPassword2Activity();
            //sendEmail
        }
    }

    public void ouvrirChoixLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), ChoixLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirForgetPassword2Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public boolean isFormat(String text) {
        return (!TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches());
    }

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public void validateEmail(){
        if(isEmpty(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_required));
            isEmail = false;
        }

        else if(!isFormat(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_format_invalid));
            isEmail = false;
        }

        else{
            setErreurNull(erreurEmail);
            isEmail = true;
        }
    }

    public void onChangeFunctions(){
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onFocusFunctions(){
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToTop();
            }
        });
    }

    public void scrollToTop(){
        final Handler handler;
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                scrollView.smoothScrollTo(0, 500);
                handler.postDelayed(this, 200);
            }
        };
        handler.postDelayed(r, 200);
    }
}