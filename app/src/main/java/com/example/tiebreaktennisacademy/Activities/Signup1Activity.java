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
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup1Activity extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter;
    private AutoCompleteTextView gender;
    private ImageView back, facebook, google;
    private AppCompatButton next;
    private TextView help, erreurFullname, erreurPhone, erreurGender;
    private TextInputLayout inputFullname, inputPhone, inputGender;
    private TextInputEditText fullname, phone;
    private Dialog dialog;
    private String[] genderItems;
    private Boolean isFullname = false, isPhone = false, isGender = false;
    private int signUpGoogle = 1000;
    private CallbackManager callbackManager;
    private DatabaseReference databaseReference;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup1);

        gender = (AutoCompleteTextView) findViewById(R.id.gender);
        back = (ImageView) findViewById(R.id.back);
        next = (AppCompatButton) findViewById(R.id.next_btn);
        fullname = (TextInputEditText) findViewById(R.id.username);
        phone = (TextInputEditText) findViewById(R.id.phone);
        erreurFullname = (TextView) findViewById(R.id.erreur_fullname);
        erreurGender = (TextView) findViewById(R.id.erreur_gender);
        erreurPhone = (TextView) findViewById(R.id.erreur_phone);
        help = (TextView) findViewById(R.id.help);
        inputFullname = (TextInputLayout) findViewById(R.id.inputlayout_username);
        inputPhone = (TextInputLayout) findViewById(R.id.inputlayout_phone);
        inputGender = (TextInputLayout) findViewById(R.id.inputlayout_gender);
        facebook = (ImageView) findViewById(R.id.facebook);
        google = (ImageView) findViewById(R.id.google);

        intializeFacebookItems();
        loginManagerActions();
        intializeGoogleItems();
        setGenderItems();
        onclickFunctions();
        onChangeFunctions();
        initialiseDataBase();
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

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpWithGoogle();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHelpActivity();
            }
        });
    }

    public void ouvrirSignup2Activity(){
        Intent intent = new Intent(getApplicationContext(), Signup2Activity.class);
        intent.putExtra("fullname", fullname.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
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

    public boolean isNumber(String text) {
        Matcher matcher = Pattern.compile("^[0-9]*$").matcher(text);
        return matcher.matches();
    }

    public boolean isLength(String text){
        return text.length() >= 8;
    }

    public void validateFormSignUp1(){
        if(isEmpty(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_required));
            setInputLayoutErrors(inputFullname, fullname);
        }

        else if(isEmpty(phone.getText().toString())){
            setErreurText(erreurPhone,getString(R.string.phone_required));
            setInputLayoutErrors(inputPhone, phone);
        }

        else if(isEmpty(gender.getText().toString())){
            setErreurText(erreurGender,getString(R.string.gender_required));
            setInputGenderErrors(inputGender, gender);
        }

        else if(isFullname == true && isPhone == true && isGender == true){
            setErreurNull(erreurFullname);
            setErreurNull(erreurPhone);
            setErreurNull(erreurGender);
            setInputLayoutNormal(inputFullname,fullname);
            setInputLayoutNormal(inputPhone,phone);
            setInputGenderNormal(inputGender,gender);
            chargementIfPhoneRegistred();
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

    public void validateGender(){
        if(isEmpty(gender.getText().toString())){
            setErreurText(erreurGender,getString(R.string.gender_required));
            setInputGenderErrors(inputGender, gender);
            isGender = false;
        }

        else{
            setErreurNull(erreurGender);
            setInputGenderNormal(inputGender, gender);
            isGender = true;
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

    public void setInputGenderErrors(TextInputLayout input, AutoCompleteTextView text){
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

    public void setInputGenderNormal(TextInputLayout input, AutoCompleteTextView text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            input.setBackground(getDrawable(R.drawable.edi_text_background));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                text.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.black)));
            }
        }
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
                        showErreurFacebookDialog();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showErreurFacebookDialog();
                    }
                });
    }

    public void signUpWithFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email","user_friends","user_birthday"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == signUpGoogle){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handledSignedResult(task);
        }

        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handledSignedResult(Task<GoogleSignInAccount> completedTask){
        try {
            completedTask.getResult(ApiException.class);
            ouvrirSignupWithGoogleActivity();

        }

        catch (ApiException e) {
            showErreurGoogleDialog();
        }
    }

    public void ouvrirSignupWithFacebookActivity(){
        Intent intent = new Intent(getApplicationContext(), SignupWithFacebookActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void showErreurFacebookDialog(){
        dialog = new Dialog(Signup1Activity.this);
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
        desc.setText(R.string.desc_erreur_facebook);

        dialog.show();
    }

    public void intializeGoogleItems(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
    }

    public void signUpWithGoogle(){
        Intent signUpIntent = gsc.getSignInIntent();
        startActivityForResult(signUpIntent,signUpGoogle);
    }

    public void ouvrirSignupWithGoogleActivity(){
        Intent intent = new Intent(getApplicationContext(), SignupWithGoogleActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void showErreurGoogleDialog(){
        dialog = new Dialog(Signup1Activity.this);
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
        desc.setText(R.string.desc_erreur_google);

        dialog.show();
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void chargementIfPhoneRegistred(){
        final ProgressDialog progressDialog = new ProgressDialog(Signup1Activity.this, R.style.chargement);
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
                    setErreurText(erreurPhone,getString(R.string.phone_exist));
                    progressDialog.dismiss();
                }

                else{
                    setErreurNull(erreurPhone);
                    ouvrirSignup2Activity();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ouvrirHelpActivity(){
        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}