package com.example.tiebreaktennisacademy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiebreaktennisacademy.Models.Reservation;
import com.example.tiebreaktennisacademy.R;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Reservation> reservationArrayList;

    public ReservationAdapter(Context context, ArrayList<Reservation> reservations){
        this.context = context;
        this.reservationArrayList = reservations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView coache;
        private TextView coacheNom, date, court, from, to;

        public ViewHolder(View itemView) {
            super(itemView);

            coache = (CircleImageView) itemView.findViewById(R.id.photo_coache);
            coacheNom = (TextView) itemView.findViewById(R.id.coache);
            date = (TextView) itemView.findViewById(R.id.date);
            court = (TextView) itemView.findViewById(R.id.court);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
        }
    }

    @Override
    public ReservationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.iem_show_date,parent,false);
        return new ReservationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReservationAdapter.ViewHolder holder, int position) {
        holder.coacheNom.setText(reservationArrayList.get(position).getCoache());
        holder.date.setText(reservationArrayList.get(position).getDate());
        holder.court.setText(reservationArrayList.get(position).getCourt());
        holder.from.setText(reservationArrayList.get(position).getFrom());
        holder.to.setText(reservationArrayList.get(position).getTo());
        if(reservationArrayList.get(position).getCoache().equals(context.getResources().getString(R.string.nom_mal))){
            holder.coache.setImageResource(R.drawable.mal);
        }

        else if(reservationArrayList.get(position).getCoache().equals(context.getResources().getString(R.string.nom_kieron))){
            holder.coache.setImageResource(R.drawable.kieron);
        }

        else if(reservationArrayList.get(position).getCoache().equals(context.getResources().getString(R.string.nom_jarred))){
            holder.coache.setImageResource(R.drawable.jarred);
        }

        else{
            holder.coache.setImageResource(R.drawable.user);
        }
    }

    @Override
    public int getItemCount() {
        return reservationArrayList.size();
    }
}
