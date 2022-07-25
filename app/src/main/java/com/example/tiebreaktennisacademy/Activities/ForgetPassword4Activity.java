package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ForgetPassword4Activity extends AppCompatActivity {
    private ImageView back;
    private AppCompatButton get;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password4);

        back = (ImageView) findViewById(R.id.back);
        get = (AppCompatButton) findViewById(R.id.get_account_btn);

        onclickFunctions();
        initialiseDataBase();
    }

    @Override
    public void onBackPressed() {
        ouvrirForgetPassword1Activity();
    }

    public void ouvrirForgetPassword1Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirHomeActivity(){
        updateJournal(getString(R.string.normal_forget),emailSession());
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirForgetPassword1Activity();
            }
        });

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
            }
        });
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
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

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }
}