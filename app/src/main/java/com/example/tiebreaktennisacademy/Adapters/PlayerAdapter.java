package com.example.tiebreaktennisacademy.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tiebreaktennisacademy.Models.Player;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Player> playerArrayList;
    private DatabaseReference databaseReference;

    public PlayerAdapter(Context context, ArrayList<Player> players) {
        this.context = context;
        this.playerArrayList = players;
        initialiseDataBase();
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nomPrenom, email;
        CircleImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            nomPrenom = (TextView) itemView.findViewById(R.id.nom_prenom);
            email = (TextView) itemView.findViewById(R.id.email);
            photo = (CircleImageView) itemView.findViewById(R.id.photo_player);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_player,parent,false);
        return new PlayerAdapter.ViewHolder(v);
    }

    public String emailSession(){
        Session session = new Session(context);
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nomPrenom.setText(playerArrayList.get(position).getFullname());
        if(decodeString(playerArrayList.get(position).getEmail()).equals(emailSession())){
            holder.email.setText(decodeString(playerArrayList.get(position).getEmail())+ context.getString(R.string.your_account));
        }

        else{
            holder.email.setText(decodeString(playerArrayList.get(position).getEmail()));
        }


        databaseReference.child("images_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(playerArrayList.get(position).getEmail())){
                    holder.photo.setImageResource(R.drawable.user);
                }

                else{
                    databaseReference.child("images_users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot photoSnapshot) {
                            Glide
                                    .with(context)
                                    .load(photoSnapshot.child(playerArrayList.get(position).getEmail()).child("photo").getValue(String.class))
                                    .centerCrop()
                                    .into(holder.photo);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }

    public static String decodeString(String string) {
        return string.replace(",", ".");
    }
}
