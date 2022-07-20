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
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.concurrent.TimeUnit;

public class ForgetPassword1Activity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton next;
    private TextView erreurEmail;
    private TextInputEditText email;
    private TextInputLayout textInputEmail;
    private Dialog dialog;
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
        }

        else if(isEmail == true){
            setErreurNull(erreurEmail);
            chargementIfEmailRegistred();
        }
    }

    public void ouvrirChoixLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), ChoixLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirForgetPassword2Activity(String phone, String verificationId){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword2Activity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("verificationId",verificationId);
        intent.putExtra("email",email.getText().toString());
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

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void chargementIfEmailRegistred(){
        final ProgressDialog progressDialog = new ProgressDialog(ForgetPassword1Activity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.getting_progress));
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
                    sendMessageToPhone(snapshot,progressDialog);
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

    public void sendMessageToPhone(DataSnapshot snapshot, ProgressDialog progressDialog){
        final String phone = snapshot.child(encodeString(email.getText().toString())).child("phone").getValue(String.class);

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+216" + phone,60, TimeUnit.SECONDS,ForgetPassword1Activity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        showNotificationErrorFonctionnalite();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressDialog.dismiss();
                        ouvrirForgetPassword2Activity(phone,s);;
                    }
                });
    }

    public void showNotificationErrorFonctionnalite(){
        dialog = new Dialog(ForgetPassword1Activity.this);
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
        desc.setText(R.string.forget_not_available);

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.forget_password_error));

        dialog.show();
    }

}