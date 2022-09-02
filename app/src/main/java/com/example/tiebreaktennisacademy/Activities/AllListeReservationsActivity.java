package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.Adapters.ReservationAdapter;
import com.example.tiebreaktennisacademy.Models.Reservation;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AllListeReservationsActivity extends AppCompatActivity {
    private ImageView back;
    private TextView copiright, currentDate,dataNotFound;
    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private ArrayList<Reservation> reservationArrayList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_liste_reservations);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        currentDate = (TextView) findViewById(R.id.curentDate);
        dataNotFound = (TextView) findViewById(R.id.no_data_found);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_reservations);

        onclickFunctions();
        setCopyrightText();
        setCurrentMonthYear();
        initialiseDataBase();
        checkIfBookingExist();
        initialiseItemRecycleView();
        initiliseListWithAdapter();
        getDataFromFireBase();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirListeReservationsActivity();
    }

    public void onclickFunctions() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirListeReservationsActivity();
            }
        });
    }

    public void ouvrirListeReservationsActivity(){
        Intent intent = new Intent(getApplicationContext(), ListReservationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public String stylingDate(Date current){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH);
        return formatter.format(current);
    }

    public Date getCurrentDate(){
        return Calendar.getInstance().getTime();
    }

    public void setCurrentMonthYear(){
        currentDate.setText(getResources().getString(R.string.today) +" " + stylingDate(getCurrentDate()));
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public void checkIfBookingExist(){
        databaseReference.child("reservations").orderByChild("email").equalTo(emailSession()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    hideNoDataFound(dataNotFound);
                }

                else{
                    showNoDataFound(dataNotFound);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void showNoDataFound(TextView textView){
        textView.setVisibility(View.VISIBLE);
    }

    public void hideNoDataFound(TextView textView){
        textView.setVisibility(View.INVISIBLE);
    }

    public void initialiseItemRecycleView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void initiliseListWithAdapter(){
        reservationArrayList = new ArrayList<>();
        reservationAdapter = new ReservationAdapter(getApplicationContext(),reservationArrayList);
        recyclerView.setAdapter(reservationAdapter);
    }

    public void getDataFromFireBase(){
        databaseReference.child("reservations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("email").getValue().toString().equals(emailSession())){
                        Reservation res = new Reservation();
                        res.setCoache(dataSnapshot.child("coache").getValue().toString());
                        String date = dataSnapshot.child("date").getValue().toString();
                        res.setDate(stylingDateFromDataBase(date));
                        res.setCourt(dataSnapshot.child("court").getValue().toString());
                        res.setFrom(dataSnapshot.child("fromTime").getValue().toString());
                        res.setTo(dataSnapshot.child("toTime").getValue().toString());

                        reservationArrayList.add(res);
                    }
                }
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String stylingDateFromDataBase(String dt){
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatter2 = new SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter1.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter2.format(date);
    }
}