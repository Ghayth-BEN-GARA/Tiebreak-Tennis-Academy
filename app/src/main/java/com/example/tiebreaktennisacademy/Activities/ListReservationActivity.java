package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.Adapters.ReservationAdapter;
import com.example.tiebreaktennisacademy.Models.Reservation;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
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
import java.util.List;
import java.util.Locale;

public class ListReservationActivity extends AppCompatActivity {
    private ImageView back, notification, before,next;
    private TextView copiright, curentDate, allReservations;
    private Dialog dialog;
    private CompactCalendarView planing;
    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private ArrayList<Reservation> reservationArrayList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reservation);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        notification = (ImageView) findViewById(R.id.notification);
        planing = (CompactCalendarView) findViewById(R.id.planing);
        curentDate = (TextView) findViewById(R.id.curentDate);
        before = (ImageView) findViewById(R.id.before);
        next = (ImageView) findViewById(R.id.next);
        allReservations = (TextView) findViewById(R.id.all_reservations);

        onclickFunctions();
        setCopyrightText();
        initialiseDataBase();
        checkIfBookingExist();
        configCalendar();
        initCurrentDate();
        checkIfReservationExist();
        getEventFromCalendar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirHomeActivity();
    }

    public void onclickFunctions() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoRervationsExist();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextMonth();
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousMonth();
            }
        });

        allReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAllReservationsActivity();
            }
        });
    }

    public void ouvrirHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirAllReservationsActivity(){
        Intent intent = new Intent(getApplicationContext(), AllListeReservationsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
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

    public void checkIfBookingExist(){
        databaseReference.child("reservations").orderByChild("email").equalTo(emailSession()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    hideNotification(notification);
                    showAllReservationsText(allReservations);
                }

                else{
                    showNotification(notification);
                    hideAllReservationsText(allReservations);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void showNotification(ImageView image){
        image.setVisibility(View.VISIBLE);
    }

    public void hideNotification(ImageView image){
        image.setVisibility(View.INVISIBLE);
    }

    public void showAllReservationsText(TextView text){
        text.setVisibility(View.VISIBLE);
    }

    public void hideAllReservationsText(TextView text){
        text.setVisibility(View.INVISIBLE);
    }

    public void showNoRervationsExist(){
        dialog = new Dialog(ListReservationActivity.this);
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
        desc.setText(R.string.no_reservation_desc);

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.no_reservation));

        dialog.show();
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public void configCalendar(){
        planing.setUseThreeLetterAbbreviation(true);
        planing.setShouldShowMondayAsFirstDay(true);
        planing.setShouldDrawDaysHeader(true);
    }

    public void showNextMonth(){
        planing.showCalendarWithAnimation();
        planing.showNextMonth();
    }

    public void showPreviousMonth(){
        planing.showCalendarWithAnimation();
        planing.showPreviousMonth();
    }

    public String stylingDate(Date current){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        return formatter.format(current);
    }

    public void initCurrentDate(){
        curentDate.setText(stylingDate(planing.getFirstDayOfCurrentMonth()));
    }

    public void checkIfReservationExist(){
        databaseReference.child("reservations").orderByChild("email").equalTo(emailSession()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    getListEventFromFireBaseIntoCalendar();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void getListEventFromFireBaseIntoCalendar(){
        databaseReference.child("reservations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Calendar myCalendar = Calendar.getInstance();
                String[] datesList = new String[0];

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("email").getValue().toString().equals(emailSession())){
                        String dateEvent = dataSnapshot.child("date").getValue().toString();
                        datesList = dateEvent.split("-");
                        int mon = Integer.parseInt(datesList[1]);
                        myCalendar.set(Calendar.YEAR, Integer.parseInt(datesList[0]));
                        myCalendar.set(Calendar.MONTH, mon - 1);
                        myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datesList[2]));

                        Event event = new Event(getResources().getColor(R.color.green), myCalendar.getTimeInMillis(), "Reservation");
                        planing.addEvent(event,true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void getEventFromCalendar(){
        planing.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = planing.getEvents(dateClicked);
                if(events.size() == 0){
                    showNoEventFound();
                }

                else{
                    showEventFound(stylingDateClicked(dateClicked));
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                curentDate.setText(stylingDate(firstDayOfNewMonth));
            }
        });
    }

    public void showNoEventFound(){
        dialog = new Dialog(ListReservationActivity.this);
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
        desc.setText(R.string.no_event_sad);

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.title_no_event));

        dialog.show();
    }

    public String stylingDateClicked(Date current){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = formatter.format(current);
        return date;
    }

    public void showEventFound(String date){
        dialog = new Dialog(ListReservationActivity.this);
        dialog.setContentView(R.layout.item_show_liste_reservations);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

        TextView desc = dialog.findViewById(R.id.desc_title_success);
        desc.setText(getString(R.string.desc_list_reservations));

        TextView title = dialog.findViewById(R.id.title_success);
        title.setText(getString(R.string.list_reservation));

        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_reservations);

        initialiseItemRecycleView();
        initiliseListWithAdapter();
        getDataFromFireBase(date);

        dialog.show();
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

    public void getDataFromFireBase(String date){
        databaseReference.child("reservations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("email").getValue().toString().equals(emailSession())){
                        if(dataSnapshot.child("date").getValue().toString().equals(date)){
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
                }
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

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