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
import java.util.Calendar;

public class PersonalAccountActivity extends AppCompatActivity {
    private ImageView back, goFullname, goContact, goControl;
    private TextView fullname, copiright, titleFullname, titleContact, contact, titleControl, control;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_account);

        back = (ImageView) findViewById(R.id.back);
        fullname = (TextView) findViewById(R.id.fullname);
        copiright = (TextView) findViewById(R.id.copyright_app);
        goFullname = (ImageView) findViewById(R.id.go_fullname);
        titleFullname = (TextView) findViewById(R.id.title_fullname);
        goContact = (ImageView) findViewById(R.id.go_coordonnes);
        contact = (TextView) findViewById(R.id.desc_coordonnes);
        titleContact = (TextView) findViewById(R.id.title_coordonnes);
        goControl = (ImageView) findViewById(R.id.go_propriete);
        titleControl = (TextView) findViewById(R.id.title_propriete);
        control = (TextView) findViewById(R.id.desc_propriete);

        onclickFunctions();
        initialiseDataBase();
        setFullnamePersonne();
        setCopyrightText();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirParametresActivity();
            }
        });

        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirFullnameActivity();
            }
        });

        titleFullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirFullnameActivity();
            }
        });

        goFullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirFullnameActivity();
            }
        });

        goContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirContactInformationsActivity();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirContactInformationsActivity();
            }
        });

        titleContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirContactInformationsActivity();
            }
        });

        goControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirControlActivity();
            }
        });

        titleControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirControlActivity();
            }
        });

        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirControlActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirParametresActivity();
    }

    public void ouvrirParametresActivity(){
        Intent intent = new Intent(getApplicationContext(), ParametresActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
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

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public void ouvrirFullnameActivity(){
        Intent intent = new Intent(getApplicationContext(), FullnameActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirContactInformationsActivity(){
        Intent intent = new Intent(getApplicationContext(), ContactInformationsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirControlActivity(){
        Intent intent = new Intent(getApplicationContext(), ControlAccountActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}