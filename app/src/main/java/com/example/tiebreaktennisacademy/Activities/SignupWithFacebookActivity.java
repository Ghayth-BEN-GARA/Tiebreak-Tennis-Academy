package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ScrollView;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupWithFacebookActivity extends AppCompatActivity {
    private TextInputEditText fullname, email, password, naissance, taille, poid;
    private DatePickerDialog.OnDateSetListener date;
    private TextView erreurFullname, erreurEmail, erreurPassword, erreurNaissanceGender, erreurTaillePoid;
    private ArrayAdapter<String> arrayAdapter;
    private AutoCompleteTextView gender;
    private ScrollView scrollView;
    private AppCompatButton signup;
    private String[] genderItems;
    private Boolean isFullname = true, isEmail = false, isPassword = false, isNaissance = false, isGender = true, isTaille = false, isPoid = false;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_facebook);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        fullname = (TextInputEditText) findViewById(R.id.username);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        naissance = (TextInputEditText) findViewById(R.id.naissance);
        gender = (AutoCompleteTextView) findViewById(R.id.gender);
        taille = (TextInputEditText) findViewById(R.id.taille);
        poid = (TextInputEditText) findViewById(R.id.poids);
        signup = (AppCompatButton) findViewById(R.id.signup_btn);
        erreurFullname = (TextView) findViewById(R.id.erreur_fullname);
        erreurEmail = (TextView) findViewById(R.id.erreur_email);
        erreurPassword = (TextView) findViewById(R.id.erreur_password);
        erreurNaissanceGender = (TextView) findViewById(R.id.erreur_gender_date);
        erreurTaillePoid = (TextView) findViewById(R.id.erreur_poid_size);

        setGenderItems();
        onclickFunctions();
        onChangeFunctions();
        onFocusFunctions();
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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormSignUpWithFacebook();
            }
        });
    }

    public  void configurerCalandrier(){
        Calendar agenda = Calendar.getInstance();
        int year = agenda.get(Calendar.YEAR);
        int month = agenda.get(Calendar.MONTH);
        int day = agenda.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(SignupWithFacebookActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, date, year , month, day);
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

    public void setGenderItems(){
        genderItems = getResources().getStringArray(R.array.gender_items);
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.items_gender,genderItems);
        gender.setAdapter(arrayAdapter);
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

    public void validateGender(){
        if(isEmpty(gender.getText().toString())){
            setErreurText(erreurNaissanceGender,getString(R.string.gender_required));
            isGender = false;
        }

        else{
            setErreurNull(erreurNaissanceGender);
            isGender = true;
        }
    }

    public void validateNaissance(){
        if(isEmpty(naissance.getText().toString())){
            setErreurText(erreurNaissanceGender,getString(R.string.naissance_required));
            isNaissance = false;
        }

        else{
            setErreurNull(erreurNaissanceGender);
            isNaissance = true;
        }
    }

    public void validateSize(){
        if(isEmpty(taille.getText().toString())){
            setErreurText(erreurTaillePoid,getString(R.string.taille_required));
            isTaille = false;
        }

        else if(!isNumber(taille.getText().toString())){
            setErreurText(erreurTaillePoid,getString(R.string.taille_number));
            isTaille = false;
        }

        else{
            setErreurNull(erreurTaillePoid);
            isTaille = true;
        }
    }

    public void validatePoid(){
        if(isEmpty(poid.getText().toString())){
            setErreurText(erreurTaillePoid,getString(R.string.poid_required));
            isPoid = false;
        }

        else if(!isNumber(poid.getText().toString())){
            setErreurText(erreurTaillePoid,getString(R.string.poid_number));
            isPoid = false;
        }

        else{
            setErreurNull(erreurTaillePoid);
            isPoid = true;
        }
    }

    public void onFocusFunctions(){
        taille.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToTop();
            }
        });

        poid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    public void validateFormSignUpWithFacebook(){
        if(isEmpty(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_required));
        }

        else if(isEmpty(email.getText().toString())){
            setErreurText(erreurEmail,getString(R.string.email_required));
        }

        else if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
        }

        else if(isEmpty(gender.getText().toString())){
            setErreurText(erreurNaissanceGender,getString(R.string.gender_required));
        }

        else if(isEmpty(naissance.getText().toString())){
            setErreurText(erreurNaissanceGender,getString(R.string.naissance_required));
        }

        else if(isEmpty(taille.getText().toString())){
            setErreurText(erreurTaillePoid,getString(R.string.taille_required));
        }

        else if(isEmpty(poid.getText().toString())){
            setErreurText(erreurTaillePoid,getString(R.string.poid_required));
        }

        else if(isFullname == true && isEmail == true && isPassword == true && isGender == true && isNaissance == true && isTaille == true && isPoid == true){
            setErreurNull(erreurFullname);
            setErreurNull(erreurEmail);
            setErreurNull(erreurPassword);
            setErreurNull(erreurNaissanceGender);
            setErreurNull(erreurTaillePoid);
            //signup
            logoutFromFacebook();
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
                                JSONObject informations = new JSONObject(response);;
                                gender.setText(informations.getString("gender"));

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