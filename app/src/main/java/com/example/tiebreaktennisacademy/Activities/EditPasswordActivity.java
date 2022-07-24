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
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPasswordActivity extends AppCompatActivity {
    private ImageView back;
    private TextView copiright, erreurPassword, erreurNewPassword, erreurRepeatNewPassword;
    private AppCompatButton cancel, update;
    private TextInputEditText password, newPassword, repeatNewPassword;
    private Dialog dialog;
    private DatabaseReference databaseReference;
    private Boolean isOld = false, isNew = false, isRepeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        cancel = (AppCompatButton) findViewById(R.id.cancel_btn);
        password = (TextInputEditText) findViewById(R.id.old_password);
        newPassword = (TextInputEditText) findViewById(R.id.new_password);
        repeatNewPassword = (TextInputEditText) findViewById(R.id.repeat_new_password);
        erreurPassword = (TextView) findViewById(R.id.erreur_old_password);
        erreurNewPassword = (TextView) findViewById(R.id.erreur_new_password);
        erreurRepeatNewPassword = (TextView) findViewById(R.id.erreur_repeat_new_password);
        update = (AppCompatButton) findViewById(R.id.update_btn);

        onclickFunctions();
        setCopyrightText();
        onChangeFunctions();
        initialiseDataBase();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirParametresSecurityActivity();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllInputs();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormUpdatePassword();
            }
        });
    }

    public void ouvrirParametresSecurityActivity(){
        Intent intent = new Intent(getApplicationContext(), PasswordSecurityActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirParametresSecurityActivity();
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public void resetAllInputs(){
        password.setText("");
        newPassword.setText("");
        repeatNewPassword.setText("");
        setErreurNull(erreurPassword);
        setErreurNull(erreurNewPassword);
        setErreurNull(erreurRepeatNewPassword);
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
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

    public boolean isEquals(String text1, String text2){
        return text1.equals(text2);
    }

    public boolean isLength(String text){
        return text.length() >= 5;
    }

    public void onChangeFunctions(){
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateOldPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateNewPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        repeatNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateRepeatNewPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateOldPassword(){
        if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
            isOld = false;
        }

        else if(!isMinuscule(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_minisucle));
            isOld = false;
        }

        else if(!isMajuscule(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_majuscule));
            isOld = false;
        }

        else if(!isChiffre(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_number));
            isOld = false;
        }

        else if(!isLength(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_length));
            isOld = false;
        }

        else{
            setErreurNull(erreurPassword);
            isOld = true;
        }
    }

    public void validateNewPassword(){
        if(isEmpty(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_required));
            isNew = false;
        }

        else if(!isMinuscule(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_minisucle));
            isNew = false;
        }

        else if(!isMajuscule(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_majuscule));
            isNew = false;
        }

        else if(!isChiffre(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_number));
            isNew = false;
        }

        else if(!isLength(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_length));
            isNew = false;
        }

        else{
            setErreurNull(erreurNewPassword);
            isNew = true;
        }
    }

    public void validateRepeatNewPassword(){
        if(isEmpty(repeatNewPassword.getText().toString())){
            setErreurText(erreurRepeatNewPassword,getString(R.string.password_required));
            isRepeat = false;
        }

        else if(!isMinuscule(repeatNewPassword.getText().toString())){
            setErreurText(erreurRepeatNewPassword,getString(R.string.password_minisucle));
            isRepeat = false;
        }

        else if(!isMajuscule(repeatNewPassword.getText().toString())){
            setErreurText(erreurRepeatNewPassword,getString(R.string.password_majuscule));
            isRepeat = false;
        }

        else if(!isChiffre(repeatNewPassword.getText().toString())){
            setErreurText(erreurRepeatNewPassword,getString(R.string.password_number));
            isRepeat = false;
        }

        else if(!isLength(repeatNewPassword.getText().toString())){
            setErreurText(erreurRepeatNewPassword,getString(R.string.password_length));
            isRepeat = false;
        }

        else{
            setErreurNull(erreurRepeatNewPassword);
            isRepeat = true;
        }
    }

    public void validateFormUpdatePassword(){
        if(isEmpty(password.getText().toString())){
            setErreurText(erreurPassword,getString(R.string.password_required));
        }

        else if(isEmpty(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_required));
        }

        else if(isEmpty(repeatNewPassword.getText().toString())){
            setErreurText(erreurRepeatNewPassword,getString(R.string.password_required));
        }

        else if(!isEquals(newPassword.getText().toString(),repeatNewPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_not_equals));
            setErreurText(erreurRepeatNewPassword,getString(R.string.password_not_equals));
        }

        else if(isOld == true && isNew == true && isRepeat == true){
            setErreurNull(erreurPassword);
            setErreurNull(erreurNewPassword);
            setErreurNull(erreurRepeatNewPassword);
            chargementUpdatePassword();
        }
    }

    public void chargementUpdatePassword(){
        final ProgressDialog progressDialog = new ProgressDialog(EditPasswordActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.update_password_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                checkRightOldPassword(progressDialog);
            }
        }).start();
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
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

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void checkRightOldPassword(ProgressDialog progressDialog){
        String oldPasswordHashed = hashPassword(password.getText().toString());
        String newPasswordHashed = hashPassword(newPassword.getText().toString());

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(encodeString(emailSession())).child("password").getValue(String.class).equals(oldPasswordHashed)){
                    if(snapshot.child(encodeString(emailSession())).child("password").getValue(String.class).equals(newPasswordHashed)){
                        setErreurText(erreurNewPassword,getString(R.string.old_new_password));
                        progressDialog.dismiss();
                    }

                    else{
                        setErreurNull(erreurNewPassword);
                        updatePassword(progressDialog);
                    }
                }

                else{
                    setErreurText(erreurPassword,getString(R.string.old_password_invalid));
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updatePassword(ProgressDialog progressDialog){
        if(isPasswordChanged(emailSession())){
            progressDialog.dismiss();
            viderAllInputs();
            showSuccessNotification();
        }
    }

    public boolean isPasswordChanged(String email){
        String passwordHashed = hashPassword(newPassword.getText().toString());

        databaseReference.child("users").child(encodeString(email)).child("password").setValue(passwordHashed);
        return true;
    }

    public void showSuccessNotification(){
        dialog = new Dialog(EditPasswordActivity.this);
        dialog.setContentView(R.layout.item_success);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView title = (TextView) dialog.findViewById(R.id.title_success);
        TextView desc = (TextView) dialog.findViewById(R.id.desc_title_success);

        title.setText(R.string.password_changed);
        desc.setText(R.string.password_changed_desc);

        TextView cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void viderAllInputs(){
        password.setText("");
        newPassword.setText("");
        repeatNewPassword.setText("");
        setErreurNull(erreurPassword);
        setErreurNull(erreurNewPassword);
        setErreurNull(erreurRepeatNewPassword);
    }
}