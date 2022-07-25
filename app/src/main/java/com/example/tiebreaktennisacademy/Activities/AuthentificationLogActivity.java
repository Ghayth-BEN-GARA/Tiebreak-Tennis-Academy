package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.tiebreaktennisacademy.Adapters.JournalAdapter;
import com.example.tiebreaktennisacademy.Models.Journal;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AuthentificationLogActivity extends AppCompatActivity {
    private ImageView back;
    private RecyclerView recyclerView;
    private JournalAdapter journalAdapter;
    private DatabaseReference databaseReference;
    private ArrayList<Journal> journals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification_log);

        back = (ImageView) findViewById(R.id.back);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_journal);

        onclickFunctions();
        initialiseDataBase();
        initialiseItemRecycleView();
        initiliseListWithAdapter();
        getDataFromFireBase();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirPasswordSecurityActivity();
            }
        });
    }

    public void ouvrirPasswordSecurityActivity(){
        Intent intent = new Intent(getApplicationContext(), PasswordSecurityActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirPasswordSecurityActivity();
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void initialiseItemRecycleView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void initiliseListWithAdapter(){
        journals = new ArrayList<>();
        journalAdapter = new JournalAdapter(getApplicationContext(),journals);
        recyclerView.setAdapter(journalAdapter);
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public void getDataFromFireBase(){
        databaseReference.child("journal_users").child(encodeString(emailSession())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Journal journal = dataSnapshot.getValue(Journal.class);
                    journals.add(journal);
                }
                journalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}