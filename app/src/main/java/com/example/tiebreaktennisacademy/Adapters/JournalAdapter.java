package com.example.tiebreaktennisacademy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tiebreaktennisacademy.Models.Journal;
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
import java.util.Date;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Journal> journals;
    private DatabaseReference databaseReference;

    public JournalAdapter(Context context, ArrayList<Journal> journals) {
        this.context = context;
        this.journals = journals;
        initialiseDataBase();
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public String emailSession(){
        Session session = new Session(context);
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView phone, action, dateTime;
        CircleImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            phone = (TextView) itemView.findViewById(R.id.appareil);
            action = (TextView) itemView.findViewById(R.id.action);
            dateTime = (TextView) itemView.findViewById(R.id.date_time);
            photo = (CircleImageView) itemView.findViewById(R.id.photo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_journal,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Journal journal = journals.get(position);
        holder.phone.setText(journal.getPhone());
        holder.action.setText(journal.getAction());
        holder.dateTime.setText(stylingDate(journal.getDate()) + " à " + journal.getTime());

        databaseReference.child("images_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(encodeString(emailSession())).child("photo").getValue(String.class) != null){

                    Glide
                            .with(context)
                            .load(snapshot.child(encodeString(emailSession())).child("photo").getValue(String.class))
                            .centerCrop()
                            .into(holder.photo);
                }

                else{
                    holder.photo.setImageResource(R.drawable.user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    public String stylingDate(String text){
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat formatter2 = new SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter1.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter2.format(date);
    }

}
