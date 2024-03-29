package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    private ImageView back, facebook, google;
    private TextView help, erreurEmail, erreurPassword;
    private AppCompatButton signIn;
    private TextInputEditText email, password;
    private Boolean isEmail = false, isPassword = false;
    private Dialog dialog;
    private CallbackManager callbackManager;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        back = (ImageView) findViewById(R.id.back);
        help = (TextView) findViewById(R.id.help);
        signIn = (AppCompatButton) findViewById(R.id.signin_btn);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        erreurEmail = (TextView) findViewById(R.id.erreur_email);
        erreurPassword = (TextView) findViewById(R.id.erreur_password);
        facebook = (ImageView) findViewById(R.id.facebook);
        google = (ImageView) findViewById(R.id.google);

        onClickFunctions();
        onChangeFunctions();
        initialiseDataBase();
        initializeFacebookSDK();
        intializeCallBackManager();
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

    public void ouvrirHelpActivity(){
        Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void onClickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirChoixLoginActivity();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHelpActivity();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormSignIn();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile","email","user_friends","user_birthday"));
                loginManagerActions();
            }
        });
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

    public void validateFormSignIn(){
        if(isEmpty(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_required));
        }

        else if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
        }

        else if(isEmail == true && isPassword == true){
            setErreurNull(erreurEmail);
            setErreurNull(erreurPassword);
            chargementNormalSignIn();
        }
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
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void loginManagerActions(){
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        chargementFacebookSignIn(loginResult.getAccessToken());
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

    public static String encodeString(String string) {
        return string.replace(".", ",");
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
        session.saveEmailApplication(email.getText().toString());
    }

    public void ouvrirHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void chargementNormalSignIn(){
        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signin_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                normalSignIn(progressDialog);
            }
        }).start();
    }

    public void normalSignIn(ProgressDialog progressDialog){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(encodeString(email.getText().toString()))){
                    setErreurText(erreurEmail,getString(R.string.no_account_found));
                    progressDialog.dismiss();
                }

                else{
                    setErreurNull(erreurEmail);
                    signInUserFirebase(snapshot,progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void signInUserFirebase(DataSnapshot snapshot, ProgressDialog progressDialog){
        final String getPassword = snapshot.child(encodeString(email.getText().toString())).child("password").getValue(String.class);

        if(!getPassword.equals(hashPassword(password.getText().toString()))){
            setErreurText(erreurPassword,getString(R.string.wrong_password));
            progressDialog.dismiss();
        }

        else{
            setErreurNull(erreurPassword);
            createSession();
            updateJournal(getString(R.string.normal_login),email.getText().toString());
            ouvrirHomeActivity();
            progressDialog.dismiss();
        }
    }

    public void showErreurFacebookDialog(){
        dialog = new Dialog(SignInActivity.this);
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

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.erreur_facebook));

        dialog.show();
    }

    public void chargementFacebookSignIn(AccessToken token){
        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signin_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                getEmailFromFacebook(progressDialog,token);
            }
        }).start();
    }

    public void getEmailFromFacebook(ProgressDialog progressDialog, AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                   checkIfEmailRegistred((object.getString("email")),progressDialog);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle paramestres = new Bundle();
        paramestres.putString("fields","id,name,email");
        request.setParameters(paramestres);
        request.executeAsync();
    }

    public void checkIfEmailRegistred(String email, ProgressDialog progressDialog){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(encodeString(email))){
                    setErreurText(erreurEmail,getString(R.string.no_account_found));
                    progressDialog.dismiss();
                }

                else{
                    setErreurNull(erreurPassword);
                    createSessionFacebook(email, progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void createSessionFacebook(String email, ProgressDialog progressDialog){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        session.saveEmailApplication(email);
        updateJournal(getString(R.string.facebook_login),email);
        progressDialog.dismiss();
        ouvrirHomeActivity();
    }

    public void updateJournal(String text, String email){
        String key = databaseReference.child("journal_users").push().getKey();
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("action").setValue(text);
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("date").setValue(getCurrentDate());
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("time").setValue(getCurrentTime());
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("phone").setValue(getAppareilUsed());
    }

    public String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return (formatter.format(date));
    }

    public String getCurrentTime(){
        Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return (formatter.format(time));
    }

    public String getAppareilUsed(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return (model + ".");
        }

        else {
            return (manufacturer + " " + model + ".");
        }
    }

    public void initializeFacebookSDK(){
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public void intializeCallBackManager(){
        callbackManager = CallbackManager.Factory.create();
    }
}