package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup3Activity extends AppCompatActivity {
    private ImageView back;
    private TextInputEditText naissance, taille, poid;
    private DatePickerDialog.OnDateSetListener date;
    private AppCompatButton signUp;
    private TextInputLayout textInputNaissance, textInputTaille, textInputPoid;
    private TextView erreurNaissance, erreurTaille, erreurPoid;
    private Boolean isNaissance = false, isTaille = false, isPoid = false;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        back = (ImageView) findViewById(R.id.back);
        naissance = (TextInputEditText) findViewById(R.id.naissance);
        taille = (TextInputEditText) findViewById(R.id.taille);
        poid = (TextInputEditText) findViewById(R.id.poids);
        signUp = (AppCompatButton) findViewById(R.id.signup_btn);
        textInputNaissance = (TextInputLayout) findViewById(R.id.inputlayout_naissance);
        textInputPoid = (TextInputLayout) findViewById(R.id.inputlayout_poids);
        textInputTaille = (TextInputLayout) findViewById(R.id.inputlayout_taille);
        erreurNaissance = (TextView) findViewById(R.id.erreur_naissance);
        erreurTaille = (TextView) findViewById(R.id.erreur_taille);
        erreurPoid = (TextView) findViewById(R.id.erreur_poid);

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

        naissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurerCalandrier();
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                actionSurCalandrier(year, month, dayOfMonth);
            }
        };

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormSignUp3();
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

    public  void configurerCalandrier(){
        Calendar agenda = Calendar.getInstance();
        int year = agenda.get(Calendar.YEAR);
        int month = agenda.get(Calendar.MONTH);
        int day = agenda.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(Signup3Activity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, date, year , month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void actionSurCalandrier(int year, int month, int day){
        month = month + 1;
        String chaine = year + "-" + month + "-" + day;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(chaine);
            naissance.setText(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void ouvrirSignup4Activity(String email){
        Intent intent = new Intent(getApplicationContext(), Signup4Activity.class);
        intent.putExtra("email",email);
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

    public void validateFormSignUp3(){
        if(isEmpty(naissance.getText().toString())){
            setErreurText(erreurNaissance,getString(R.string.naissance_required));
            setInputLayoutErrors(textInputNaissance,naissance);
        }

        else if(isEmpty(taille.getText().toString())){
            setErreurText(erreurTaille,getString(R.string.taille_required));
            setInputLayoutErrors(textInputTaille,taille);
        }

        else if(isEmpty(poid.getText().toString())){
            setErreurText(erreurPoid,getString(R.string.poid_required));
            setInputLayoutErrors(textInputPoid,poid);
        }

        else if(isNaissance == true && isTaille == true && isPoid == true){
            setErreurNull(erreurNaissance);
            setErreurNull(erreurTaille);
            setErreurNull(erreurPoid);
            setInputLayoutNormal(textInputNaissance,naissance);
            setInputLayoutNormal(textInputTaille,taille);
            setInputLayoutNormal(textInputPoid,poid);
            chargementSignUp();
        }
    }

    public void validateNaissance(){
        if(isEmpty(naissance.getText().toString())){
            setErreurText(erreurNaissance,getString(R.string.naissance_required));
            setInputLayoutErrors(textInputNaissance,naissance);
            isNaissance = false;
        }

        else{
            setErreurNull(erreurNaissance);
            setInputLayoutNormal(textInputNaissance,naissance);
            isNaissance = true;
        }
    }

    public void validateSize(){
        if(isEmpty(taille.getText().toString())){
            setErreurText(erreurTaille,getString(R.string.taille_required));
            setInputLayoutErrors(textInputTaille,taille);
            isTaille = false;
        }

        else if(!isNumber(taille.getText().toString())){
            setErreurText(erreurTaille,getString(R.string.taille_number));
            setInputLayoutErrors(textInputTaille,taille);
            isTaille = false;
        }

        else{
            setErreurNull(erreurTaille);
            setInputLayoutNormal(textInputTaille,taille);
            isTaille = true;
        }
    }

    public void validatePoid(){
        if(isEmpty(poid.getText().toString())){
            setErreurText(erreurPoid,getString(R.string.poid_required));
            setInputLayoutErrors(textInputPoid,poid);
            isPoid = false;
        }

        else if(!isNumber(poid.getText().toString())){
            setErreurText(erreurPoid,getString(R.string.poid_number));
            setInputLayoutErrors(textInputPoid,poid);
            isPoid = false;
        }

        else{
            setErreurNull(erreurPoid);
            setInputLayoutNormal(textInputPoid,poid);
            isPoid = true;
        }
    }

    public void onChangeFunctions(){
        naissance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateNaissance();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        taille.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateSize();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        poid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePoid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    public void chargementSignUp(){
        final ProgressDialog progressDialog = new ProgressDialog(Signup3Activity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.registration));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                signUpUser(progressDialog);
            }
        }).start();
    }

    public void signUpUser(ProgressDialog progressDialog){
        String fullnameInput = getIntent().getStringExtra("fullname");
        String phoneInput = getIntent().getStringExtra("phone");
        String emailInput = getIntent().getStringExtra("email");
        String passwordInput = getIntent().getStringExtra("password");
        String genderInput = getIntent().getStringExtra("gender");
        String naissanceInput = naissance.getText().toString();
        String tailleInput = taille.getText().toString();
        String poidInput = poid.getText().toString();

        databaseReference.child("users").child(encodeString(emailInput)).child("fullname").setValue(fullnameInput);
        databaseReference.child("users").child(encodeString(emailInput)).child("phone").setValue(phoneInput);
        databaseReference.child("users").child(encodeString(emailInput)).child("email").setValue(encodeString(emailInput));
        databaseReference.child("users").child(encodeString(emailInput)).child("password").setValue(hashPassword(passwordInput));
        databaseReference.child("users").child(encodeString(emailInput)).child("gender").setValue(genderInput);
        databaseReference.child("users").child(encodeString(emailInput)).child("naissance").setValue(naissanceInput);
        databaseReference.child("users").child(encodeString(emailInput)).child("taille").setValue(tailleInput);
        databaseReference.child("users").child(encodeString(emailInput)).child("poid").setValue(poidInput);

        checkIfEmailRegistred(progressDialog);
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void checkIfEmailRegistred(ProgressDialog dialog){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(encodeString(getIntent().getStringExtra("email")))){
                    ouvrirSignup4Activity(getIntent().getStringExtra("email"));
                    dialog.dismiss();
                }

                else{
                    Toast.makeText(getApplicationContext(),getString(R.string.error_signup), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}