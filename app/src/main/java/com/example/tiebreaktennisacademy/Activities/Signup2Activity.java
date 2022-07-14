package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup2Activity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton next;
    private TextInputLayout textInputEmail, textInputPassword, textInputRepeatPassword;
    private TextView erreurEmail, erreurPassword, erreurRepeatPassword;
    private TextInputEditText email, password, repeatPassword;
    private Boolean isEmail = false, isPassword = false, isRepeatPassword = false;
    private DatabaseReference databaseReference;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);
        erreurEmail = (TextView) findViewById(R.id.erreur_email);
        erreurPassword = (TextView) findViewById(R.id.erreur_password);
        erreurRepeatPassword = (TextView) findViewById(R.id.erreur_repeat_password);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        repeatPassword = (TextInputEditText) findViewById(R.id.repeat_password);
        textInputEmail = (TextInputLayout) findViewById(R.id.inputlayout_email);
        textInputPassword = (TextInputLayout) findViewById(R.id.inputlayout_password);
        textInputRepeatPassword = (TextInputLayout) findViewById(R.id.inputlayout_repeat_password);

        onclickFunctions();
        onChangeFunctions();
        initialiseDataBase();
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
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("password", password.getText().toString());
        intent.putExtra("fullname", getIntent().getStringExtra("fullname"));
        intent.putExtra("phone", getIntent().getStringExtra("phone"));
        intent.putExtra("gender", getIntent().getStringExtra("gender"));
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
            setInputLayoutErrors(textInputEmail,email);
        }

        else if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
            setInputLayoutErrors(textInputPassword,password);
        }

        else if(isEmpty(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_required));
            setInputLayoutErrors(textInputRepeatPassword,repeatPassword);
        }

        else if(!isEquals(password.getText().toString(),repeatPassword.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_not_equals));
            setErreurText(erreurRepeatPassword,getString(R.string.password_not_equals));
            setInputLayoutErrors(textInputPassword,password);
            setInputLayoutErrors(textInputRepeatPassword,repeatPassword);
        }

        else if(isEmail == true && isPassword == true && isRepeatPassword == true){
            setErreurNull(erreurEmail);
            setErreurNull(erreurPassword);
            setErreurNull(erreurRepeatPassword);
            setInputLayoutNormal(textInputEmail,email);
            setInputLayoutNormal(textInputPassword,password);
            setInputLayoutNormal(textInputRepeatPassword,repeatPassword);
            chargementIfEmailRegistred();
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
            setInputLayoutErrors(textInputEmail,email);
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

    public void validatePassword(){
        if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
            setInputLayoutErrors(textInputPassword,password);
            isPassword = false;
        }

        else if(!isMinuscule(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_minisucle));
            setInputLayoutErrors(textInputPassword,password);
            isPassword = false;
        }

        else if(!isMajuscule(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_majuscule));
            setInputLayoutErrors(textInputPassword,password);
            isPassword = false;
        }

        else if(!isChiffre(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_number));
            setInputLayoutErrors(textInputPassword,password);
            isPassword = false;
        }

        else if(!isLength(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_length));
            setInputLayoutErrors(textInputPassword,password);
            isPassword = false;
        }

        else{
            setErreurNull(erreurPassword);
            setInputLayoutNormal(textInputPassword,password);
            isPassword = true;
        }
    }

    public void validateRepeatPassword(){
        if(isEmpty(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_required));
            setInputLayoutErrors(textInputRepeatPassword,repeatPassword);
            isRepeatPassword = false;
        }

        else if(!isMinuscule(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_minisucle));
            setInputLayoutErrors(textInputRepeatPassword,repeatPassword);
            isRepeatPassword = false;
        }

        else if(!isMajuscule(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_majuscule));
            setInputLayoutErrors(textInputRepeatPassword,repeatPassword);
            isRepeatPassword = false;
        }

        else if(!isChiffre(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_number));
            setInputLayoutErrors(textInputRepeatPassword,repeatPassword);
            isRepeatPassword = false;
        }

        else if(!isLength(repeatPassword.getText().toString())){
            setErreurText(erreurRepeatPassword,getString(R.string.password_length));
            setInputLayoutErrors(textInputRepeatPassword,repeatPassword);
            isRepeatPassword = false;
        }

        else{
            setErreurNull(erreurRepeatPassword);
            setInputLayoutErrors(textInputRepeatPassword,repeatPassword);
            isRepeatPassword = true;
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

    public void setInputLayoutNormal(TextInputLayout input, TextInputEditText text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            input.setBackground(getDrawable(R.drawable.edi_text_background));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                text.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.black)));
            }
        }
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void chargementIfEmailRegistred(){
        final ProgressDialog progressDialog = new ProgressDialog(Signup2Activity.this, R.style.chargement);
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
                if(snapshot.hasChild(encodeString(email.getText().toString()))){
                    showNotificationError();
                    progressDialog.dismiss();
                }

                else{
                    ouvrirSignup3Activity();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showNotificationError(){
        dialog = new Dialog(Signup2Activity.this);
        dialog.setContentView(R.layout.item_erreur);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        AppCompatButton cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView desc = dialog.findViewById(R.id.desc_title_erreur);
        desc.setText(R.string.email_exist);

        dialog.show();
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }
}