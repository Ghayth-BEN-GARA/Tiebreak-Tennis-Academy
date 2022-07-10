package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiebreaktennisacademy.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup1Activity extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter;
    private AutoCompleteTextView gender;
    private ImageView back, facebook;
    private AppCompatButton next;
    private TextView erreurFullname,erreurGender;
    private ScrollView scrollView;
    private TextInputEditText fullname;
    private String[] genderItems;
    private Boolean isFullname = false, isGender = true;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup1);

        gender = (AutoCompleteTextView) findViewById(R.id.gender);
        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);
        fullname = (TextInputEditText) findViewById(R.id.username);
        erreurFullname = (TextView) findViewById(R.id.erreur_fullname);
        erreurGender = (TextView) findViewById(R.id.erreur_gender);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        facebook = (ImageView) findViewById(R.id.facebook);

        intializeFacebookItems();
        loginManagerActions();
        setGenderItems();
        onclickFunctions();
        onChangeFunctions();
        onFocusFunctions();
    }

    public void setGenderItems(){
        genderItems = getResources().getStringArray(R.array.gender_items);
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.items_gender,genderItems);
        gender.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        ouvrirChoixLoginActivity();
    }

    public void ouvrirChoixLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), ChoixLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
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
                validateFormSignUp1();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpWithFacebook();
            }
        });
    }

    public void ouvrirSignup2Activity(){
        Intent intent = new Intent(getApplicationContext(), Signup2Activity.class);
        intent.putExtra("fullname", fullname.getText().toString());
        intent.putExtra("gender", gender.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public boolean isLetter(String text) {
        Matcher matcher = Pattern.compile("^[a-z A-Z]*$").matcher(text);
        return matcher.matches();
    }

    public void validateFormSignUp1(){
        if(isEmpty(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_required));
        }

        else if(isEmpty(gender.getText().toString())){
            setErreurText(erreurGender,getString(R.string.gender_required));
        }

        else if(isFullname == true && isGender == true){
            setErreurNull(erreurFullname);
            setErreurNull(erreurFullname);
            ouvrirSignup2Activity();
        }
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public void onChangeFunctions(){
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFullname();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateGender();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateFullname(){
        if(isEmpty(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_required));
            isFullname = false;
        }

        else if(!isLetter(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_letter));
            isFullname = false;
        }

        else{
            setErreurNull(erreurFullname);
            isFullname = true;
        }
    }

    public void validateGender(){
        if(isEmpty(gender.getText().toString())){
            setErreurText(erreurGender,getString(R.string.gender_required));
            isGender = false;
        }

        else{
            setErreurNull(erreurGender);
            isGender = true;
        }
    }

    public void onFocusFunctions(){
        fullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    public void intializeFacebookItems(){
        callbackManager = CallbackManager.Factory.create();
    }

    public void loginManagerActions(){
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        ouvrirSignupWithFacebookActivity();
                    }

                    @Override
                    public void onCancel() {
                        // add notification cancel
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // add notification errors
                    }
                });
    }

    public void signUpWithFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email","user_friends","user_birthday"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ouvrirSignupWithFacebookActivity(){
        Intent intent = new Intent(getApplicationContext(), SignupWithFacebookActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}