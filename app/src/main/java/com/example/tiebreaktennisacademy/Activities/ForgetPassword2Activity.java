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

public class ForgetPassword2Activity extends AppCompatActivity {

    private ImageView back;
    private AppCompatButton next;
    private TextInputEditText code1, code2, code3, code4;
    private ScrollView scrollView;
    private TextView erreurCode;
    private Boolean isCode1 = false, isCode2 = false, isCode3 = false, isCode4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password2);

        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);
        code1 = (TextInputEditText) findViewById(R.id.code1);
        code2 = (TextInputEditText) findViewById(R.id.code2);
        code3 = (TextInputEditText) findViewById(R.id.code3);
        code4 = (TextInputEditText) findViewById(R.id.code4);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        erreurCode = (TextView) findViewById(R.id.erreur_code_securite);

        onclickFunctions();
        onChangeFunctions();
        onFocusFunctions();
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
                validateFormForgetPassword2();
            }
        });
    }

    public void validateFormForgetPassword2(){
        if(isEmpty(code1.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isEmpty(code2.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isEmpty(code3.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isEmpty(code4.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
        }

        else if(isCode1 == true && isCode2 == true && isCode3 == true && isCode4 == true){
            setErreurNull(erreurCode);
            ouvrirForgetPassword3Activity();
            //testEgaliteCode
            //showSuccessnotification
        }
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

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public boolean isNumber(String text) {
        Matcher matcher = Pattern.compile("^[0-9]*$").matcher(text);
        return matcher.matches();
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public void onChangeFunctions(){
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode1();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isEmpty(code1.getText().toString())){
                    swipeToOtherInput(code2);
                }
            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode2();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isEmpty(code2.getText().toString())){
                    swipeToOtherInput(code3);
                }
            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode3();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isEmpty(code3.getText().toString())){
                    swipeToOtherInput(code4);
                }
            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCode4();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateCode1(){
        if(isEmpty(code1.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode1 = false;
        }

        else if(!isNumber(code1.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode1 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode1 = true;
        }
    }

    public void validateCode2(){
        if(isEmpty(code2.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode2 = false;
        }

        else if(!isNumber(code2.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode2 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode2 = true;
        }
    }

    public void validateCode3(){
        if(isEmpty(code3.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode3 = false;
        }

        else if(!isNumber(code3.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode3 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode3 = true;
        }
    }

    public void validateCode4(){
        if(isEmpty(code4.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_required));
            isCode4 = false;
        }

        else if(!isNumber(code4.getText().toString())){
            setErreurText(erreurCode,getString(R.string.code_number));
            isCode4 = false;
        }

        else{
            setErreurNull(erreurCode);
            isCode4 = true;
        }
    }

    public void onFocusFunctions(){
        code1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToTop();
            }
        });

        code2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToTop();
            }
        });

        code3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToTop();
            }
        });

        code4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    public void swipeToOtherInput(TextInputEditText text){
        text.requestFocus();
    }
}