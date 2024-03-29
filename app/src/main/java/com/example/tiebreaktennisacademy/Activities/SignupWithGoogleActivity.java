package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupWithGoogleActivity extends AppCompatActivity {
    private TextInputLayout inputFullname, inputPhone, inputEmail, inputPassword, inputGender, inputNaissance, inputTaille, inputPoid;
    private TextInputEditText fullname, phone, email, password, naissance, gender, taille, poid;
    private TextView erreurFullname, erreurPhone, erreurEmail, erreurPassword, erreurNaissance, erreurGender, erreurTaille, erreurPoid;
    private AppCompatButton signup;
    private Boolean isFullname = true, isPhone = false, isEmail = false, isPassword = false, isNaissance = false, isGender = true, isTaille = false, isPoid = false;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_google);

        fullname = (TextInputEditText) findViewById(R.id.username);
        phone = (TextInputEditText) findViewById(R.id.phone);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        naissance = (TextInputEditText) findViewById(R.id.naissance);
        gender = (TextInputEditText) findViewById(R.id.gender);
        taille = (TextInputEditText) findViewById(R.id.taille);
        poid = (TextInputEditText) findViewById(R.id.poids);
        signup = (AppCompatButton) findViewById(R.id.signup_btn);
        erreurFullname = (TextView) findViewById(R.id.erreur_fullname);
        erreurPhone = (TextView) findViewById(R.id.erreur_phone);
        erreurEmail = (TextView) findViewById(R.id.erreur_email);
        erreurPassword = (TextView) findViewById(R.id.erreur_password);
        erreurNaissance = (TextView) findViewById(R.id.erreur_naissance);
        erreurGender = (TextView) findViewById(R.id.erreur_gender);
        erreurPoid = (TextView) findViewById(R.id.erreur_poid);
        erreurTaille = (TextView) findViewById(R.id.erreur_taille);
        inputFullname = (TextInputLayout) findViewById(R.id.inputlayout_username);
        inputEmail = (TextInputLayout) findViewById(R.id.inputlayout_email);
        inputPassword = (TextInputLayout) findViewById(R.id.inputlayout_password);
        inputGender = (TextInputLayout) findViewById(R.id.inputlayout_gender);
        inputNaissance = (TextInputLayout) findViewById(R.id.inputlayout_naissance);
        inputTaille = (TextInputLayout) findViewById(R.id.inputlayout_taille);
        inputPoid = (TextInputLayout) findViewById(R.id.inputlayout_poids);

        onclickFunctions();
        onChangeFunctions();
        intializeGoogleItems();
        getInformationsFromGoogle();
        initialiseDataBase();
    }

    @Override
    public void onBackPressed() {
        ouvrirSignup1Activity();
    }

    public void ouvrirSignup1Activity(){
        Intent intent = new Intent(getApplicationContext(), Signup1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void onclickFunctions(){
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormSignUpWithFacebook();
            }
        });
    }

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public boolean isLetter(String text) {
        Matcher matcher = Pattern.compile("^[a-z A-Z]*$").matcher(text);
        return matcher.matches();
    }

    public boolean isFormat(String text) {
        return (!TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches());
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

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhone();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    public void validateFullname(){
        if(isEmpty(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_required));
            setInputLayoutErrors(inputFullname,fullname);
            isFullname = false;
        }

        else if(!isLetter(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_letter));
            setInputLayoutErrors(inputFullname,fullname);
            isFullname = false;
        }

        else{
            setErreurNull(erreurFullname);
            setInputLayoutNormal(inputFullname,fullname);
            isFullname = true;
        }
    }

    public void validatePhone(){
        if(isEmpty(phone.getText().toString())){
            setErreurText(erreurPhone,getString(R.string.phone_required));
            setInputLayoutErrors(inputPhone, phone);
            isPhone = false;
        }

        else if(!isNumber(phone.getText().toString())){
            setErreurText(erreurPhone,getString(R.string.phone_number));
            setInputLayoutErrors(inputPhone, phone);
            isPhone = false;
        }

        else if(!isLength(phone.getText().toString())){
            setErreurText(erreurPhone,getString(R.string.phone_length));
            setInputLayoutErrors(inputPhone, phone);
            isPhone = false;
        }

        else{
            setErreurNull(erreurPhone);
            setInputLayoutNormal(inputPhone, phone);
            isPhone = true;
        }
    }


    public void validateEmail(){
        if(isEmpty(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_required));
            setInputLayoutErrors(inputEmail,email);
            isEmail = false;
        }

        else if(!isFormat(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_format_invalid));
            setInputLayoutErrors(inputEmail,email);
            isEmail = false;
        }

        else{
            setErreurNull(erreurEmail);
            setInputLayoutNormal(inputEmail,email);
            isEmail = true;
        }
    }

    public void validatePassword(){
        if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
            setInputLayoutErrors(inputPassword,password);
            isPassword = false;
        }

        else if(!isMinuscule(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_minisucle));
            setInputLayoutErrors(inputPassword,password);
            isPassword = false;
        }

        else if(!isMajuscule(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_majuscule));
            setInputLayoutErrors(inputPassword,password);
            isPassword = false;
        }

        else if(!isChiffre(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_number));
            setInputLayoutErrors(inputPassword,password);
            isPassword = false;
        }

        else if(!isLength(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_length));
            setInputLayoutErrors(inputPassword,password);
            isPassword = false;
        }

        else{
            setErreurNull(erreurPassword);
            setInputLayoutNormal(inputPassword,password);
            isPassword = true;
        }
    }

    public void validateGender(){
        if(isEmpty(gender.getText().toString())){
            setErreurText(erreurGender,getString(R.string.gender_required));
            setInputLayoutErrors(inputGender,gender);
            isGender = false;
        }

        else{
            setErreurNull(erreurGender);
            setInputLayoutNormal(inputGender,gender);
            isGender = true;
        }
    }

    public void validateNaissance(){
        if(isEmpty(naissance.getText().toString())){
            setErreurText(erreurNaissance,getString(R.string.naissance_required));
            setInputLayoutErrors(inputNaissance,naissance);
            isNaissance = false;
        }

        else{
            setErreurNull(erreurNaissance);
            setInputLayoutNormal(inputNaissance,naissance);
            isNaissance = true;
        }
    }

    public void validateSize(){
        if(isEmpty(taille.getText().toString())){
            setErreurText(erreurTaille,getString(R.string.taille_required));
            setInputLayoutErrors(inputTaille,taille);
            isTaille = false;
        }

        else if(!isNumber(taille.getText().toString())){
            setErreurText(erreurTaille,getString(R.string.taille_number));
            setInputLayoutErrors(inputTaille,taille);
            isTaille = false;
        }

        else{
            setErreurNull(erreurTaille);
            setInputLayoutNormal(inputTaille,taille);
            isTaille = true;
        }
    }

    public void validatePoid(){
        if(isEmpty(poid.getText().toString())){
            setErreurText(erreurPoid,getString(R.string.poid_required));
            setInputLayoutErrors(inputPoid,poid);
            isPoid = false;
        }

        else if(!isNumber(poid.getText().toString())){
            setErreurText(erreurPoid,getString(R.string.poid_number));
            setInputLayoutErrors(inputPoid,poid);
            isPoid = false;
        }

        else{
            setErreurNull(erreurPoid);
            setInputLayoutNormal(inputPoid,poid);
            isPoid = true;
        }
    }

    public void validateFormSignUpWithFacebook(){
        if(isEmpty(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_required));
            setInputLayoutErrors(inputFullname,fullname);
        }

        else if(isEmpty(phone.getText().toString())){
            setErreurText(erreurPhone,getString(R.string.phone_required));
            setInputLayoutErrors(inputPhone, phone);
        }

        else if(isEmpty(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_required));
            setInputLayoutErrors(inputEmail,email);
        }

        else if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
            setInputLayoutErrors(inputPassword,password);
        }

        else if(isEmpty(gender.getText().toString())){
            setErreurText(erreurGender,getString(R.string.gender_required));
            setInputLayoutErrors(inputGender,gender);
        }

        else if(isEmpty(naissance.getText().toString())){
            setErreurText(erreurNaissance,getString(R.string.naissance_required));
            setInputLayoutErrors(inputNaissance,naissance);
        }

        else if(isEmpty(taille.getText().toString())){
            setErreurText(erreurTaille,getString(R.string.taille_required));
            setInputLayoutErrors(inputTaille,taille);
        }

        else if(isEmpty(poid.getText().toString())){
            setErreurText(erreurPoid,getString(R.string.poid_required));
            setInputLayoutErrors(inputPoid,poid);
        }

        else if(isFullname == true && isPhone == true && isEmail == true && isPassword == true && isGender == true && isNaissance == true && isTaille == true && isPoid == true){
            setErreurNull(erreurFullname);
            setErreurNull(erreurPhone);
            setErreurNull(erreurEmail);
            setErreurNull(erreurPassword);
            setErreurNull(erreurNaissance);
            setErreurNull(erreurGender);
            setErreurNull(erreurTaille);
            setErreurNull(erreurPoid);
            setInputLayoutNormal(inputFullname,fullname);
            setInputLayoutNormal(inputPhone,phone);
            setInputLayoutNormal(inputEmail,email);
            setInputLayoutNormal(inputPassword,password);
            setInputLayoutNormal(inputGender,gender);
            setInputLayoutNormal(inputNaissance,naissance);
            setInputLayoutNormal(inputTaille,taille);
            setInputLayoutNormal(inputPoid,poid);
            chargementIfPhoneRegistred();
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

    public void intializeGoogleItems(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
    }

    public void getInformationsFromGoogle(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            fullname.setText(account.getDisplayName());
            email.setText(account.getEmail());
        }
    }

    public void logoutFromGoogle(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ouvrirHomeActivity();
            }
        });
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void chargementIfPhoneRegistred(){
        final ProgressDialog progressDialog = new ProgressDialog(SignupWithGoogleActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                checkIfPhoneRegistred(progressDialog);
            }
        }).start();
    }

    public void checkIfPhoneRegistred(ProgressDialog progressDialog){
        databaseReference.child("users").orderByChild("phone").equalTo(phone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null ){
                    progressDialog.dismiss();
                    setErreurText(erreurPhone,getString(R.string.phone_exist));
                }

                else{
                    setErreurNull(erreurPhone);
                    checkIfEmailRegistred(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkIfEmailRegistred(ProgressDialog progressDialog){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(encodeString(email.getText().toString()))){
                    progressDialog.dismiss();
                    setErreurText(erreurEmail,getString(R.string.email_exist));
                }

                else{
                    progressDialog.dismiss();
                    chargementUserRegistred();
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

    public void chargementUserRegistred(){
        final ProgressDialog progressDialog = new ProgressDialog(SignupWithGoogleActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.registration));
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                logoutFromGoogle();
                signUpUser();
            }
        },3000);
    }

    public void signUpUser(){
        databaseReference.child("users").child(encodeString(email.getText().toString())).child("fullname").setValue(fullname.getText().toString());
        databaseReference.child("users").child(encodeString(email.getText().toString())).child("email").setValue(encodeString(email.getText().toString()));
        databaseReference.child("users").child(encodeString(email.getText().toString())).child("password").setValue(hashPassword(password.getText().toString()));
        databaseReference.child("users").child(encodeString(email.getText().toString())).child("gender").setValue(gender.getText().toString());
        databaseReference.child("users").child(encodeString(email.getText().toString())).child("naissance").setValue(naissance.getText().toString());
        databaseReference.child("users").child(encodeString(email.getText().toString())).child("taille").setValue(taille.getText().toString());
        databaseReference.child("users").child(encodeString(email.getText().toString())).child("poid").setValue(poid.getText().toString());
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
        session.saveEmailApplication(decodeString(email.getText().toString()));
    }

    public void ouvrirHomeActivity(){
        createSession();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public static String decodeString(String string) {
        return string.replace(",", ".");
    }
}