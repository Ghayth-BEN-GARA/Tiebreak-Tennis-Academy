package com.example.tiebreaktennisacademy.Fragements;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tiebreaktennisacademy.Activities.CoachesActivity;
import com.example.tiebreaktennisacademy.Activities.CourtsActivity;
import com.example.tiebreaktennisacademy.Activities.PlaningActivity;
import com.example.tiebreaktennisacademy.Activities.PlayersActivity;
import com.example.tiebreaktennisacademy.R;

public class HomeFragment extends Fragment {
    private CardView cardCoache, cardPlayer, cardCourt, cardPlaning;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        cardCoache = (CardView) view.findViewById(R.id.card_coache);
        cardPlayer = (CardView) view.findViewById(R.id.card_players);
        cardCourt = (CardView) view.findViewById(R.id.card_court);
        cardPlaning = (CardView) view.findViewById(R.id.card_planing);

        onclickFunctions();
        return view;
    }

    public void onclickFunctions(){
        cardCoache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirCoachActivity();
            }
        });

        cardPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirPlayersActivity();
            }
        });

        cardCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirCourtsActivity();
            }
        });

        cardPlaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirPlaningActivity();
            }
        });
    }

    public void ouvrirCoachActivity(){
        Intent intent = new Intent(getActivity(), CoachesActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirPlayersActivity(){
        Intent intent = new Intent(getActivity(), PlayersActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirCourtsActivity(){
        Intent intent = new Intent(getActivity(), CourtsActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirPlaningActivity(){
        Intent intent = new Intent(getActivity(), PlaningActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}