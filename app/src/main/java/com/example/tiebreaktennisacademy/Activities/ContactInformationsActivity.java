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

public class ContactInformationsActivity extends AppCompatActivity {
    private ImageView back;
    private TextView copiright, phone, email, erreurNumber;
    private AppCompatButton btnWhatssapp;
    private Dialog dialog;
    private TextInputEditText number;
    private DatabaseReference databaseReference;
    private Boolean isNumberWhatsapp = true;
    private String removedPhoneFromString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_informations);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        btnWhatssapp = (AppCompatButton) findViewById(R.id.add_whatsup_btn);

        onclickFunctions();
        setCopyrightText();
        initialiseDataBase();
        setDataPersonne();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirPersonalAccountActivity();
            }
        });

        btnWhatssapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotificationAddWatsApp();
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

    public void setDataPersonne(){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email.setText(emailSession());
                removedPhoneFromString = snapshot.child(encodeString(emailSession())).child("phone").getValue(String.class);
                phone.setText("(+216) " + removedPhoneFromString.substring(0,2) + " " + removedPhoneFromString.substring(2,5) + " " + removedPhoneFromString.substring(5,8));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showNotificationAddWatsApp(){
        dialog = new Dialog(ContactInformationsActivity.this);
        dialog.setContentView(R.layout.item_add_watssapp);
        dialog.getWindow().getDecorView().setLeft(30);
        dialog.getWindow().getDecorView().setRight(30);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView cancel = dialog.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        number = dialog.findViewById(R.id.whatsapp);
        erreurNumber = dialog.findViewById(R.id.erreur_whatsapp);
        AppCompatButton update = dialog.findViewById(R.id.update_whatsapp_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validerFormNumber();
            }
        });

        setNumberPersonne();
        onChangeEditWhatsappFunctions();

        dialog.show();
    }

    public void onChangeEditWhatsappFunctions(){
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateNumber(){
        if(isEmpty(number.getText().toString())){
            setErreurText(erreurNumber,getString(R.string.phone_required));
            isNumberWhatsapp = false;
        }

        else if(!isNumber(number.getText().toString())){
            setErreurText(erreurNumber,getString(R.string.phone_number));
            isNumberWhatsapp = false;
        }

        else if(!isLength(number.getText().toString())){
            setErreurText(erreurNumber,getString(R.string.phone_length));
            isNumberWhatsapp = false;
        }

        else{
            setErreurNull(erreurNumber);
            isNumberWhatsapp = true;
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

    public boolean isLength(String text){
        return text.length() >= 8;
    }

    public boolean isNumber(String text) {
        Matcher matcher = Pattern.compile("^[0-9]*$").matcher(text);
        return matcher.matches();
    }

    public void validerFormNumber(){
        if(isEmpty(number.getText().toString())){
            setErreurText(erreurNumber,getString(R.string.phone_required));
        }

        else if(isNumberWhatsapp == true){
            setErreurNull(erreurNumber);
            chargementUpdateNumber();
        }
    }

    public void chargementUpdateNumber(){
        final ProgressDialog progressDialog = new ProgressDialog(ContactInformationsActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.whatssapp_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                checkIfPhoneExist(progressDialog);
            }
        }).start();
    }

    public void checkIfPhoneExist(ProgressDialog progressDialog){
        databaseReference.child("users").orderByChild("phone").equalTo(number.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null && !snapshot.hasChild(encodeString(emailSession()))){
                    setErreurText(erreurNumber, getString(R.string.whatsapp_exist));
                    progressDialog.dismiss();
                }

                else{
                    chechIfNumberWhatsAppExist(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void chechIfNumberWhatsAppExist(ProgressDialog progressDialog){
        databaseReference.child("second_infos_users").orderByChild("whatsapp").equalTo(number.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null && !snapshot.hasChild(encodeString(emailSession()))){
                    setErreurText(erreurNumber, getString(R.string.whatsapp_exist));
                    progressDialog.dismiss();
                }

                else{
                    addWhatsappNumber(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addWhatsappNumber(ProgressDialog progressDialog){
        databaseReference.child("second_infos_users").child(encodeString(emailSession())).child("whatsapp").setValue(number.getText().toString());
        checkIfFirebaseUpdated(progressDialog);
    }

    public void checkIfFirebaseUpdated(ProgressDialog progressDialog){
        databaseReference.child("second_infos_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(encodeString(emailSession()))){
                    progressDialog.dismiss();
                    dialog.dismiss();
                    showSuccessNotificationFireBaseUpdated();
                }

                else{
                    dialog.dismiss();
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
        dialog = new Dialog(ContactInformationsActivity.this);
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
        dialog = new Dialog(ContactInformationsActivity.this);
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

    public void setNumberPersonne(){
        databaseReference.child("second_infos_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((snapshot.child(encodeString(emailSession())).child("whatsapp").getValue(String.class) != null) && (snapshot.child(encodeString(emailSession())).child("adresse").getValue(String.class) != null)){
                    number.setText(snapshot.child(encodeString(emailSession())).child("whatsapp").getValue(String.class));
                }

                else{
                    number.setText(removedPhoneFromString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}