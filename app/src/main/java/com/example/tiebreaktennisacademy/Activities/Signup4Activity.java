package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Signup4Activity extends AppCompatActivity {

    private ImageView back;
    private AppCompatButton getStarted;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        back = (ImageView) findViewById(R.id.back);
        getStarted = (AppCompatButton) findViewById(R.id.create_account_btn);

        onclickFunctions();
        initialiseDataBase();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),R.string.not_available,Toast.LENGTH_LONG).show();
            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeAccount();
                ouvrirHomeActivity();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),R.string.not_return,Toast.LENGTH_LONG).show();
    }

    public void createSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        session.saveEmailApplication(decodeString(getIntent().getStringExtra("email")));
    }

    public void ouvrirHomeActivity(){
        createSession();
        updateJournal(getString(R.string.normal_signup),getIntent().getStringExtra("email"));
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public static String decodeString(String string) {
        return string.replace(",", ".");
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void activeAccount(){
        databaseReference.child("compte_users").child(encodeString(getIntent().getStringExtra("email"))).child("email").setValue(encodeString(getIntent().getStringExtra("email")));
        databaseReference.child("compte_users").child(encodeString(getIntent().getStringExtra("email"))).child("active").setValue("true");
    }

    public void updateJournal(String text, String email){
        String key = databaseReference.child("journal_users").push().getKey();
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("action").setValue(text);
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
}