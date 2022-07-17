package com.example.tiebreaktennisacademy.Fragements;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
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

public class AccountFragment extends Fragment {
    private TextView fullname, emailP, phone, gender, naissance, taille, poid;
    private ScrollView scrollView;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        fullname = (TextView) view.findViewById(R.id.fullname);
        emailP = (TextView) view.findViewById(R.id.email);
        phone = (TextView) view.findViewById(R.id.mobile);
        gender = (TextView) view.findViewById(R.id.gender);
        naissance = (TextView) view.findViewById(R.id.naissance);
        taille = (TextView) view.findViewById(R.id.taille);
        poid = (TextView) view.findViewById(R.id.poid);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);

        initialiseDataBase();
        setDataPersonne();

        return view;
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void setDataPersonne(){
        Session session = new Session(getActivity());
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
}