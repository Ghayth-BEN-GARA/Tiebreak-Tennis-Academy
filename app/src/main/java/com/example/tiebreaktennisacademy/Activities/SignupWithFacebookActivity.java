package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tiebreaktennisacademy.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupWithFacebookActivity extends AppCompatActivity {
    private TextInputLayout inputFullname, inputEmail, inputPassword, inputGender, inputNaissance, inputTaille, inputPoid;
    private TextInputEditText fullname, email, password, naissance, gender, taille, poid;
    private TextView erreurFullname, erreurEmail, erreurPassword, erreurNaissance, erreurGender, erreurTaille, erreurPoid;
    private AppCompatButton signup;
    private Boolean isFullname = true, isEmail = false, isPassword = false, isNaissance = false, isGender = true, isTaille = false, isPoid = false;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_facebook);

        fullname = (TextInputEditText) findViewById(R.id.username);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        naissance = (TextInputEditText) findViewById(R.id.naissance);
        gender = (TextInputEditText) findViewById(R.id.gender);
        taille = (TextInputEditText) findViewById(R.id.taille);
        poid = (TextInputEditText) findViewById(R.id.poids);
        signup = (AppCompatButton) findViewById(R.id.signup_btn);
        erreurFullname = (TextView) findViewById(R.id.erreur_fullname);
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
        intializeToken();
        getInformationsFromFacebook();
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

        else if(isFullname == true && isEmail == true && isPassword == true && isGender == true && isNaissance == true && isTaille == true && isPoid == true){
            setErreurNull(erreurFullname);
            setErreurNull(erreurEmail);
            setErreurNull(erreurPassword);
            setErreurNull(erreurNaissance);
            setErreurNull(erreurGender);
            setErreurNull(erreurTaille);
            setErreurNull(erreurPoid);
            setInputLayoutNormal(inputFullname,fullname);
            setInputLayoutNormal(inputEmail,email);
            setInputLayoutNormal(inputPassword,password);
            setInputLayoutNormal(inputGender,gender);
            setInputLayoutNormal(inputNaissance,naissance);
            setInputLayoutNormal(inputTaille,taille);
            setInputLayoutNormal(inputPoid,poid);
            //signup
            logoutFromFacebook();
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

    public void intializeToken(){
        accessToken = AccessToken.getCurrentAccessToken();
    }

    public void getInformationsFromFacebook(){
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    fullname.setText(object.getString("name"));
                    email.setText(object.getString("email"));
                    String naiss = object.getString("birthday");
                    naissance.setText(naiss.substring(6, naiss.length())+"-"+naiss.substring(0, 2)+"-"+naiss.substring(3, 5));
                    String url = "https://genderapi.io/api/?name="+object.getString("first_name");

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject informations = new JSONObject(response);
                                if(informations.getString("gender").equals("male")){
                                    gender.setText("Man");
                                }

                                else{
                                    gender.setText("Female");
                                }


                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //add errors notifications
                        }
                    });
                    requestQueue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle paramestres = new Bundle();
        paramestres.putString("fields","name,email,first_name,birthday");
        request.setParameters(paramestres);
        request.executeAsync();
    }

    public void logoutFromFacebook(){
        LoginManager.getInstance().logOut();
    }
}