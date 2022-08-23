package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.Adapters.HelpAdapter;
import com.example.tiebreaktennisacademy.Models.Help;
import com.example.tiebreaktennisacademy.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import me.relex.circleindicator.CircleIndicator;

public class Help2Activity extends AppCompatActivity {
    private ImageView back;
    private TextView copiright;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private HelpAdapter helpAdapter;
    private List<Help> helpList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help2);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);

        onclickFunctions();
        setCopyrightText();
        initialisationListeHelp();
        remplirListeHelp();
        attacherAdapterAvecItems();
        configurerIndicateurs();
        indicateurListener();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirHomeActivity();
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

    public void initialisationListeHelp(){
        helpList = new ArrayList<>();
    }

    public void remplirListeHelp(){
        helpList.add(new Help(R.drawable.players,getString(R.string.players),getString(R.string.players_description)));
        helpList.add(new Help(R.drawable.coach2,getString(R.string.coaches),getString(R.string.coaches_description)));
        helpList.add(new Help(R.drawable.court,getString(R.string.court),getString(R.string.court_description)));
        helpList.add(new Help(R.drawable.planning,getString(R.string.planning),getString(R.string.planing_description)));
        helpList.add(new Help(R.drawable.profile,getString(R.string.account),getString(R.string.profile_description)));
        helpList.add(new Help(R.drawable.donnes,getString(R.string.parametres),getString(R.string.config_description)));
    }

    public void attacherAdapterAvecItems(){
        helpAdapter = new HelpAdapter(getApplicationContext(),helpList);
        viewPager.setAdapter(helpAdapter);
    }

    public void configurerIndicateurs(){
        circleIndicator.setViewPager(viewPager);
    }

    public void indicateurListener(){
        helpAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }
}