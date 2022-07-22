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
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullnameActivity extends AppCompatActivity {
    private ImageView back;
    private TextView copiright, erreurFullname, erreurSecondeName;
    private TextInputEditText fullname, secondeName;
    private AppCompatButton cancel, update;
    private Dialog dialog;
    private DatabaseReference databaseReference;
    private Boolean isFullname = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullname);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        fullname = (TextInputEditText) findViewById(R.id.fullname);
        secondeName = (TextInputEditText) findViewById(R.id.second_name);
        erreurFullname = (TextView) findViewById(R.id.erreur_fullname);
        erreurSecondeName = (TextView) findViewById(R.id.erreur_seconde_name);
        cancel = (AppCompatButton) findViewById(R.id.cancel_btn);
        update = (AppCompatButton) findViewById(R.id.update_btn);

        onclickFunctions();
        setCopyrightText();
        initialiseDataBase();
        setFullnamePersonne();
        onChangeFunctions();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirPersonalAccountActivity();
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
                validerFormFullname();
            }
        });
    }

    public void ouvrirPersonalAccountActivity(){
        Intent intent = new Intent(getApplicationContext(), PersonalAccountActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirPersonalAccountActivity();
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public void setFullnamePersonne(){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullname.setText(snapshot.child(encodeString(emailSession())).child("fullname").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public boolean isLetter(String text) {
        Matcher matcher = Pattern.compile("^[a-z A-Z]*$").matcher(text);
        return matcher.matches();
    }

    public void resetAllInputs(){
        fullname.setText("");
        secondeName.setText("");
        setErreurNull(erreurFullname);
        setErreurNull(erreurSecondeName);
    }

    public void validerFormFullname(){
        if(isEmpty(fullname.getText().toString())){
            setErreurText(erreurFullname,getString(R.string.username_required));
        }

        else if(isFullname == true){
            chargementUpdateNames();
        }
    }

    public void chargementUpdateNames(){
        final ProgressDialog progressDialog = new ProgressDialog(FullnameActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.update_names_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                updateNames(progressDialog);
            }
        }).start();
    }

    public void updateNames(ProgressDialog progressDialog){
        databaseReference.child("second_infos_users").child(encodeString(emailSession())).child("email").setValue(encodeString(emailSession()));
        if(secondeName.getText().toString() != null){
            databaseReference.child("second_infos_users").child(encodeString(emailSession())).child("seconde_name").setValue(secondeName.getText().toString());
            checkIfSecondeUpdated(progressDialog);
        }

        else{
            checkIfFulnameUpdated(progressDialog);
        }
        databaseReference.child("users").child(encodeString(emailSession())).child("fullname").setValue(fullname.getText().toString());
    }

    public void checkIfSecondeUpdated(ProgressDialog progressDialog){
        databaseReference.child("second_infos_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(encodeString(emailSession()))){
                    progressDialog.dismiss();
                    showSuccessNotificationFireBaseUpdated();
                }

                else{
                    showErreurNotificationFireBaseNotUpdated();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showSuccessNotificationFireBaseUpdated(){
        dialog = new Dialog(FullnameActivity.this);
        dialog.setContentView(R.layout.item_success);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView title = (TextView) dialog.findViewById(R.id.title_success);
        TextView desc = (TextView) dialog.findViewById(R.id.desc_title_success);

        title.setText(R.string.profil_changed);
        desc.setText(R.string.profil_changed_desc);

        TextView cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showErreurNotificationFireBaseNotUpdated(){
        dialog = new Dialog(FullnameActivity.this);
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
        desc.setText(R.string.error_update_infos_error);

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.update_infos_error));

        dialog.show();
    }

    public void checkIfFulnameUpdated(ProgressDialog progressDialog){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(encodeString(emailSession())).child("fullname").equals(fullname.getText().toString())){
                    progressDialog.dismiss();
                    showSuccessNotificationFireBaseUpdated();
                }

                else{
                    showErreurNotificationFireBaseNotUpdated();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}