package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfilActivity extends AppCompatActivity {
    private TextView fullname, emailP, phone, gender, naissance, taille, poid;
    private ImageView back;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        fullname = (TextView) findViewById(R.id.fullname);
        emailP = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.mobile);
        gender = (TextView) findViewById(R.id.gender);
        naissance = (TextView) findViewById(R.id.naissance);
        taille = (TextView) findViewById(R.id.taille);
        poid = (TextView) findViewById(R.id.poid);
        back = (ImageView) findViewById(R.id.back);

        onclickFunctions();
        initialiseDataBase();
        setDataPersonne();
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void setDataPersonne(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullname.setText(snapshot.child(encodeString(email)).child("fullname").getValue(String.class));
                emailP.setText(email);
                String ch = snapshot.child(encodeString(email)).child("phone").getValue(String.class);
                phone.setText("(+216) " + ch.substring(0,2) + " " + ch.substring(2,5) + " " + ch.substring(5,8));
                gender.setText(snapshot.child(encodeString(email)).child("gender").getValue(String.class));
                naissance.setText(stylingDateNaissance(snapshot.child(encodeString(email)).child("naissance").getValue(String.class)));
                taille.setText(snapshot.child(encodeString(email)).child("taille").getValue(String.class) + " cm");
                poid.setText(snapshot.child(encodeString(email)).child("poid").getValue(String.class) + " kg");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String stylingDateNaissance(String dateNaissance){
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatter2 = new SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter1.parse(dateNaissance);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter2.format(date);
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
            }
        });
    }


    public void ouvrirHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirHomeActivity();
    }
}