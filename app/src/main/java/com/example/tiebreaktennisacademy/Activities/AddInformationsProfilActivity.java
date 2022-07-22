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
import com.bumptech.glide.Glide;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddInformationsProfilActivity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton profile;
    private CircleImageView imageViewProfil;
    private TextView erreurCin, erreurAdresse;
    private TextInputEditText cin, adresse;
    private AppCompatButton saveInformations;
    private Dialog dialog;
    private Boolean isCin = false, isAdresse = false;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_informations_profil);

        back = (ImageView) findViewById(R.id.back);
        profile = (AppCompatButton) findViewById(R.id.profil);
        imageViewProfil = (CircleImageView) findViewById(R.id.photo_profil);
        erreurCin = (TextView) findViewById(R.id.erreur_cin);
        erreurAdresse = (TextView) findViewById(R.id.erreur_ville);
        cin = (TextInputEditText) findViewById(R.id.cin);
        adresse = (TextInputEditText) findViewById(R.id.ville);
        saveInformations = (AppCompatButton) findViewById(R.id.edit_informations_btn);

        onclickFunctions();
        initialiseDataBase();
        setImagePersonne();
        onChangeFunctions();
        setInformationsIfExist();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirProfileActivity();
    }

    public void ouvrirProfileActivity(){
        Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void onclickFunctions() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirProfileActivity();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirProfileActivity();
            }
        });

        saveInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormAddInformations();
            }
        });
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void setImagePersonne(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();

        databaseReference.child("images_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(encodeString(email)).child("photo").getValue(String.class) != null){

                    Glide
                            .with(getApplicationContext())
                            .load(snapshot.child(encodeString(email)).child("photo").getValue(String.class))
                            .centerCrop()
                            .into(imageViewProfil);
                }

                else{
                    imageViewProfil.setImageResource(R.drawable.user);
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

    public void validateFormAddInformations(){
        if(isEmpty(cin.getText().toString())){
            setErreurText(erreurCin,getString(R.string.cin_required));
        }

        else if(isEmpty(adresse.getText().toString())){
            setErreurText(erreurAdresse,getString(R.string.adresse_required));
        }

        else if(isCin == true && isAdresse == true){
            setErreurNull(erreurCin);
            setErreurNull(erreurAdresse);
            chargementAddInformations();
        }
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

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public void onChangeFunctions(){
        cin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCin();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adresse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateAdresse();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateCin(){
        if(isEmpty(cin.getText().toString())){
            setErreurText(erreurCin,getString(R.string.cin_required));
            isCin = false;
        }

        else if(!isNumber(cin.getText().toString())){
            setErreurText(erreurCin,getString(R.string.cin_not_number));
            isCin = false;
        }

        else if(!isLength(cin.getText().toString())){
            setErreurText(erreurCin,getString(R.string.cin_length));
            isCin = false;
        }

        else{
            setErreurNull(erreurCin);
            isCin = true;
        }
    }

    public void validateAdresse(){
        if(isEmpty(adresse.getText().toString())){
            setErreurText(erreurAdresse,getString(R.string.adresse_required));
            isAdresse = false;
        }

        else{
            setErreurNull(erreurAdresse);
            isAdresse = true;
        }
    }

    public void chargementAddInformations(){
        final ProgressDialog progressDialog = new ProgressDialog(AddInformationsProfilActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.update_password_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                checkIfCinExiste(progressDialog);
            }
        }).start();
    }

    public void checkIfCinExiste(ProgressDialog progressDialog){
        databaseReference.child("second_infos_users").orderByChild("cin").equalTo(cin.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null && !snapshot.hasChild(encodeString(emailSession()))){
                    setErreurText(erreurCin, getString(R.string.cin_exist));
                    progressDialog.dismiss();
                }

                else{
                    addSecondInformations(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public void addSecondInformations(ProgressDialog progressDialog){
        databaseReference.child("second_infos_users").child(encodeString(emailSession())).child("email").setValue(encodeString(emailSession()));
        databaseReference.child("second_infos_users").child(encodeString(emailSession())).child("cin").setValue(cin.getText().toString());
        databaseReference.child("second_infos_users").child(encodeString(emailSession())).child("adresse").setValue(adresse.getText().toString());
        checkIfFirebaseUpdated(progressDialog);
    }

    public void checkIfFirebaseUpdated(ProgressDialog progressDialog){
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
        dialog = new Dialog(AddInformationsProfilActivity.this);
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
        dialog = new Dialog(AddInformationsProfilActivity.this);
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

    public void setInformationsIfExist(){
        databaseReference.child("second_infos_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((snapshot.child(encodeString(emailSession())).child("cin").getValue(String.class) != null) && (snapshot.child(encodeString(emailSession())).child("adresse").getValue(String.class) != null)){
                    cin.setText(snapshot.child(encodeString(emailSession())).child("cin").getValue(String.class));
                    adresse.setText(snapshot.child(encodeString(emailSession())).child("adresse").getValue(String.class));
                }

                else{
                    cin.setText("");
                    adresse.setText("");
                    erreurCin.setText("");
                    erreurAdresse.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}