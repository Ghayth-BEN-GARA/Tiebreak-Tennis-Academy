package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PlaningActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private ImageView back;
    private TextView copiright, erreurRadio, erreurDate, erreurTime;
    private TextInputEditText coache, dateBooking, fromTime, toTime;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private AppCompatButton bookBtn;
    private Dialog dialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planing);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        coache = (TextInputEditText) findViewById(R.id.coach);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        dateBooking = (TextInputEditText) findViewById(R.id.date);
        fromTime = (TextInputEditText) findViewById(R.id.fromInput);
        toTime = (TextInputEditText) findViewById(R.id.toInput);
        bookBtn = (AppCompatButton) findViewById(R.id.book_btn);
        erreurRadio = (TextView) findViewById(R.id.erreur_court);
        erreurDate = (TextView) findViewById(R.id.erreur_date);
        erreurTime = (TextView) findViewById(R.id.erreur_time);

        onclickFunctions();
        setCopyrightText();
        onRadioButtonChangeListener();
        initialiseDataBase();
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

        dateBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurerCalandrier();
            }
        });

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurerMontreFrom();
            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurerMontreTo();
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationFormulaireCourt();
            }
        });
    }

    public void ouvrirHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public void setCoacheName(){
        if(radioButton.getText().equals("Court 1")){
            coache.setText(getResources().getString(R.string.nom_jarred));
        }

        else if(radioButton.getText().equals("Court 2")){
            coache.setText(getResources().getString(R.string.nom_kieron));
        }

        else if(radioButton.getText().equals("Court 3")){
            coache.setText(getResources().getString(R.string.nom_mal));
        }
    }

    public void onRadioButtonChangeListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                setCoacheName();
                setErreurNull(erreurRadio);
            }
        });
    }

    public void configurerCalandrier(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(PlaningActivity.this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        String chaine = year + "-" + month + "-" + dayOfMonth;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(chaine);
            dateBooking.setText(format.format(date));
            setErreurNull(erreurDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void configurerMontreFrom(){
        Calendar calendar = Calendar.getInstance();
        int heure = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(PlaningActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                fromTime.setText(hourOfDay + ":" + minute);
                setErreurNull(erreurTime);
            }
        },heure,minute, true);

        timePickerDialog.show();
    }

    public void configurerMontreTo(){
        Calendar calendar = Calendar.getInstance();
        int heure = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(PlaningActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                toTime.setText(hourOfDay + ":" + minute);
                setErreurNull(erreurTime);
            }
        },heure,minute, true);

        timePickerDialog.show();
    }

    public void validationFormulaireCourt(){
        if(radioGroup.getCheckedRadioButtonId() == -1){
            setErreurText(erreurRadio,getString(R.string.court_required));
        }

        else if(dateBooking.getText().toString().equals("")){
            setErreurText(erreurDate,getString(R.string.date_required));
        }

        else if(fromTime.getText().toString().equals("")){
            setErreurText(erreurTime,getString(R.string.from_time_required));
        }

        else if(toTime.getText().toString().equals("")){
            setErreurText(erreurTime,getString(R.string.end_time_required));
        }

        else {
            Date from = convertStringToTime(fromTime.getText().toString());
            Date to = convertStringToTime(toTime.getText().toString());

            if (from.compareTo(to) > 0) {
                setErreurText(erreurTime,getString(R.string.end_time_false));
            }

            else{
                setErreurNull(erreurRadio);
                setErreurNull(erreurDate);
                setErreurNull(erreurTime);

                chargementCheckIfBookingExist();
            }
        }
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public void chargementCheckIfBookingExist(){
        final ProgressDialog progressDialog = new ProgressDialog(PlaningActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.book_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                CheckIfBookingExist(progressDialog);
            }
        }).start();
    }

    public void CheckIfBookingExist(ProgressDialog progressDialog){
        databaseReference.child("reservations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chechIfDateOfBookExist(progressDialog);
                }

                else{
                    ajouterReservation(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showNotificationAjouteReservationImpossible();
            }
        });
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public void showNotificationAjouteReservationImpossibleTime(){
        dialog = new Dialog(PlaningActivity.this);
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
        desc.setText(R.string.another_booking_this_informations);

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.booking_error));

        dialog.show();
    }

    public void ajouterReservation(ProgressDialog progressDialog){
        if(creerReservations(radioButton.getText().toString(), coache.getText().toString(), dateBooking.getText().toString(), fromTime.getText().toString(), toTime.getText().toString())){
            progressDialog.dismiss();
            showNotificationAjouteReservationSuccess();
        }

        else{
            progressDialog.dismiss();
            showNotificationAjouteReservationImpossible();
        }
    }

    public boolean creerReservations(String court, String coaches, String date, String fromT, String toT){
        String key = databaseReference.child("reservations").push().getKey();
        databaseReference.child("reservations").child(key).child("email").setValue(emailSession());
        databaseReference.child("reservations").child(key).child("court").setValue(court);
        databaseReference.child("reservations").child(key).child("coache").setValue(coaches);
        databaseReference.child("reservations").child(key).child("date").setValue(date);
        databaseReference.child("reservations").child(key).child("fromTime").setValue(fromT);
        databaseReference.child("reservations").child(key).child("toTime").setValue(toT);
        return true;
    }

    public void showNotificationAjouteReservationSuccess(){
        dialog = new Dialog(PlaningActivity.this);
        dialog.setContentView(R.layout.item_success);
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
                ouvrirPlaningActivity();
            }
        });

        TextView desc = dialog.findViewById(R.id.desc_title_success);
        desc.setText(getString(R.string.booking_success_desc));

        TextView title = dialog.findViewById(R.id.title_success);
        title.setText(getString(R.string.booking_success));

        dialog.show();
    }

    public void ouvrirPlaningActivity(){
        Intent intent = new Intent(getApplicationContext(), PlaningActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void showNotificationAjouteReservationImpossible(){
        dialog = new Dialog(PlaningActivity.this);
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
        desc.setText(R.string.desc_erreur_rservations);

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.booking_error));

        dialog.show();
    }

    public Date convertStringToDate(String chaine){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = format.parse(chaine);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Date convertStringToTime(String chaine){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;

        try {
            date = format.parse(chaine);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void chechIfDateOfBookExist(ProgressDialog progressDialog){
        databaseReference.child("reservations").orderByChild("date").equalTo(dateBooking.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    checkIfCourtExist(progressDialog);
                }

                else{
                    ajouterReservation(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showNotificationAjouteReservationImpossible();
            }
        });
    }

    public void checkIfCourtExist(ProgressDialog progressDialog){
        databaseReference.child("reservations").orderByChild("court").equalTo(radioButton.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    checkIfTimeBookingExist(progressDialog);
                }

                else{
                    ajouterReservation(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showNotificationAjouteReservationImpossible();
            }
        });
    }

    public void checkIfTimeBookingExist(ProgressDialog progressDialog){
        databaseReference.child("reservations").orderByChild("fromTime").startAt(fromTime.getText().toString()).endAt(toTime.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressDialog.dismiss();
                    showNotificationAjouteReservationImpossibleTime();
                }

                else{
                    ajouterReservation(progressDialog);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showNotificationAjouteReservationImpossible();
            }
        });
    }
}