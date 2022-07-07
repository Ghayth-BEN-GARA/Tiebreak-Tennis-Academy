package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPassword3Activity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton get;
    private ScrollView scrollView;
    private TextView erreurPassword, erreurRepeatPassword;
    private TextInputEditText password, repeatPassword;
    private Boolean isPassword = false, isRepeatPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password3);

        back = (ImageView) findViewById(R.id.back);
        get = (AppCompatButton) findViewById(R.id.get_account_btn);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        erreurPassword = (TextView) findViewById(R.id.erreur_password);
        erreurRepeatPassword = (TextView) findViewById(R.id.erreur_repeat_password);
        password = (TextInputEditText) findViewById(R.id.password);
        repeatPassword = (TextInputEditText) findViewById(R.id.repeat_password);

        onClickFunctions();
        onChangeFunctions();
        onFocusFunctions();
    }

    @Override
    public void onBackPressed() {
        ouvrirForgetPassword1Activity();
    }

    public void ouvrirForgetPassword1Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirForgetPassword4Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword4Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void onClickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirForgetPassword1Activity();
            }
        });

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormForgetPassword3();
            }
        });
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

    public void validateFormForgetPassword3(){
        if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
        }

        else if(isEmpty(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_required));
        }

        else if(!isEquals(password.getText().toString(),repeatPassword.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_not_equals));
            setErreurText(erreurRepeatPassword,getString(R.string.password_not_equals));
        }

        else if(isPassword == true && isRepeatPassword == true){
            setErreurNull(erreurPassword);
            setErreurNull(erreurRepeatPassword);
            ouvrirForgetPassword4Activity();
            //updatepassword
        }
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public void onChangeFunctions(){
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