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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup2Activity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton next;
    private ScrollView scrollView;
    private TextView erreurEmail, erreurPassword, erreurRepeatPassword;
    private TextInputEditText email, password, repeatPassword;
    private Boolean isEmail = false, isPassword = false, isRepeatPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        erreurEmail = (TextView) findViewById(R.id.erreur_email);
        erreurPassword = (TextView) findViewById(R.id.erreur_password);
        erreurRepeatPassword = (TextView) findViewById(R.id.erreur_repeat_password);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        repeatPassword = (TextInputEditText) findViewById(R.id.repeat_password);

        onclickFunctions();
        onChangeFunctions();
        onFocusFunctions();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirSignup1Activity();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormSignUp2();
            }
        });
    }

    public void ouvrirSignup1Activity(){
        Intent intent = new Intent(getApplicationContext(), Signup1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        ouvrirSignup1Activity();
    }

    public void ouvrirSignup3Activity(){
        Intent intent = new Intent(getApplicationContext(), Signup3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public boolean isFormat(String text) {
        return (!TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches());
    }

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public boolean isMinuscule(String text) {
        Matcher matcher = Pattern.compile("((?=.*[a-z]).{1,100})").matcher(text);
        return matcher.matches();
    }

    public boolean isMajuscule(String text) {
        Matcher matcher = Pattern.compile("((?=.*[A-Z]).{1,100})").matcher(text);
        return matcher.matches();
    }

    public boolean isChiffre(String text) {
        Matcher matcher = Pattern.compile("((?=.*[0-9]).{1,100})").matcher(text);
        return matcher.matches();
    }

    public boolean isLength(String text){
        return text.length() >= 5;
    }

    public boolean isEquals(String text1, String text2){
        return text1.equals(text2);
    }

    public void validateFormSignUp2(){
        if(isEmpty(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_required));
        }

        else if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
        }

        else if(isEmpty(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_required));
        }

        else if(!isEquals(password.getText().toString(),repeatPassword.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_not_equals));
            setErreurText(erreurRepeatPassword,getString(R.string.password_not_equals));
        }

        else if(isEmail == true && isPassword == true && isRepeatPassword == true){
            setErreurNull(erreurEmail);
            setErreurNull(erreurPassword);
            setErreurNull(erreurRepeatPassword);
            ouvrirSignup3Activity();
            //sendData
        }
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
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

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        repeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateRepeatPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    public void validatePassword(){
        if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
            isPassword = false;
        }

        else if(!isMinuscule(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_minisucle));
            isPassword = false;
        }

        else if(!isMajuscule(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_majuscule));
            isPassword = false;
        }

        else if(!isChiffre(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_number));
            isPassword = false;
        }

        else if(!isLength(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_length));
            isPassword = false;
        }

        else{
            setErreurNull(erreurPassword);
            isPassword = true;
        }
    }

    public void validateRepeatPassword(){
        if(isEmpty(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_required));
            isRepeatPassword = false;
        }

        else if(!isMinuscule(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_minisucle));
            isRepeatPassword = false;
        }

        else if(!isMajuscule(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_majuscule));
            isRepeatPassword = false;
        }

        else if(!isChiffre(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_number));
            isRepeatPassword = false;
        }

        else if(!isLength(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_length));
            isRepeatPassword = false;
        }

        else{
            setErreurNull(erreurRepeatPassword);
            isRepeatPassword = true;
        }
    }

    public void onFocusFunctions(){
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToTop();
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToTop();
            }
        });

        repeatPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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