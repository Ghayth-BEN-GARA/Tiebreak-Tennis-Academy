package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword1Activity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton next;
    private TextView erreurEmail;
    private TextInputEditText email;
    private TextInputLayout textInputEmail;
    private Boolean isEmail = false;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password1);

        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);
        email = (TextInputEditText) findViewById(R.id.email);
        erreurEmail = (TextView) findViewById(R.id.erreur_email);
        textInputEmail = (TextInputLayout) findViewById(R.id.inputlayout_email);

        onclickFunctions();
        onChangeFunctions();
        initialiseDataBase();
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
            setInputLayoutErrors(textInputEmail, email);
        }

        else if(isEmail == true){
            setErreurNull(erreurEmail);
            setInputLayoutNormal(textInputEmail,email);
            chargementIfEmailRegistred();
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
            setInputLayoutErrors(textInputEmail, email);
            isEmail = false;
        }

        else if(!isFormat(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_format_invalid));
            setInputLayoutErrors(textInputEmail,email);
            isEmail = false;
        }

        else{
            setErreurNull(erreurEmail);
            setInputLayoutNormal(textInputEmail,email);
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

    public void setInputLayoutNormal(TextInputLayout input, TextInputEditText text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            input.setBackground(getDrawable(R.drawable.edi_text_background));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                text.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.black)));
            }
        }
    }

    public void setInputLayoutErrors(TextInputLayout input, TextInputEditText text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            input.setBackground(getDrawable(R.drawable.edit_text_background_erreur));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                text.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(com.google.android.material.R.color.design_default_color_error)));
            }
        }
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void chargementIfEmailRegistred(){
        final ProgressDialog progressDialog = new ProgressDialog(ForgetPassword1Activity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                checkIfEmailRegistred(progressDialog);
            }
        }).start();
    }

    public void checkIfEmailRegistred(ProgressDialog progressDialog){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(encodeString(email.getText().toString()))){
                    setErreurText(erreurEmail,getString(R.string.no_account_found));
                    progressDialog.dismiss();
                }

                else{
                    sendLinkResetPassword(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void sendLinkResetPassword(ProgressDialog progressDialog){

    }
}