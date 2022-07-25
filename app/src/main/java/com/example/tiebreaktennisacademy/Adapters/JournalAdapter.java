package com.example.tiebreaktennisacademy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiebreaktennisacademy.Models.Journal;
import com.example.tiebreaktennisacademy.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Journal> journals;

    public JournalAdapter(Context context, ArrayList<Journal> journals) {
        this.context = context;
        this.journals = journals;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView phone, action, dateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            phone = (TextView) itemView.findViewById(R.id.appareil);
            action = (TextView) itemView.findViewById(R.id.action);
            dateTime = (TextView) itemView.findViewById(R.id.date_time);
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
