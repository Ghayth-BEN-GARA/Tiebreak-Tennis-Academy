package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPassword3Activity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton get;
    private TextView erreurPassword, erreurRepeatPassword;
    private TextInputEditText password, repeatPassword;
    private TextInputLayout inputLayoutPassword, inputLayoutRepeatPassword;
    private DatabaseReference databaseReference;
    private Boolean isPassword = false, isRepeatPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password3);

        back = (ImageView) findViewById(R.id.back);
        get = (AppCompatButton) findViewById(R.id.get_account_btn);
        erreurPassword = (TextView) findViewById(R.id.erreur_password);
        erreurRepeatPassword = (TextView) findViewById(R.id.erreur_repeat_password);
        password = (TextInputEditText) findViewById(R.id.password);
        repeatPassword = (TextInputEditText) findViewById(R.id.repeat_password);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputlayout_password);
        inputLayoutRepeatPassword = (TextInputLayout) findViewById(R.id.inputlayout_repeat_password);

        onClickFunctions();
        onChangeFunctions();
        initialiseDataBase();
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
            chargementUpdatePassword();
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

    public void chargementUpdatePassword(){
        final ProgressDialog progressDialog = new ProgressDialog(ForgetPassword3Activity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.getting_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                updatePassword(progressDialog);
            }
        }).start();
    }

    public void updatePassword(ProgressDialog progressDialog){
        if(isPasswordChanged()){
            createSession();
            ouvrirForgetPassword4Activity();
            progressDialog.dismiss();
        }
    }

    public boolean isPasswordChanged(){
        String passwordHashed = hashPassword(password.getText().toString());

        databaseReference.child("users").child(encodeString(getIntent().getStringExtra("email"))).child("password").setValue(passwordHashed);
        return true;
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String hashPassword(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void createSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        session.saveEmailApplication(decodeString(getIntent().getStringExtra("email")));
    }

    public static String decodeString(String string) {
        return string.replace(",", ".");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }
}